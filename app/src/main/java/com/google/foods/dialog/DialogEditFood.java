package com.google.foods.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.foods.R;
import com.google.foods.adapter.FoodManagerAdapter;
import com.google.foods.fragment.FoodManagerFragment;
import com.google.foods.models.ItemFood;
import com.google.foods.utils.CommonValue;
import com.squareup.picasso.Picasso;

/**
 * Created by Hoang on 08/19/2017.
 */

public class DialogEditFood extends Dialog implements View.OnClickListener {

    private ImageView imvFoodImage;
    private EditText edtFoodName;
    private EditText edtFoodTotalQuantity;
    private Spinner spinnerFoodType;

    private Button btnCancel, btnOK;

    private Context context;
    private FoodManagerAdapter foodManagerAdapter;
    private ItemFood itemFood;

    public DialogEditFood(@NonNull Context context) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_edit_food);
        initView();
    }

    private void initView() {
        imvFoodImage = (ImageView) findViewById(R.id.imv_edit_food_image);
        edtFoodName = (EditText) findViewById(R.id.edt_edit_food_name);
        edtFoodTotalQuantity = (EditText) findViewById(R.id.edt_edit_food_total_quantity);

        spinnerFoodType = (Spinner) findViewById(R.id.spinner_edit_food_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.price_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFoodType.setAdapter(adapter);

        btnCancel = (Button) findViewById(R.id.btn_exit_dialog);
        btnCancel.setOnClickListener(this);
        btnOK = (Button) findViewById(R.id.btn_ok_dialog);
        btnOK.setOnClickListener(this);
    }

    public void setInforFoodNeedEdit(ItemFood itemFood) {
        this.itemFood = itemFood;
        Picasso.with(context).load(itemFood.getImage()).placeholder(R.drawable.ic_loading).error(R.drawable.ic_load_fail).into(imvFoodImage);
        edtFoodName.setText(itemFood.getName());
        edtFoodTotalQuantity.setText(itemFood.getTotalQuantity() + "");
        int position = 0;
        switch (itemFood.getType()) {
            case CommonValue.VALUE_FOOD_5_VND:
                position = 3;
                break;
            case CommonValue.VALUE_FOOD_10_VND:
                position = 4;
                break;
            case CommonValue.VALUE_FOOD_15_VND:
                position = 5;
                break;
            case CommonValue.VALUE_FOOD_35_VND:
                position = 1;
                break;
            case CommonValue.VALUE_FOOD_40_VND:
                position = 2;
                break;
        }
        spinnerFoodType.setSelection(position);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit_dialog:
                dismiss();
                break;
            case R.id.btn_ok_dialog:
                itemFood.setName(edtFoodName.getText().toString());
                itemFood.setTotalQuantity(Integer.parseInt(edtFoodTotalQuantity.getText().toString()));
                int foodType = CommonValue.VALUE_FOOD_30_VND;
                switch (spinnerFoodType.getSelectedItemPosition()) {
                    case CommonValue.DEFAULT_VALUE_INT_1:
                        foodType = CommonValue.VALUE_FOOD_35_VND;
                        break;
                    case CommonValue.DEFAULT_VALUE_INT_2:
                        foodType = CommonValue.VALUE_FOOD_40_VND;
                        break;
                    case CommonValue.DEFAULT_VALUE_INT_3:
                        foodType = CommonValue.VALUE_FOOD_5_VND;
                        break;
                    case CommonValue.DEFAULT_VALUE_INT_4:
                        foodType = CommonValue.VALUE_FOOD_10_VND;
                        break;
                    case CommonValue.DEFAULT_VALUE_INT_5:
                        foodType = CommonValue.VALUE_FOOD_15_VND;
                        break;
                }
                itemFood.setType(foodType);
                foodManagerAdapter.updateItemFood(itemFood);

                dismiss();
                break;
        }
    }

    public void setFoodManagerAdapter(FoodManagerAdapter foodManagerAdapter) {
        this.foodManagerAdapter = foodManagerAdapter;
    }
}
