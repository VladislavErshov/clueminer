/*
 * Copyright (C) 2011-2017 clueminer.org
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
package org.clueminer.explorer;

import java.awt.Image;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import org.clueminer.clustering.api.Cluster;
import org.clueminer.clustering.api.Clustering;
import org.clueminer.clustering.api.EvaluationTable;
import org.clueminer.clustering.api.dendrogram.DendrogramMapping;
import org.clueminer.clustering.api.dendrogram.DendrogramVisualizationListener;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.dgram.vis.ImageFactory;
import org.clueminer.eval.utils.HashEvaluationTable;
import org.clueminer.gui.EvaluatorProperty;
import org.clueminer.utils.Props;
import org.netbeans.api.progress.ProgressHandle;
import org.openide.actions.NewAction;
import org.openide.actions.PasteAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.NodeTransfer;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;
import org.openide.util.Task;
import org.openide.util.TaskListener;
import org.openide.util.actions.SystemAction;
import org.openide.util.datatransfer.PasteType;
import org.openide.util.lookup.Lookups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Tomas Barton
 * @param <E>
 * @param <C>
 */
public class ClusteringNode<E extends Instance, C extends Cluster<E>> extends AbstractNode implements DendrogramVisualizationListener<E, C> {

    private Image image;
    DendrogramMapping mapping;
    private boolean rendering = false;
    private static final Logger LOG = LoggerFactory.getLogger(ClusteringNode.class);
    private final ReentrantLock lock = new ReentrantLock();
    private static final RequestProcessor RP = new RequestProcessor("clustering properties");

    public ClusteringNode(Clustering<E, C> clusters) {
        super(Children.LEAF, Lookups.singleton(clusters));
        String name = generateName();
        setDisplayName(name);
        setName(name);
    }

    /**
     * Generate thumbnail of clustering
     *
     * @param type
     * @return
     */
    @Override
    public Image getIcon(int type) {
        if (image == null && rendering == false) {
            Clustering clustering = getClustering();
            updateIcon(clustering.getParams());
        }
        if (image == null) {
            return ImageFactory.loading();
        }
        return image;
    }

    public Clustering<E, C> getClustering() {
        return getLookup().lookup(Clustering.class);
    }

    @Override
    public PasteType getDropType(Transferable t, final int action, int index) {
        final Node dropNode = NodeTransfer.node(t, DnDConstants.ACTION_COPY_OR_MOVE + NodeTransfer.CLIPBOARD_CUT);
        if (null != dropNode) {
            /* final Movie movie = (Movie)dropNode.getLookup().lookup( Movie.class );
             * if( null != movie && !this.equals( dropNode.getParentNode() )) {
             * return new PasteType() {
             * public Transferable paste() throws IOException {
             * getChildren().add( new Node[] { new MovieNode(movie) } );
             * if( (action & DnDConstants.ACTION_MOVE) != 0 ) {
             * dropNode.getParentNode().getChildren().remove( new Node[] {dropNode} );
             * }
             * return null;
             * }
             * };
             * } */
        }
        return null;
    }

    private String generateName() {
        Clustering<E, C> clustering = getClustering();
        if (clustering != null) {
            String name = clustering.getName();
            if (name.length() > 10) {
                return name.substring(0, 7) + "...|" + clustering.size() + "|";
            }
            return name;
        }
        return "(missing)";
    }

    @Override
    public Cookie getCookie(Class clazz) {
        Children ch = getChildren();

        if (clazz.isInstance(ch)) {
            return (Cookie) ch;
        }

        return super.getCookie(clazz);
    }

    @Override
    protected void createPasteTypes(Transferable t, List s) {
        super.createPasteTypes(t, s);
        PasteType paste = getDropType(t, DnDConstants.ACTION_COPY, -1);
        if (null != paste) {
            s.add(paste);
        }
    }

    @Override
    public Action[] getActions(boolean context) {
        return new Action[]{
            SystemAction.get(NewAction.class),
            SystemAction.get(PasteAction.class)};
    }

    @Override
    public boolean canDestroy() {
        return true;
    }

