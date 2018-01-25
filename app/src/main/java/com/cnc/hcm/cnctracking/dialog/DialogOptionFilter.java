package com.cnc.hcm.cnctracking.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.activity.MainActivity;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.SettingApp;


/**
 * Created by giapmn on 12/13/17.
 */

public class DialogOptionFilter extends Dialog implements View.OnClickListener {

    private static final String TAGG = DialogOptionFilter.class.getSimpleName();

    private RadioButton rdOption1, rdOption2, rdOption3, rdOption4;
    private Button btnOK;

    private int option;
    private Context context;
    private MainActivity mainActivity;

    public DialogOptionFilter(@NonNull Context context) {
        super(context);
        this.context = context;
        mainActivity = (MainActivity) context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_option_filter);
        initViews();

    }


    private void initViews() {
        rdOption1 = (RadioButton) findViewById(R.id.rd_option_1);
        rdOption2 = (RadioButton) findViewById(R.id.rd_option_2);
        rdOption3 = (RadioButton) findViewById(R.id.rd_option_3);
        rdOption4 = (RadioButton) findViewById(R.id.rd_option_4);
        rdOption1.setOnClickListener(this);
        rdOption2.setOnClickListener(this);
        rdOption3.setOnClickListener(this);
        rdOption4.setOnClickListener(this);

        option = SettingApp.getInstance(context).getTypeFilterList();
        switch (option) {
            case Conts.TYPE_NEW_TASK:
                rdOption1.setChecked(true);
                break;
            case Conts.TYPE_DOING_TASK:
                rdOption2.setChecked(true);
                break;
            case Conts.TYPE_COMPLETE_TASK:
                rdOption3.setChecked(true);
                break;
            case Conts.TYPE_ALL_TASK:
                rdOption4.setChecked(true);

                break;
        }

        btnOK = (Button) findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingApp.getInstance(context).setTypeFilterList(option);
                if (mainActivity != null) {
                    mainActivity.filerTask(option);
                }
                dismiss();
            }
        });


    }


    @Override
    public void onClick(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()) {
            case R.id.rd_option_1:
                if (checked) {
                    option = Conts.TYPE_NEW_TASK;
                }
                break;
            case R.id.rd_option_2:
                if (checked) {
                    option = Conts.TYPE_DOING_TASK;
                }
                break;
            case R.id.rd_option_3:
                if (checked) {
                    option = Conts.TYPE_COMPLETE_TASK;
                }
                break;
            case R.id.rd_option_4:
                if (checked) {
                    option = Conts.TYPE_ALL_TASK;
                }
                break;
        }
    }

}
