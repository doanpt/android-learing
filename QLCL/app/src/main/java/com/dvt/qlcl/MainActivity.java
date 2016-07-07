package com.dvt.qlcl;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dvt.jsoup.HtmlParse;
import com.dvt.util.CommonMethod;

public class MainActivity extends AppCompatActivity {
    private EditText edtCode;
    private Button btnView;
    private ImageView ivIcon;
    private Animation animation;
    private HtmlParse htmlParse;
    private ProgressBar progressBar;
    private LinearLayout llLoad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        htmlParse = new HtmlParse();
        String shareCode = CommonMethod.getCode(MainActivity.this);
        if ("".equals(shareCode)) {
            initView();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    edtCode.setVisibility(View.VISIBLE);
                    btnView.setVisibility(View.VISIBLE);
                }
            }, 3000);
        } else {
            startResult(shareCode);
        }
    }

    private void initView() {
        ivIcon = (ImageView) findViewById(R.id.image_icon);
        edtCode = (EditText) findViewById(R.id.edt_code_student);
        btnView = (Button) findViewById(R.id.btn_view);
        llLoad= (LinearLayout) findViewById(R.id.ll_load);
        progressBar= (ProgressBar) findViewById(R.id.ProgressBar);
        animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anim_image);
        ivIcon.startAnimation(animation);
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = edtCode.getText() + "";
                if (CommonMethod.getInstance().isValid(code)) {
                    try {
                        new LoadDataTask().execute(code);
                    }catch (Exception e){
                        Toast.makeText(MainActivity.this,"Load dữ liệu lỗi. Làm ơn thử lại!",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Mã sinh viên có 10 chữ số", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void startResult(String code) {
        Intent intentResult = new Intent(MainActivity.this, MainResult.class);
        CommonMethod.setCode(MainActivity.this, code);
        startActivity(intentResult);
    }
    class LoadDataTask extends AsyncTask<String,Void,Void>{
        String code="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            llLoad.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                code=params[0];
                htmlParse.getAllDataForFirstStart(MainActivity.this,code);
            }catch (Exception e){
                Toast.makeText(MainActivity.this,"Load dữ liệu lỗi. Làm ơn thử lại",Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            startResult(code);
        }
    }
}
