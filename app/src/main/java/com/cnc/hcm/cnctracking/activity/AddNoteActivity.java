package com.cnc.hcm.cnctracking.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.util.Conts;

/**
 * Created by Android on 06/03/2018.
 */

public class AddNoteActivity extends AppCompatActivity {
    private EditText edtInputNote;
    private String note;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Button btnSave = findViewById(R.id.btn_save);
        Button btnCancel = findViewById(R.id.btn_cancel);
        edtInputNote = findViewById(R.id.edt_input_note);
        note = getIntent().getStringExtra(Conts.KEY_CURRENT_NOTE);
        edtInputNote.setText(note);
        edtInputNote.requestFocus();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                note = edtInputNote.getText().toString();
                intent.putExtra(Conts.KEY_RESULT_ADD_NOTE, note);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
//        super.onBackPressed();
    }
}
