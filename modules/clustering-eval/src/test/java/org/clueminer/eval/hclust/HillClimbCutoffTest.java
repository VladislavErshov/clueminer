package org.clueminer.eval.hclust;

import org.clueminer.clustering.aggl.HCLW;
import org.clueminer.clustering.aggl.linkage.SingleLinkage;
import org.clueminer.clustering.api.AlgParams;
import org.clueminer.clustering.api.ClusteringType;
import org.clueminer.clustering.api.HierarchicalResult;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.eval.ALE;
import org.clueminer.fixtures.clustering.FakeDatasets;
import org.clueminer.utils.Props;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author deric
 */
public class HillClimbCutoffTest {

    private static final HillClimbCutoff subject = new HillClimbCutoff();
    private Dataset<? extends Instance> dataset;

    public HillClimbCutoffTest() {
        subject.setEvaluator(new ALE());
    }

    @Before
    public void setUp() {
        dataset = FakeDatasets.schoolData();
    }

    @Test
    public void testFindCutoff() {
        HCLW alg = new HCLW();
        Props pref = new Props();
        pref.put(AlgParams.LINKAGE, SingleLinkage.name);
        pref.put(AlgParams.CLUSTERING_TYPE, ClusteringType.ROWS_CLUSTERING);
        HierarchicalResult result = alg.hierarchy(dataset, pref);

        result.getTreeData().print();

        double cut = subject.findCutoff(result, pref);
        assertEquals(true, cut > 0);
        System.out.println("cutoff = " + cut);
        int numClusters = result.getClustering().size();
        System.out.println("clustering size: " + numClusters);
        assertEquals(true, numClusters < 4);
    }

}
