package siltarae.mytestapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;


public class DrugInfoActivity extends Activity {

    boolean successLogin = false;
    int color;
    int shape;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_info);

    }

    public void imageIconClicked(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        intent.putExtra("success",successLogin);
        finish();
    }

    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView mediName = (TextView)findViewById(R.id.medicineName);
        TextView mediColor = (TextView)findViewById(R.id.medicineColor);
        TextView mediShape = (TextView)findViewById(R.id.medicineShape);
        TextView mediDescript = (TextView)findViewById(R.id.medicineDescription);
        ImageView mediImage = (ImageView)findViewById(R.id.imageIcon);

        File storageDir = Environment.getExternalStorageDirectory();
        File outputfile = new File(storageDir, "medicine.jpg");
        Bitmap bitmap = BitmapFactory.decodeFile(outputfile.getAbsolutePath());
        mediImage.setImageBitmap(bitmap);
        Intent intent = getIntent();
        color = intent.getIntExtra("color", color);
        shape = intent.getIntExtra("shape", shape);
        if(color == 1 && shape == 1){
            mediName.setText("씨콜드정(주간)");
            mediColor.setText("노랑");
            mediShape.setText("원형");
            mediDescript.setText("<주간용> 감기의 여러증상\n(콧물, 코막힘, 재채기, 인후통, 기침,\n가래, 오한, 발열, 두통,\n관절통, 근육통)의 완화");
        }
        else if(color == 2 && shape == 1){
            mediName.setText("씨콜드정(야간)");
            mediColor.setText("파랑");
            mediShape.setText("타원형");
            mediDescript.setText("<야간용> 감기의 여러증상\n(콧물, 코막힘, 재채기, 인후통, 기침,\n가래, 오한, 발열, 두통,\n관절통, 근육통)의 완화");
        }
        else if(color == 3 && shape == 1){
            mediName.setText("리보테인정");
            mediColor.setText("분홍");
            mediShape.setText("원형");
            mediDescript.setText("육체피로, 임신/수유기, 병중/병후\n체력저하 시 비타민 B2, B6 보급. \n구각염, 구순염, 구내염,\n설염, 습진, 피부염 완화");
        }
        else
        {
            mediName.setText("정보 없음");
            mediColor.setText("정보 없음");
            mediShape.setText("정보 없음");
            mediDescript.setText("정보 없음");
        }
/*        successLogin = intent.getBooleanExtra("success", false);
        if(successLogin == true){
 //           loginBtn.setText("로그아웃");
  //          loginText.setText("정보 저장을 원치 않으시면 로그아웃해주세요.");
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_drug_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
