package org.clueminer.clustering.benchmark;

import org.clueminer.clustering.aggl.HAC;
import org.clueminer.clustering.api.AgglomerativeClustering;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.math.Matrix;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author deric
 */
public class ExperimentTest {

    private Experiment subject;

    public ExperimentTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testRun() {
    }

    @Test
    public void testGenerateData() {
        BenchParams params = new BenchParams();
        params.n = 15;
        subject = new Experiment(params, null, new AgglomerativeClustering[]{new HAC()});
        Dataset<? extends Instance> data = subject.generateData(params.n, params.dimension);
        assertEquals(params.n, data.size());
        assertEquals(params.dimension, data.attributeCount());
        Matrix m = data.asMatrix();
        assertEquals(params.n, m.rowsCount());
        assertEquals(params.dimension, m.columnsCount());
        m.print(5, 2);
    }

}
