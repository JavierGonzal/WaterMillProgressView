package com.thedeveloperworldisyours.watermillprogressview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.thedeveloperworldisyours.cabezas.WaterMillDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.activity_main_my_button);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDialog();
                }
            });
        }
    }

    public void openDialog() {
        WaterMillDialog waterMillDialog = new WaterMillDialog();
        waterMillDialog.show(getSupportFragmentManager(), "");
    }
}
