package com.google.foods.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.foods.R;
import com.google.foods.dialog.DialogNetworkConnection;
import com.google.foods.models.ItemFood;
import com.google.foods.utils.CommonValue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddFoodActivity extends AppCompatActivity implements View.OnClickListener {
    /*Key to listen onActivityResult after choose image*/
    private static final int REQUEST_IMAGE = 101;
    private static final String TAG = AddFoodActivity.class.getSimpleName();
    private Spinner mSpnPrice;
    private Button btnAddNewFood;
    private ImageView mImgFood, mImageDefault;
    private EditText edtFoodName;
    private EditText edtFoodTotalQuantity;
    private FirebaseStorage storage;
    private DatabaseReference reference;
    private StorageReference mountainsRef;
    private Bitmap bitmap;

    private DialogNetworkConnection dialogNetworkConnection;
    private boolean isNetworkConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBroadcastReciver();
        setContentView(R.layout.activity_add_food_content);
        /*get instance of Store database on firebase*/
        storage = FirebaseStorage.getInstance("gs://foodapp-e95f3.appspot.com");
        /*Get root node*/
        reference = FirebaseDatabase.getInstance().getReference();
        initView();
    }

    private void registerBroadcastReciver() {
        dialogNetworkConnection = new DialogNetworkConnection(this);
        dialogNetworkConnection.setCancelable(false);

        IntentFilter filter = new IntentFilter();
        filter.addAction(CommonValue.ACTION_NETWORK_CHANGE);
        registerReceiver(receiver, filter);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case CommonValue.ACTION_NETWORK_CHANGE:
                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetInfor = connectivityManager.getActiveNetworkInfo();
                    isNetworkConnected = activeNetInfor != null && activeNetInfor.isConnectedOrConnecting();
                    if (isNetworkConnected) {
                        dialogNetworkConnection.dismiss();
                    } else {
                        dialogNetworkConnection.show();
                    }
                    break;
            }

        }
    };

    private void initView() {
        edtFoodName = (EditText) findViewById(R.id.add_food_edt_food_name);
        edtFoodTotalQuantity = (EditText) findViewById(R.id.add_food_edt_total_quantity);
        btnAddNewFood = (Button) findViewById(R.id.btn_add_new_food);
        mImageDefault = (ImageView) findViewById(R.id.imageView2);
        btnAddNewFood.setOnClickListener(this);
        mImgFood = (ImageView) findViewById(R.id.img_choose_image_from_library);
        mImgFood.setOnClickListener(this);
        mSpnPrice = (Spinner) findViewById(R.id.spn_price);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.price_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnPrice.setAdapter(adapter);
    }

    /*
    * This method to get reuslt after user choose image.
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    final Uri uri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        mImgFood.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        bitmap = null;
                        Log.e(TAG, "IOException when load bitmap", e);
                    }
                }
            }
        }
    }

    /*
    * Create new itemfood and set value to push to firebase
    * */
    private ItemFood getNewItem() {
        ItemFood itemFood = new ItemFood();
        itemFood.setName(edtFoodName.getText() + "");
        itemFood.setTotalQuantity(Integer.parseInt(edtFoodTotalQuantity.getText() + ""));
        itemFood.setOrderQuantity(0);
        final String arrSplit[] = mSpnPrice.getSelectedItem().toString().split(" ");
        int price = Integer.parseInt(arrSplit[1]);
        itemFood.setType(price);
        return itemFood;
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            //handler when user press choose image button and start activity with action :ACTION_OPEN_DOCUMENT
            case R.id.img_choose_image_from_library:
                final Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
                break;
            //handler when user click add food button
            case R.id.btn_add_new_food:
                if (edtFoodName.getText().toString().trim().equals(CommonValue.BLANK)) {
                    Snackbar.make(v, getResources().getString(R.string.not_input_food_name), Snackbar.LENGTH_LONG).show();
                } else if (edtFoodTotalQuantity.getText().toString().trim().equals(CommonValue.BLANK)) {
                    Snackbar.make(v, getResources().getString(R.string.not_input_total_quantity), Snackbar.LENGTH_LONG).show();
                } else {
                    if (bitmap == null) {
                        Snackbar.make(getCurrentFocus(), getResources().getString(R.string.need_choose_image_food_again), Snackbar.LENGTH_LONG).show();
                        mImgFood.setImageResource(android.R.color.transparent);
                        return;
                    } else {
                        //create and show progress dialog
                        final ProgressDialog progressDialog = new ProgressDialog(AddFoodActivity.this,
                                R.style.AppTheme_Dark_Dialog);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage(getResources().getString(R.string.uploading_food));
                        progressDialog.show();
                        progressDialog.setCanceledOnTouchOutside(false);
                        //create new item to push to firebase
                        final ItemFood item = getNewItem();
                        //get byte from picture which user choose
                        byte[] data = PrepareUpload();
                        //push to firebase
                        UploadTask uploadTask = mountainsRef.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Snackbar.make(v, getResources().getString(R.string.fail_please_try_again), Snackbar.LENGTH_LONG).show();
                                bitmap = null;
                                mImgFood.setImageResource(android.R.color.transparent);
                                progressDialog.dismiss();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                //upload success => set image for itemfood and push item to firebase
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                item.setImage(String.valueOf(downloadUrl));
                                DatabaseReference itemRef = reference.child(CommonValue.DATABASE_TABLE_FOOD).push();
                                item.setIdFood(itemRef.getKey());
                                itemRef.setValue(item, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if (databaseError == null) {
                                            Snackbar.make(v, getResources().getString(R.string.add_food_successfull), Snackbar.LENGTH_LONG).show();
                                        } else {
                                            Snackbar.make(v, getResources().getString(R.string.fail_please_try_again), Snackbar.LENGTH_LONG).show();
                                        }
                                        bitmap = null;
                                        mImgFood.setImageResource(android.R.color.transparent);
                                        edtFoodName.setText(CommonValue.BLANK);
                                        edtFoodTotalQuantity.setText(CommonValue.BLANK);
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        });
                    }
                }
                break;
        }
    }

    //get byte from picture which user choose
    private byte[] PrepareUpload() {
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();
        // Create a reference to "mountains.jpg"
        mountainsRef = storageRef.child("IMG_" + edtFoodName.getText() + ".png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        return data;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}