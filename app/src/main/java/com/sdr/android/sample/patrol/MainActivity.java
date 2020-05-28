package com.sdr.android.sample.patrol;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.sdr.patrollib.PatrolUser;
import com.sdr.patrollib.SDR_PATROL_BIAOHUA;
import com.sdr.patrollib.support.PatrolFunctions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickStart(View view) {
        String token = ((EditText) findViewById(R.id.edittext)).getText().toString();
        if (TextUtils.isEmpty(token)) {
            return;
        }
        List<String> exceptList = new ArrayList<>();
        exceptList.add(PatrolFunctions.设备巡检);
        SDR_PATROL_BIAOHUA.start(this, new PatrolUser("41", "管理员", "13011111111", token, exceptList));
    }
}