    private void computeClusterProperties(final Sheet sheet, final Clustering<E, C> clustering) {

        final ProgressHandle ph = ProgressHandle.createHandle("Computing properties of " + clustering.getName());

        final RequestProcessor.Task taskMetrics = RP.create(new Runnable() {
            @Override
            public void run() {
                Sheet.Set set = sheet.get(Sheet.PROPERTIES);
                if (set == null) {
                    set = Sheet.createPropertiesSet();
                }
                try {
                    set.setDisplayName("Clustering (" + clustering.size() + ")");
                    Property nameProp = new PropertySupport.Reflection(clustering, String.class, "getName", null);
                    nameProp.setName("Name");
                    set.put(nameProp);

                    Property sizeProp = new PropertySupport.Reflection(clustering, Integer.class, "size", null);
                    sizeProp.setName("Size");
                    set.put(sizeProp);

                } catch (NoSuchMethodException ex) {
                    Exceptions.printStackTrace(ex);
                }

                sheet.put(set);
                algorithmSheet(clustering, sheet);
                internalSheet(clustering, sheet);
                externalSheet(clustering, sheet);
            }
        });

        taskMetrics.addTaskListener(new TaskListener() {
            @Override
            public void taskFinished(Task task) {
                LOG.info("finished computing properties");
                firePropertySetsChange(null, null);
            }
        });
        taskMetrics.schedule(0);

    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Clustering<E, C> clustering = getClustering();
        if (clustering != null) {
            computeClusterProperties(sheet, clustering);
        }
        return sheet;
    }

    private Dataset<E> getDataset(Clustering<E, C> clustering) {
        Dataset<E> dataset = clustering.getLookup().lookup(Dataset.class);
        if (dataset == null) {
            LOG.warn("no dataset in lookup");
        }
        return dataset;
    }

    protected EvaluationTable<E, C> evaluationTable(Clustering<E, C> clustering) {
        EvaluationTable<E, C> evalTable = clustering.getEvaluationTable();
        //we try to compute score just once, to eliminate delays
        if (evalTable == null) {
            Dataset<E> dataset = getDataset(clustering);
            evalTable = new HashEvaluationTable<>(clustering, dataset);
            clustering.setEvaluationTable(evalTable);
        }
        return evalTable;
    }

    private void internalSheet(Clustering<E, C> clustering, Sheet sheet) {
        Sheet.Set set = new Sheet.Set();
        EvaluationTable<E, C> evalTable = evaluationTable(clustering);
        set.setName("Internal Evaluation");
        set.setDisplayName("Internal Evaluation");
        for (final Entry<String, Double> score : evalTable.getInternal().entrySet()) {
            Property evalProp = new EvaluatorProperty(score.getKey(), score.getValue());
            set.put(evalProp);
        }
        sheet.put(set);
    }

    private void externalSheet(Clustering<E, C> clustering, Sheet sheet) {
        Dataset<? extends Instance> dataset = getDataset(clustering);
        //we need dataset for external evaluation
        if (dataset != null) {
            if (dataset.getClasses().size() > 0) {
                Sheet.Set set = new Sheet.Set();
                EvaluationTable<E, C> evalTable = evaluationTable(clustering);
                set.setName("External Evaluation");
                set.setDisplayName("External Evaluation");
                for (final Entry<String, Double> score : evalTable.getExternal().entrySet()) {
                    Property evalProp = new EvaluatorProperty(score.getKey(), score.getValue());
                    evalProp.setDisplayName(score.getKey());
                    set.put(evalProp);
                }

                sheet.put(set);
            } else {
                LOG.warn("dataset {} seem to have {} classes",
                        dataset.getName(), dataset.getClasses().size());
            }
        }
    }

    private void algorithmSheet(Clustering<E, C> clustering, Sheet sheet) {
        final Props params = clustering.getParams();
        if (params == null) {
            return;
        }
        Sheet.Set set = new Sheet.Set();
        set.setName("Algorithm");
        set.setDisplayName("Algorithm");
        for (final String key : params.keySet()) {
            Property evalProp = new PropertySupport.ReadOnly<Object>(key, Object.class, "", "") {
                @Override
                public String getValue() throws IllegalAccessException, InvocationTargetException {
                    return params.get(key, "");
                }
            };
            evalProp.setDisplayName(key);
            set.put(evalProp);
        }
        sheet.put(set);
    }

    @Override
    public void clusteringFinished(Clustering<E, C> clustering) {
        //not much to do
    }

    public Image updateIcon(Props prop) {
        if (rendering == false) {
            lock.lock();
            try {
                Clustering clustering = getClustering();
                //ensure that for each clustering we submit exatctly one task
                rendering = true;
                //image should be updated asynchronously when image is generated

                final DendrogramMapping mapping = clustering.getLookup().lookup(DendrogramMapping.class);
                Future<? extends Image> future = ImageFactory.getInstance().generateImage(clustering, prop, this, mapping);
                if (future.isDone()) {
                    return future.get();
                }
                //image is rendering, wait for it...
                return ImageFactory.loading();
            } catch (InterruptedException | ExecutionException ex) {
                Exceptions.printStackTrace(ex);
            } finally {
                lock.unlock();
            }
        } else {
            LOG.warn("another icon update in progress");
        }
        if (image == null) {
            return ImageFactory.loading();
        }
        return image;
    }

    @Override
    public void previewUpdated(Image preview) {
        this.image = preview;
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                rendering = false;
                fireIconChange();
            }
        });
    }
}
