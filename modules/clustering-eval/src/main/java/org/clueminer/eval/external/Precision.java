package org.clueminer.eval.external;

import org.clueminer.eval.utils.CountingPairs;
import org.clueminer.clustering.api.ExternalEvaluator;
import com.google.common.collect.BiMap;
import com.google.common.collect.Table;
import java.util.Map;
import org.clueminer.clustering.api.Cluster;
import org.clueminer.clustering.api.Clustering;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.math.Matrix;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Tomas Barton
 */
@ServiceProvider(service = ExternalEvaluator.class)
public class Precision extends AbstractExternalEval {

    private static final long serialVersionUID = -1547620533572167034L;
    private static final String name = "Precision";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double score(Clustering clusters, Dataset dataset) {
        Table<String, String, Integer> table = CountingPairs.contingencyTable(clusters);
        return countScore(table, clusters);
    }

    @Override
    public double score(Clustering<Cluster> c1, Clustering<Cluster> c2) {
        Table<String, String, Integer> table = CountingPairs.contingencyTable(c1, c2);
        return countScore(table, c1);
    }

    @Override
    public double score(Clustering clusters, Dataset dataset, Matrix proximity) {
        return score(clusters, dataset);
    }

    public double countScore(Table<String, String, Integer> table, Clustering<Cluster> ref) {
        BiMap<String, String> matching = CountingPairs.findMatching(table);
        Map<String, Integer> res;
        int tp, fp;
        double index = 0.0;
        double precision;
        //for each cluster we have score of quality
        Cluster c;
        for (String cluster : matching.values()) {
            c = ref.get(cluster);
            //we intentionally ignore computing precision in clusters with single instance
            if (c.size() > 1) {
                res = CountingPairs.countAssignments(table, matching.inverse().get(cluster), cluster);
                //System.out.println("class: " + matching.inverse().get(cluster) + " cluster = " + cluster);
                tp = res.get("tp");
                fp = res.get("fp");
                //System.out.println("sum = " + (tp + fp + res.get("fn") + res.get("tn")));
                precision = tp / (double) (tp + fp);
                //System.out.println("precision = " +precision);
                index += precision;
            }
        }

        //average value
        return index / table.columnKeySet().size();
    }

    /**
     * Bigger is better
     *
     * @return
     */
    @Override
    public boolean isMaximized() {
        return true;
    }

}
