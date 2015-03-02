/*
 * Copyright (C) 2015 clueminer.org
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
package org.clueminer.evaluation.inline;

import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.clueminer.clustering.api.ClusterEvaluation;
import org.clueminer.clustering.api.Clustering;
import org.clueminer.clustering.api.EvaluationTable;
import org.clueminer.clustering.api.dendrogram.ColorScheme;
import org.clueminer.clustering.api.factory.InternalEvaluatorFactory;
import org.clueminer.clustering.gui.colors.ColorSchemeImpl;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.eval.AICScore;
import org.clueminer.eval.external.NMI;
import org.clueminer.eval.utils.ClusteringComparator;
import org.clueminer.gui.BPanel;
import org.clueminer.std.StdScale;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;
import org.openide.util.Task;
import org.openide.util.TaskListener;

/**
 *
 * @author deric
 */
public class ScorePlot extends BPanel implements TaskListener {

    private static final long serialVersionUID = -4456572592761477081L;

    private Collection<? extends Clustering> clusterings;
    private Clustering[] internal;
    private Clustering[] external;
    private ClusteringComparator compInternal;
    private ClusteringComparator compExternal;
    protected Font defaultFont;
    protected Font headerFont;
    protected int lineHeight = 12;
    protected int elemHeight = 20;
    protected int fontSize = 10;
    private int headerHeight;
    protected float headerFontSize = 10;
    private int maxWidth;
    private Insets insets = new Insets(5, 5, 5, 5);
    private Object2IntOpenHashMap<Clustering> matching;
    static BasicStroke wideStroke = new BasicStroke(8.0f);
    private double strokeW;
    private ColorScheme colorScheme;
    private double minDist;
    private double midDist;
    private double maxDist;
    private static final RequestProcessor RP = new RequestProcessor("sorting...", 100, false, true);
    private Object2DoubleOpenHashMap<String> results;
    private Color fontColor;
    private final StdScale scale;

