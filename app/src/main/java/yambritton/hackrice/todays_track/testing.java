package yambritton.hackrice.todays_track;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class testing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final Button videobutton = (Button) findViewById(R.id.videobutton);

        videobutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //  select video

            }
        });

        final Button audiobutton = (Button) findViewById(R.id.audiobutton);

        audiobutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        final Button outputbutton = (Button) findViewById(R.id.outputbutton);

        outputbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // generate output
            }
        });
    }

}
