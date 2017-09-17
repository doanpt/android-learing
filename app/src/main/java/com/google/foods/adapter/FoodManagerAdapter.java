package com.google.foods.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.foods.R;
import com.google.foods.customeview.DialogNotification;
import com.google.foods.dialog.DialogEditFood;
import com.google.foods.fragment.FoodManagerFragment;
import com.google.foods.models.ItemFood;
import com.google.foods.utils.CommonMethod;
import com.google.foods.utils.CommonValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Hoang on 08/14/2017.
 */

public class FoodManagerAdapter extends RecyclerView.Adapter<FoodManagerAdapter.ViewHolder> {

    private static final String TAGG = "FoodManagerAdapter";
    private ArrayList<ItemFood> arrItemFood = new ArrayList<>();
    private ArrayList<ItemFood> arrFilter = new ArrayList<>();
    private ArrayList<ItemFood> arrFilterOutOfStock = new ArrayList<>();
    private Context context;
    private boolean isFoodOutOfStock;
    private OnClickMoreOptionListener onClickMoreOptionListener;
    private DialogNotification confirmRemoveDialog;
    private DialogEditFood dialogEditFood;
    private DatabaseReference mData;
    private FirebaseStorage firebaseStorage;

    private FoodManagerFragment foodManagerFragment;

    public FoodManagerAdapter(Context context) {
        initData();
        this.context = context;
        isFoodOutOfStock = false;

        confirmRemoveDialog = new DialogNotification(context);
        confirmRemoveDialog.setTextBtnExit(context.getResources().getString(R.string.text_exit_upercase));
        confirmRemoveDialog.setTextBtnOK(context.getResources().getString(R.string.text_delete_upercase));

        dialogEditFood = new DialogEditFood(context);
        dialogEditFood.setFoodManagerAdapter(this);
    }

    private void initData() {
        mData = FirebaseDatabase.getInstance().getReference();
        mData.child(CommonValue.DATABASE_TABLE_FOOD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrItemFood.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ItemFood itemFood = postSnapshot.getValue(ItemFood.class);
                    arrItemFood.add(itemFood);
                    notifyDataSetChanged();
                }
                setArrFilter(arrItemFood);
                if (foodManagerFragment != null && arrItemFood.size() > CommonValue.DEFAULT_VALUE_INT_0) {
                    foodManagerFragment.hideLayoutListEmpty();
                    foodManagerFragment.hideDumyLoadingData();
                    filterByFoodType(foodManagerFragment.getFoodType());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        firebaseStorage = FirebaseStorage.getInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_mgr, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemFood item = arrItemFood.get(position);
        holder.tvSTT.setText((position + CommonValue.DEFAULT_VALUE_INT_1) + CommonValue.BLANK);
        holder.tvFoodName.setText(item.getName() + CommonValue.BLANK);
        holder.tvFoodConLai.setText((item.getTotalQuantity() - item.getOrderQuantity()) + CommonValue.BLANK);
        holder.tvFoodType.setText(CommonMethod.convertMoneyToVND(item.getType()));

        int remainingFood = item.getTotalQuantity() - item.getOrderQuantity();
        if (remainingFood == CommonValue.DEFAULT_VALUE_INT_0) {
            holder.tvFoodConLai.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
        } else {
            holder.tvFoodConLai.setTextColor(context.getResources().getColor(R.color.color_item_food));
        }
    }

    @Override
    public int getItemCount() {
        return arrItemFood.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvSTT;
        private TextView tvFoodName;
        private TextView tvFoodType;
        private TextView tvFoodConLai;
        private ImageView imvOption;

        public ViewHolder(View view) {
            super(view);

            tvSTT = (TextView) view.findViewById(R.id.itemfood_tv_title_stt);
            tvFoodName = (TextView) view.findViewById(R.id.itemfood_tv_title_food_name);
            tvFoodType = (TextView) view.findViewById(R.id.itemfood_tv_title_type_food);
            tvFoodConLai = (TextView) view.findViewById(R.id.itemfood_tv_title_con_lai);
            imvOption = (ImageView) view.findViewById(R.id.itemfood_imv_option);
            imvOption.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onClickMoreOptionListener != null) {
                onClickMoreOptionListener.onClickMoreOption(v, getPosition());
            }
        }
    }

    public interface OnClickMoreOptionListener {
        void onClickMoreOption(View view, int position);
    }

