package com.dvt.qlcl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends Activity {
    public static final String RESULT_CODE_STUDENT = "code_student";
    public static final String KEY_SHARE="com.dvt.qlcl";
    private EditText edtCode;
    private Button btnView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        edtCode= (EditText) findViewById(R.id.edt_code_student);
        btnView= (Button) findViewById(R.id.btn_view);
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code=edtCode.getText() + "";
                startResult(code);
            }
        });
    }

    private void startResult(String code) {
        Intent intentResult=new Intent(MainActivity.this,MainResult.class);
        //intentResult.putExtra(RESULT_CODE_STUDENT,code);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(RESULT_CODE_STUDENT,code);
        editor.apply();
        startActivity(intentResult);
    }
}
