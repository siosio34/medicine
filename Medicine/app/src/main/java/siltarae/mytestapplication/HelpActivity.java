package siltarae.mytestapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Administrator on 2015-11-15.
 */
public class HelpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_1);

       //Button button = (Button)findViewById(R.id.backBtn);

       //button.setOnClickListener(new View.OnClickListener() {
       //    @Override
       //    public void onClick(View v) {
       //        Toast.makeText(getApplicationContext(), "돌아갑니다", Toast.LENGTH_LONG).show();
       //        finish();
       //    }
       //});

    }

    public void onHelpBackHomeClicked(View view) {
        Toast.makeText(getApplicationContext(), "처음으로 돌아갑니다", Toast.LENGTH_LONG).show();
        finish();
    }

    public void onHelpBackClicked(View view) {
        setContentView(R.layout.activity_help_1);
    }

    public void onHelp1BtnClicked(View view) {
        setContentView(R.layout.activity_help_2);
    }

    public void onHelp2BtnClicked(View view) {
        setContentView(R.layout.activity_help_3);
    }

    public void onHelp3BtnClicked(View view) {
        setContentView(R.layout.activity_help_4);
    }
}
