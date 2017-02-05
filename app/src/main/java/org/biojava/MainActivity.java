package org.biojava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.biojava.*;
import org.biojava.demo.DemoSixFrameTranslation;
import org.biojava.nbio.core.sequence.DNASequence;

import sun.misc.CompoundEnumeration;

public class MainActivity extends AppCompatActivity {

    CompoundEnumeration nr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        DemoSixFrameTranslation demo = new DemoSixFrameTranslation();
//        demo.test();

//        try {
//            DNASequence.test();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }
}
