package org.biojava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.biojava.*;
import org.biojava.demo.DemoSixFrameTranslation;
import org.biojava.nbio.core.search.io.ResultFactory;
import org.biojava.nbio.core.search.io.SearchIO;
import org.biojava.nbio.core.search.io.blast.BlastXMLParser;
import org.biojava.nbio.core.sequence.DNASequence;

import java.io.File;
import java.net.URL;

import sun.misc.CompoundEnumeration;

public class MainActivity extends AppCompatActivity {

    CompoundEnumeration nr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DemoSixFrameTranslation demo = new DemoSixFrameTranslation();
        demo.test();

//        try {
//            DNASequence.test();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

////		String resource = "/org/biojava/nbio/core/search/io/blast/testBlastReport.blastxml"; //sena direktorija
//        String resource = "/core/search/io/blast/testBlastReport.blastxml"; //nauja direktorija
////		String resource = "/testBlastReport.blastxml";
//        URL resourceURL = getClass().getResource(resource);
//        File file = new File(resourceURL.getFile());
//
//        ResultFactory blastResultFactory = new BlastXMLParser();
//        final SearchIO instance;
//        try {
//            instance = new SearchIO(file, blastResultFactory);
//        } catch (Exception e) {
//            System.out.println("NEsuveike!!!!!!!!!!!!!");
//        }
    }


}
