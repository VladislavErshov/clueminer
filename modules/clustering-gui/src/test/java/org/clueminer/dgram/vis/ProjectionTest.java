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
package org.clueminer.dgram.vis;

import java.awt.BorderLayout;
import java.awt.Image;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.clueminer.clustering.algorithm.KMeans;
import org.clueminer.clustering.api.Cluster;
import org.clueminer.clustering.api.Clustering;
import org.clueminer.clustering.api.ClusteringAlgorithm;
import org.clueminer.clustering.api.dendrogram.DendrogramMapping;
import org.clueminer.clustering.api.dendrogram.DendrogramVisualizationListener;
import org.clueminer.colors.ColorBrewer;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.fixtures.clustering.FakeDatasets;
import org.clueminer.utils.PropType;
import org.clueminer.utils.Props;
import org.openide.util.Exceptions;

/**
 *
 * @author deric
 */
public class ProjectionTest<E extends Instance, C extends Cluster<E>> extends JFrame implements DendrogramVisualizationListener<E, C> {

    private JLabel picLabel;

    public ProjectionTest() throws TimeoutException {
        setLayout(new BorderLayout());
        Dataset<E> dataset = (Dataset<E>) FakeDatasets.irisDataset();

        Props prop = new Props();
        ClusteringAlgorithm<E, C> algorithm = new KMeans();
        algorithm.setColorGenerator(new ColorBrewer());
        prop.put("k", 4);
        Clustering<E, C> clustering = algorithm.cluster(dataset, prop);

        prop.put(PropType.VISUAL, "img_width", 600);
        prop.put(PropType.VISUAL, "img_height", 600);
        prop.put(PropType.VISUAL, "visualization", "Projection");
        prop.put(PropType.VISUAL, "projection", "PCA");
        final DendrogramMapping mapping = clustering.getLookup().lookup(DendrogramMapping.class);
        Future<? extends Image> future = ImageFactory.getInstance().generateImage(clustering, prop, this, mapping);
        Image image;
        try {
            image = future.get(5, TimeUnit.SECONDS);
            picLabel = new JLabel(new ImageIcon(image));
            add(picLabel);
        } catch (InterruptedException | ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    // this function will be run from the EDT
    private static void createAndShowGUI() throws Exception {
        ProjectionTest frame = new ProjectionTest();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    createAndShowGUI();
                } catch (Exception e) {
                    System.err.println(e);
                    Exceptions.printStackTrace(e);
                }
            }
        });
    }

    @Override
    public void previewUpdated(final Image preview) {
        System.out.println("updating preview");
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                picLabel.setIcon(new ImageIcon(preview));
                validate();
                revalidate();
                repaint();
            }
        });
    }

    @Override
    public void clusteringFinished(Clustering<E, C> clustering) {
        //nothing to do
    }

}
