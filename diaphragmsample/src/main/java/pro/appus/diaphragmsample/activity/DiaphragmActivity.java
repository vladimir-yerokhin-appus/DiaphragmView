package pro.appus.diaphragmsample.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;

import pro.appus.diaphragmsample.R;
import pro.appus.diaphragmsample.view.DiaphragmView;

/**
 * Created by vladimiryerokhin on 7/13/16
 */
public class DiaphragmActivity extends AppCompatActivity {

    private DiaphragmView diaphragm;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaphragm);

        initDiaphragmView();
        initButton();
        initSeekbar();
    }

    private void initDiaphragmView() {
        diaphragm = (DiaphragmView) findViewById(R.id.diaphragm);
        diaphragm.setDiaphragmPetalsCount(5);
    }

    private void initButton() {
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diaphragm.makeShot(500, 0.9f);
                diaphragm.makeShot(1000, 0.1f);
                diaphragm.makeShot(200, 0.5f);
            }
        });
    }

    private void initSeekbar() {
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                diaphragm.setOpeningValue(((float) progress) / 100f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        diaphragm.reset();
        seekBar.setProgress((int) (diaphragm.getOpeningValue() * 100));
    }
}
