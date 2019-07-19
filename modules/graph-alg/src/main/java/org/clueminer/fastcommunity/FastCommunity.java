/*
 * Copyright (C) 2011-2019 clueminer.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.clueminer.fastcommunity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import org.clueminer.clustering.algorithm.HClustResult;
import org.clueminer.clustering.api.AgglomerativeClustering;
import org.clueminer.clustering.api.AlgParams;
import org.clueminer.clustering.api.Algorithm;
import org.clueminer.clustering.api.Cluster;
import org.clueminer.clustering.api.Clustering;
import org.clueminer.clustering.api.ClusteringAlgorithm;
import org.clueminer.clustering.api.Configurator;
import org.clueminer.clustering.api.HierarchicalResult;
import org.clueminer.clustering.api.dendrogram.DendroNode;
import org.clueminer.clustering.api.dendrogram.DendroTreeData;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.distance.api.DistanceFactory;
import org.clueminer.graph.adjacencyList.AdjListFactory;
import org.clueminer.graph.adjacencyList.AdjListGraph;
import org.clueminer.graph.api.GraphConvertor;
import org.clueminer.graph.api.GraphConvertorFactory;
import org.clueminer.graph.api.Node;
import org.clueminer.hclust.DLeaf;
import org.clueminer.hclust.DTreeNode;
import org.clueminer.hclust.DynamicTreeData;
import org.clueminer.utils.Props;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Hamster
 * @param <E>
 * @param <C>
 */
@ServiceProvider(service = ClusteringAlgorithm.class)
public class FastCommunity<E extends Instance, C extends Cluster<E>> extends Algorithm<E, C> implements AgglomerativeClustering<E, C> {

    //private CommunityNetwork network;

    public static final String GRAPH_CONV = "graph_conv";
    /* @Param(name = FastCommunity.GRAPH_CONV,           factory = "org.clueminer.graph.api.GraphConvertorFactory",
           type = org.clueminer.clustering.params.ParamType.STRING) */

    @Override
    public String getName() {
        return "Fast Community";
    }

    @Override
    public HierarchicalResult hierarchy(Dataset<E> dataset, Props pref) {
        AdjListGraph graph = new AdjListGraph();
        String dist = pref.get("distance", "Euclidean");
        this.distanceFunction = DistanceFactory.getInstance().getProvider(dist);
        pref.put("algorithm", getName());

        Long[] mapping = AdjListFactory.getInstance().createNodesFromInput(dataset, graph);
        String initializer = pref.get(GRAPH_CONV, "k-NNG");
        GraphConvertor graphCon = GraphConvertorFactory.getInstance().getProvider(initializer);
        graphCon.setDistanceMeasure(distanceFunction);
        graphCon.createEdges(graph, dataset, mapping, pref);

        HierarchicalResult result = new HClustResult(dataset, pref);
        pref.put(AlgParams.ALG, getName());
        int n = dataset.size();
        int items = triangleSize(n);
        PriorityQueue<ReverseElement> pq = new PriorityQueue<>(items);
        DeltaQMatrix dQ = new DeltaQMatrix(pq);

        DendroTreeData treeData = computeLinkage(dataset, n, dQ, graph, pq);

        treeData.createMapping(n, treeData.getRoot());
        result.setTreeData(treeData);
        return result;
    }

    @Override
    public boolean isLinkageSupported(String linkage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Map<Integer, Community> initialAssignment(int n, Dataset<? extends Instance> dataset,
            DendroNode[] nodes, AdjListGraph graph, CommunityNetwork network) {
        Map<Integer, Community> clusterAssignment = new HashMap<>(n);

        int i = 0;
        for (Node node : graph.getNodes()) {
            Community community = new Community(graph, i, node);
            clusterAssignment.put(i, community);
            nodes[i] = new DLeaf(i, dataset.get(i));
            network.add(community);
            i++;
        }
        network.initConnections(graph);

        return clusterAssignment;
    }

    private DendroTreeData computeLinkage(Dataset<? extends Instance> dataset, int n,
            DeltaQMatrix dQ, AdjListGraph graph, PriorityQueue<ReverseElement> pq) {
        DendroNode[] nodes = new DendroNode[(2 * n - 1)];
        CommunityNetwork network = new CommunityNetwork(dQ, graph.getEdgeCount());

        Map<Integer, Community> assignments = initialAssignment(n, dataset, nodes, graph, network);

        dQ.build(graph);

        populatePriorityQueue(dQ, graph, pq);

        ReverseElement current;
        DendroNode node = null;
        Community left, right;
        int nodeId = n;

        while (!pq.isEmpty() && assignments.size() > 1) {
            current = pq.poll();
            int i = current.getRow();
            int j = current.getColumn();
            if (i > j) {
                int tmp = i;
                i = j;
                j = tmp;
            }
            node = getOrCreate(nodeId++, nodes);
            node.setLeft(nodes[i]);
            node.setRight(nodes[j]);
            node.setHeight(current.getValue());

            left = assignments.remove(i);
            right = assignments.remove(j);

            left.addAll(right);
            assignments.put(node.getId(), left);
            network.merge(i, j);
        }
        while (assignments.size() > 1) {
            Iterator<Integer> it = assignments.keySet().iterator();
            Integer i = it.next();
            Integer j = it.next();
            if (i > j) {
                int tmp = i;
                i = j;
                j = tmp;
            }

            node = getOrCreate(nodeId++, nodes);
            node.setLeft(nodes[i]);
            node.setRight(nodes[j]);
            // These nodes are not connected
            // thus we want to cut the dendrogram here
            node.setHeight(Double.MAX_VALUE);

            left = assignments.remove(i);
            right = assignments.remove(j);

            left.addAll(right);
            assignments.put(node.getId(), left);
            network.merge(i, j);
        }

        //last node is the root
        DendroTreeData treeData = new DynamicTreeData(node);
        return treeData;
    }

    private DendroNode getOrCreate(int id, DendroNode[] nodes) {
        if (nodes[id] == null) {
            DendroNode node = new DTreeNode(id);
            nodes[id] = node;
        }
        return nodes[id];
    }

    private int triangleSize(int n) {
        return ((n - 1) * n) >>> 1;
    }

    private void populatePriorityQueue(DeltaQMatrix dQ, AdjListGraph graph, PriorityQueue<ReverseElement> pq) {
        for (int i = 0; i < graph.getNodeCount(); i++) {
            for (int j = 0; j < graph.getNodeCount(); j++) {
                ReverseElement element = dQ.get(i, j);
                if (element != null && !pq.contains(element)) {
                    pq.add(element);
                }
            }
        }
    }

    @Override
    public Clustering<E, C> cluster(Dataset<E> dataset, Props props) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Configurator<E> getConfigurator() {
        return FcConfig.getInstance();
    }

    @Override
    public boolean isDeterministic() {
        return true;
    }
}