    public ScorePlot() {
        defaultFont = new Font("verdana", Font.PLAIN, fontSize);
        headerFont = defaultFont.deriveFont(Font.BOLD);
        scale = new StdScale();
        this.fitToSpace = false;
        this.preserveAlpha = true;
        compInternal = new ClusteringComparator(new AICScore());
        compExternal = new ClusteringComparator(new NMI());
        colorScheme = new ColorSchemeImpl(Color.green, Color.BLACK, Color.RED);
        results = new Object2DoubleOpenHashMap<>();
        try {
            initialize();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void initialize() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        fontColor = defaults.getColor("controlText");
        setBackground(defaults.getColor("window"));
    }

    void setEvaluatorX(final ClusterEvaluation provider) {
        if (internal != null && internal.length > 1) {
            final ProgressHandle ph = ProgressHandleFactory.createHandle("computing " + provider.getName());
            RP.post(new Runnable() {

                @Override
                public void run() {
                    ph.start();
                    Arrays.sort(internal, new ClusteringComparator(provider));
                    compInternal.setEvaluator(provider);
                    clusteringChanged();
                    ph.finish();
                    results = new Object2DoubleOpenHashMap<>();
                }
            });

        }
    }

    void setEvaluatorY(final ClusterEvaluation provider) {
        if (external != null && external.length > 1) {
            final ProgressHandle ph = ProgressHandleFactory.createHandle("computing " + provider.getName());
            RequestProcessor.Task task = RP.post(new Runnable() {

                @Override
                public void run() {
                    ph.start();
                    ClusteringComparator compare = new ClusteringComparator(provider);
                    try {
                        Arrays.sort(external, compare);
                    } catch (IllegalArgumentException e) {
                        System.err.println("sorting error during " + provider.getName());
                        double[] score = new double[external.length];
                        EvaluationTable et;
                        for (int i = 0; i < score.length; i++) {
                            et = compare.evaluationTable(external[i]);
                            score[i] = et.getScore(provider);
                        }
                        System.out.println(Arrays.toString(score));
                    }
                    compExternal.setEvaluator(provider);
                    ph.finish();
                }
            });
            task.addTaskListener(this);
        }
    }

    public void setClusterings(final Collection<Clustering> clusters) {
        RequestProcessor.Task task = RP.post(new Runnable() {

            @Override
            public void run() {
                results = new Object2DoubleOpenHashMap<>();
                internal = clusters.toArray(new Clustering[clusters.size()]);
                Arrays.sort(internal, compInternal);

                external = clusters.toArray(new Clustering[clusters.size()]);
                Arrays.sort(external, compExternal);
                clusterings = clusters;
            }
        });
        task.addTaskListener(this);
    }

    /**
     * Compute sorting distance for all evaluation metrics
     */
    private void computeAll() {
        InternalEvaluatorFactory ief = InternalEvaluatorFactory.getInstance();
        ClusteringComparator compare = new ClusteringComparator();
        for (ClusterEvaluation eval : ief.getAll()) {

            if (!results.containsKey(eval.getName())) {
                System.out.println("computing " + eval.getName());
                compare.setEvaluator(eval);
                try {
                    Arrays.sort(external, compare);
                } catch (IllegalArgumentException e) {
                    System.err.println("sorting error during " + eval.getName());
                }
            }
        }
    }

    @Override
    public void taskFinished(Task task) {
        clusteringChanged();
    }

    protected void clusteringChanged() {
        if (hasData()) {
            resetCache();
        }
    }

    private int itemsCnt() {
        if (clusterings == null) {
            return 0;
        }
        return clusterings.size();
    }

    private double scoreMin(Clustering[] clust, ClusteringComparator comp) {
        double res = Double.NaN;
        if (clust != null && clust.length > 0) {
            if (comp.getEvaluator().isMaximized()) {
                res = comp.getScore(clust[0]);
            }
        }

        return res;
    }

    private double scoreMax(Clustering[] clust, ClusteringComparator comp) {
        double res = Double.NaN;
        if (clust != null && clust.length > 0) {
            if (comp.getEvaluator().isMaximized()) {
                res = comp.getScore(clust[clust.length - 1]);
            }
        }

        return res;
    }

    @Override
    public void render(Graphics2D g) {
        float xB = getSize().width - maxWidth;
        //canvas dimensions
        double cxMin, cxMax, cyMin, cyMax;
        cxMin = 0.0;
        cxMax = getSize().width;
        cyMin = 0.0;
        cyMax = getSize().height;
        int mid = (int) (cyMax / 2);
        Clustering clust;
        double x1, y1, y2;
        double xmin, xmax, ymin, ymax;

        ymin = scoreMin(internal, compInternal);
        ymax = scoreMax(internal, compInternal);
        xmin = scoreMin(external, compExternal);
        xmax = scoreMax(external, compExternal);

        headerHeight = drawHeader(g);
        //set font for rendering rows
        g.setFont(defaultFont);
        double xVal, yVal, score;
        int rectWidth = 10; //TODO: fix this
        //draw
        for (int col = 0; col < external.length; col++) {
            //left clustering
            clust = external[col];
            g.setColor(fontColor);

            score = compExternal.getScore(clust);
            xVal = scale.scaleToRange(score, xmin, xmax, cxMin, cxMax);
            score = compInternal.getScore(clust);
            yVal = scale.scaleToRange(score, ymin, ymax, cyMin, cyMax);
            drawClustering(g, clust, rectWidth, xVal, yVal, mid);

            g.setStroke(wideStroke);

            // g.setColor(colorScheme.getColor(dist, minDist, midDist, maxDist));
            // g.draw(line);
            // g.setStroke(wideStroke);
            //  g.draw(new Line2D.Double(10.0, 50.0, 100.0, 50.0));
        }
        g.setColor(fontColor);
        //average distance per item        
        g.dispose();
    }

    /**
     *
     * @param g
     * @return height of drawn header
     */
    private int drawHeader(Graphics2D g) {
        g.setColor(fontColor);
        //approx one third
        int colWidth = getSize().width / 3;
        g.setFont(headerFont);
        String eval1 = compInternal.getEvaluator().getName();
        String eval2 = compExternal.getEvaluator().getName();
        updateHeaderFont(eval1, eval2, colWidth, g);

        int strWidth = stringWidth(headerFont, g, eval1);
        int x = (colWidth - strWidth) / 2;
        int y = (int) (headerFontSize + g.getFontMetrics().getDescent() * 2);
        g.drawString(eval1, x, y);

        //3rd column
        strWidth = stringWidth(headerFont, g, eval2);
        x = 2 * colWidth + (colWidth - strWidth) / 2;
        g.drawString(eval2, x, y);
        return y + 20;
    }

    private int stringWidth(Font f, Graphics2D g2, String str) {
        return (int) (f.getStringBounds(str, g2.getFontRenderContext()).getWidth());
    }

    private void drawDistance(Graphics2D g2, double distance) {
        g2.setColor(fontColor);
        int colWidth = getSize().width / 3;
        String str = String.format("%.2f", distance) + " (" + clusterings.size() + ")";
        g2.setFont(headerFont);
        int strWidth = stringWidth(headerFont, g, str);
        // 2nd column
        int x = colWidth + (colWidth - strWidth) / 2;
        int y = (int) (headerFontSize + g.getFontMetrics().getDescent() * 2);
        g.drawString(str, x, y);
    }

    /**
     * Adjust font size to given 3 columns layout
     *
     * @param s1
     * @param s2
     * @param colWidth
     * @param g2
     */
    private void updateHeaderFont(String s1, String s2, int colWidth, Graphics2D g2) {
        int maxW = Math.max(stringWidth(headerFont, g2, s1), stringWidth(headerFont, g2, s2));
        //decrease font size
        while (maxW > (0.8 * colWidth)) {
            headerFontSize *= 0.9;
            headerFont = headerFont.deriveFont(headerFontSize);
            maxW = Math.max(stringWidth(headerFont, g2, s1), stringWidth(headerFont, g2, s2));
        }
        //increase font
        while (maxW < (0.5 * colWidth)) {
            headerFontSize *= 1.1;
            headerFont = headerFont.deriveFont(headerFontSize);
            maxW = Math.max(stringWidth(headerFont, g2, s1), stringWidth(headerFont, g2, s2));
        }
    }

    private void drawClustering(Graphics2D g, Clustering clust, int rectWidth, double xVal, double yVal, int mid) {
        String str = clust.getName();
        int width;
        int x, y;
        if (str == null) {
            str = "unknown |" + clust.size() + "|";
        }

        x = (int) xVal;
        y = (int) (mid - yVal);

        g.drawRect(x, mid, rectWidth, y);
        g.fillRect(x, mid, rectWidth, y);

        width = stringWidth(defaultFont, g, str);
        checkMax(width);
//        y = yOffset + (col * elemHeight + elemHeight / 2f + g.getFontMetrics().getDescent() / 2f);
//        g.drawString(str, x, y);
    }

    private void checkMax(int width) {
        if (width > maxWidth) {
            maxWidth = width;
            resetCache();
        }
    }

    @Override
    public void sizeUpdated(Dimension size) {
        if (hasData()) {
            int h = (size.height - insets.top - insets.bottom) / itemsCnt();
            if (h > 0) {
                elemHeight = h;
                fontSize = (int) (0.8 * elemHeight);
                strokeW = 0.05 * elemHeight;
                wideStroke = new BasicStroke((float) strokeW);
                defaultFont = defaultFont.deriveFont(Font.PLAIN, fontSize);
                minDist = 0;
                maxDist = itemsCnt();
                //for euclidean distance
                //minDist = size.width - 2 * maxWidth - insets.internal - insets.external - 20;
                //maxDist = distance(maxWidth, elemHeight / 2.0, elemHeight * itemsCnt(), size.width - maxWidth);
                midDist = (maxDist + minDist) / 2.0;
                //System.out.println("min = " + minDist);
                //System.out.println("mid = " + midDist);
                //System.out.println("max = " + maxDist);
            }
            //use maximum width avaiable
            realSize.width = size.width;
            maxWidth = 0;
        }
    }

    @Override
    public boolean hasData() {
        return clusterings != null;
    }

    @Override
    public void recalculate() {
        //int width = 40 + maxWidth;
        int height = headerHeight;
        //elemHeight = (realSize.height - insets.top - insets.bottom) / itemsCnt();
        //if (elemHeight > lineHeight) {
        height += elemHeight * clusterings.size();
        //}
        //realSize.width = width;
        //reqSize.width = width;
        realSize.height = height;
        //reqSize.height = height;
        //setMinimumSize(realSize);
        setPreferredSize(realSize);
        //setSize(realSize);

    }

    @Override
    public boolean isAntiAliasing() {
        return true;
    }

    /**
     * Computes sorting distance for all measures available (might take a while)
     *
     * @return
     */
    public Object2DoubleOpenHashMap<String> getResults() {
        //make sure everything is computed
        computeAll();
        return results;
    }

    public Dataset<? extends Instance> getDataset() {
        if (clusterings != null && clusterings.size() > 0) {
            Clustering c = clusterings.iterator().next();
            return c.getLookup().lookup(Dataset.class);
        }
        return null;
    }

    public Collection<? extends Clustering> getClusterings() {
        return clusterings;
    }

}
