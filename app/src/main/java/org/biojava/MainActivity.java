package org.biojava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.biojava.*;
import org.biojava.demo.DemoSixFrameTranslation;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DemoSixFrameTranslation demo = new DemoSixFrameTranslation();
        demo.test();

    }
}
