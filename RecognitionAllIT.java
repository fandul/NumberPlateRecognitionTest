package net.sf.javaanpr.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import net.sf.javaanpr.imageanalysis.CarSnapshot;
import net.sf.javaanpr.intelligence.Intelligence;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author Rihards
 */
@RunWith(Parameterized.class)
public class RecognitionAllIT {

    private File snap;
    private String plateExpected;

    //constructor with one row of test data
    public RecognitionAllIT(File snap, String plateExpected) {
        this.snap = snap;
        this.plateExpected = plateExpected;
    }

    @Parameters
    public static Collection<Object[]> picturesToTest() throws FileNotFoundException, IOException {
        String snapshotDirPath = "src/test/resources/snapshots";
        String resultsPath = "src/test/resources/results.properties";
        InputStream resultsStream = new FileInputStream(new File(resultsPath));

        Properties properties = new Properties();
        properties.load(resultsStream);
        resultsStream.close();

        File snapshotDir = new File(snapshotDirPath);
        File[] snapshots = snapshotDir.listFiles();

        Collection<Object[]> dataForOneImage = new ArrayList();
        for (File snap : snapshots) {
            String name = snap.getName();
            String plateExpected = properties.getProperty(name);
            dataForOneImage.add(new Object[]{snap, plateExpected});
        }
        return dataForOneImage;

    }
    
    @Test
    public void testAllSnapshots () throws Exception {
        Intelligence intel = new Intelligence();
        CarSnapshot carSnap = new CarSnapshot(new FileInputStream(snap));
        String recognized = intel.recognize(carSnap);
        //assertEquals(plateExpected, intel.recognize(carSnap));
        assertThat(carSnap, notNullValue());
        assertThat(recognized, notNullValue());
        assertThat(plateExpected, is(recognized)); // with hamcrest
   }
}
