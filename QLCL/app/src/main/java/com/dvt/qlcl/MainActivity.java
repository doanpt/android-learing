package com.dvt.qlcl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dvt.util.CommonMethod;

public class MainActivity extends AppCompatActivity {
    private EditText edtCode;
    private Button btnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        edtCode = (EditText) findViewById(R.id.edt_code_student);
        btnView = (Button) findViewById(R.id.btn_view);
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = edtCode.getText() + "";
                if (CommonMethod.getInstance().isValid(code))
                    startResult(code);
                else
                    Toast.makeText(MainActivity.this, "Mã sinh viên có 10 chữ số", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startResult(String code) {
        Intent intentResult = new Intent(MainActivity.this, MainResult.class);
        CommonMethod.setCode(MainActivity.this, code);
        startActivity(intentResult);
    }
}