    public void filter(int foodType) {
        if (foodType == CommonValue.TYPE_FOOD_ALL) {
            arrFilterOutOfStock.clear();
            arrFilterOutOfStock.addAll(arrFilter);
            arrItemFood.clear();
            arrItemFood.addAll(arrFilter);
        } else {
            int size = arrFilter.size();
            arrItemFood.clear();
            arrFilterOutOfStock.clear();
            for (int index = 0; index < size; index++) {
                ItemFood item = arrFilter.get(index);
                if (item.getType() == foodType) {
                    arrItemFood.add(item);
                }
            }
            arrFilterOutOfStock.addAll(arrItemFood);
        }
    }

    public void filterByFoodType(int foodType) {
        if (isFoodOutOfStock) {
            filter(foodType);
            filterFoodOutOfStock(isFoodOutOfStock);
        } else {
            filter(foodType);
        }
        notifyDataSetChanged();
    }

    public void filterFoodOutOfStock(boolean isOutOfStock) {
        arrItemFood.clear();
        if (isOutOfStock) {
            for (int index = 0; index < arrFilterOutOfStock.size(); index++) {
                ItemFood item = arrFilterOutOfStock.get(index);
                int remainingFood = item.getTotalQuantity() - item.getOrderQuantity();
                if (remainingFood != CommonValue.DEFAULT_VALUE_INT_0) {
                    arrItemFood.add(item);
                }
            }
        } else {
            arrItemFood.addAll(arrFilterOutOfStock);
        }
        notifyDataSetChanged();
    }

    public void setOnClickMoreOptionListener(OnClickMoreOptionListener onClickMoreOptionListener) {
        this.onClickMoreOptionListener = onClickMoreOptionListener;
    }

    public void showMenuPopup(View v, final int possition) {
        PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu.getMenuInflater().inflate(R.menu.meu_option_food, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_option:
                        dialogEditFood.setInforFoodNeedEdit(arrItemFood.get(possition));
                        dialogEditFood.show();
                        break;
                    case R.id.menu_delete:
                        String content = String.format(context.getResources().getString(R.string.confirm_remove_item), arrItemFood.get(possition).getName());
                        confirmRemoveDialog.setContentMessage(content);
                        confirmRemoveDialog.setOnClickButtonExitDialogListener(new DialogNotification.OnClickButtonExitDialogListener() {
                            @Override
                            public void onClickButtonExit() {
                                confirmRemoveDialog.dismiss();
                            }
                        });
                        confirmRemoveDialog.setOnClickButtonOKDialogListener(new DialogNotification.OnClickButtonOKDialogListener() {
                            @Override
                            public void onClickButtonOK() {
                                removeItemFood(possition);
                                confirmRemoveDialog.dismiss();
                            }
                        });
                        confirmRemoveDialog.show();
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    public void removeItemFood(final int position) {
        final String foodname = arrItemFood.get(position).getName();
        final String urlImage = arrItemFood.get(position).getImage();
        mData.child(CommonValue.DATABASE_TABLE_FOOD).child(arrItemFood.get(position).getIdFood() + CommonValue.BLANK).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    notifyItemRemoved(position);
                    StorageReference photoRef = firebaseStorage.getReferenceFromUrl(urlImage);
                    photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, context.getResources().getString(R.string.deleted) + " " + foodname, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, context.getResources().getString(R.string.fail_please_try_again), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.fail_please_try_again), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateItemFood(final ItemFood itemFood) {
        Map<String, Object> mapUpdate = new HashMap<>();
        mapUpdate.put(CommonValue.DATABASE_TABLE_FOOD_FIELD_NAME, itemFood.getName());
        mapUpdate.put(CommonValue.DATABASE_TABLE_FOOD_FIELD_TYPE, itemFood.getType());
        mapUpdate.put(CommonValue.DATABASE_TABLE_FOOD_FIELD_TOTAL_QUANTITY, itemFood.getTotalQuantity());

        mData.child(CommonValue.DATABASE_TABLE_FOOD).child(itemFood.getIdFood() + CommonValue.BLANK).updateChildren(mapUpdate, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Toast.makeText(context, context.getResources().getString(R.string.update_successfull), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.fail_please_try_again), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setFoodOutOfStock(boolean foodOutOfStock) {
        isFoodOutOfStock = foodOutOfStock;
    }

    public void setArrItemFood(ArrayList<ItemFood> arrItemFood) {
        this.arrItemFood = arrItemFood;
    }

    public void setArrFilter(ArrayList<ItemFood> arrFilter) {
        this.arrFilter.clear();
        this.arrFilterOutOfStock.clear();
        this.arrFilter.addAll(arrFilter);
        this.arrFilterOutOfStock.addAll(arrFilter);
    }

    public void setFoodManagerFragment(FoodManagerFragment foodManagerFragment) {
        this.foodManagerFragment = foodManagerFragment;
    }
}
