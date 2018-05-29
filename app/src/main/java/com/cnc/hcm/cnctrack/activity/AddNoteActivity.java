package com.cnc.hcm.cnctrack.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.cnc.hcm.cnctrack.R;
import com.cnc.hcm.cnctrack.base.BaseActivity;
import com.cnc.hcm.cnctrack.util.Conts;

/**
 * Created by Android on 06/03/2018.
 */

public class AddNoteActivity extends BaseActivity {
    private EditText edtInputNote;
    private String note;

    @Override
    public void onViewReady(@Nullable Bundle savedInstanceState) {
        ImageView imvSave = findViewById(R.id.img_add_note);
        ImageView imvCancel = findViewById(R.id.img_back);
        edtInputNote = findViewById(R.id.edt_input_note);
        note = getIntent().getStringExtra(Conts.KEY_CURRENT_NOTE);
        edtInputNote.setText(note);
        edtInputNote.requestFocus();
        imvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        imvSave.setOnClickListener(new View.OnClickListener() {
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
    public int getLayoutId() {
        return R.layout.activity_add_note;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
//        super.onBackPressed();
    }
}
