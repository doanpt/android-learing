package com.cnc.hcm.cnctracking.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnc.hcm.cnctracking.R;
import com.cnc.hcm.cnctracking.adapter.WorkDetailServiceRecyclerViewAdapter;
import com.cnc.hcm.cnctracking.api.ApiUtils;
import com.cnc.hcm.cnctracking.api.MHead;
import com.cnc.hcm.cnctracking.dialog.DialogDetailTaskFragment;
import com.cnc.hcm.cnctracking.model.ConfirmChargeResponse;
import com.cnc.hcm.cnctracking.model.GetTaskDetailResult;
import com.cnc.hcm.cnctracking.model.ItemPrice;
import com.cnc.hcm.cnctracking.util.CommonMethod;
import com.cnc.hcm.cnctracking.util.Conts;
import com.cnc.hcm.cnctracking.util.UserInfo;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkDetailServiceFragment extends Fragment implements
        View.OnClickListener, DialogDetailTaskFragment.TaskDetailLoadedListener,
        DialogDetailTaskFragment.LocationUpdateListener {

    private static final String TAG = WorkDetailServiceFragment.class.getSimpleName();

    private RelativeLayout rlExpandHeaderBill;
    private LinearLayout ll_content_bill;
    private LinearLayout tv_detail_work_call_action;
    private LinearLayout tv_detail_work_sms_action;
    private LinearLayout tv_detail_work_address_action;
    private ImageView imv_expand_bill;
    private TextView tv_detail_work_contact_name;
    private TextView tv_detail_work_contact_phone;
    private TextView tv_total_value;
    private TextView tv_vat;
    private TextView tv_have_to_pay;
    private TextView tv_detail_work_total_payment;
    private TextView btn_confirm_charge;
    private double latitude;
    private double longitude;
    private RecyclerView rv_service;

    private OnPayCompletedListener onPayCompletedListener;

    private WorkDetailServiceRecyclerViewAdapter mWorkDetailServiceRecyclerViewAdapter;

    public WorkDetailServiceFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_detail_service, container, false);
        ll_content_bill = (LinearLayout) view.findViewById(R.id.ll_content_bill);
        imv_expand_bill = (ImageView) view.findViewById(R.id.imv_expand_bill);
        rlExpandHeaderBill = (RelativeLayout) view.findViewById(R.id.rl_expand_header_bill);
        rlExpandHeaderBill.setOnClickListener(this);

        tv_detail_work_contact_name = (TextView) view.findViewById(R.id.tv_detail_work_contact_name);
        tv_detail_work_contact_phone = (TextView) view.findViewById(R.id.tv_detail_work_contact_phone);
        tv_detail_work_call_action = (LinearLayout) view.findViewById(R.id.tv_detail_work_call_action);
        tv_detail_work_sms_action = (LinearLayout) view.findViewById(R.id.tv_detail_work_sms_action);
        tv_detail_work_address_action = (LinearLayout) view.findViewById(R.id.tv_detail_work_address_action);

        tv_total_value = (TextView) view.findViewById(R.id.tv_total_value);
        tv_vat = (TextView) view.findViewById(R.id.tv_vat);
        tv_have_to_pay = (TextView) view.findViewById(R.id.tv_have_to_pay);
        tv_detail_work_total_payment = (TextView) view.findViewById(R.id.tv_detail_work_total_payment);
        btn_confirm_charge = view.findViewById(R.id.btn_confirm_charge);
        btn_confirm_charge.setOnClickListener(this);
        rv_service = (RecyclerView) view.findViewById(R.id.rv_service);
        rv_service.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mWorkDetailServiceRecyclerViewAdapter = new WorkDetailServiceRecyclerViewAdapter(getContext());
        rv_service.setAdapter(mWorkDetailServiceRecyclerViewAdapter);

        ((DialogDetailTaskFragment) getParentFragment()).setTaskDetailLoadedListener(this);
        ((DialogDetailTaskFragment) getParentFragment()).setLocationUpdateListeners(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_charge:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Xác nhận khách hàng đã thanh toán cho đơn hàng");
                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        handleConfirmChangeAction();
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
//            case R.id.rl_expand_header_bill:
//                if (ll_content_bill.getVisibility() == View.GONE) {
//                    ll_content_bill.setVisibility(View.VISIBLE);
//                    imv_expand_bill.setImageResource(R.drawable.ic_expand_task_details);
//                } else {
//                    ll_content_bill.setVisibility(View.GONE);
//                    imv_expand_bill.setImageResource(R.drawable.ic_chevron_right);
//                }
//                break;
        }
    }

    private void handleConfirmChangeAction() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment instanceof DialogDetailTaskFragment) {
            DialogDetailTaskFragment dialogDetailTaskFragment = (DialogDetailTaskFragment) parentFragment;
            GetTaskDetailResult getTaskDetailResult = dialogDetailTaskFragment.getGetTaskDetailResult();
            if (getTaskDetailResult != null && getTaskDetailResult.result != null) {
                String idTask = getTaskDetailResult.result._id;
                List<MHead> arrHeads = new ArrayList<>();
                arrHeads.add(new MHead(Conts.KEY_ACCESS_TOKEN, UserInfo.getInstance(getActivity()).getAccessToken()));
                ApiUtils.getAPIService(arrHeads).confirmCharge(idTask).enqueue(new Callback<ConfirmChargeResponse>() {
                    @Override
                    public void onResponse(Call<ConfirmChargeResponse> call, Response<ConfirmChargeResponse> response) {
                        if (response.isSuccessful()) {
                            ConfirmChargeResponse confirmChargeResponse = response.body();
                            if (confirmChargeResponse.getStatusCode() == Conts.RESPONSE_STATUS_OK) {
                                CommonMethod.makeToast(getActivity(), "Xác nhận thanh toán thành công");
                                btn_confirm_charge.setEnabled(false);
                                btn_confirm_charge.setBackgroundColor(Color.parseColor("#BDBDBD"));
                                btn_confirm_charge.setText("Đã thanh toán");

                                if (onPayCompletedListener != null) {
                                    onPayCompletedListener.onPayCompleted();
                                }
                            } else {
                                CommonMethod.makeToast(getActivity(), "Chưa hoàn thành dịch vụ");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ConfirmChargeResponse> call, Throwable t) {
                        CommonMethod.makeToast(getActivity(), "Xác nhận thanh toán thất bại");
                    }
                });
            }
        }
    }

    @Override
    public void onTaskDetailLoaded(GetTaskDetailResult getTaskDetailResult) {
        Log.d(TAG, "onTaskDetailLoaded");
        try {
            Log.d(TAG, "onTaskDetailLoaded, invoice.status._id: " + getTaskDetailResult.result.invoice.status._id);
            if (getTaskDetailResult.result.invoice.status._id > 1) {
                btn_confirm_charge.setEnabled(false);
                btn_confirm_charge.setBackgroundColor(Color.parseColor("#BDBDBD"));
                btn_confirm_charge.setText("Đã thanh toán");

                if (onPayCompletedListener != null) {
                    onPayCompletedListener.onPayCompleted();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "onTaskDetailLoaded(), Exception1: " + e);
        }

        try {
            if (getTaskDetailResult.result.address != null && getTaskDetailResult.result.address.location != null) {
                handleActions(getTaskDetailResult.result.address.location.latitude, getTaskDetailResult.result.address.location.longitude);
            } else if (getTaskDetailResult.result.customer.address != null && getTaskDetailResult.result.customer.address.location != null) {
                handleActions(getTaskDetailResult.result.customer.address.location.latitude, getTaskDetailResult.result.customer.address.location.longitude);
            } else {
                Toast.makeText(getActivity(), "Not found the work address.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "onTaskDetailLoaded(), Exception1: " + e);
        }
        try {
            if (getTaskDetailResult.result.customer != null) {
                tv_detail_work_contact_name.setText(getTaskDetailResult.result.customer.fullname + "");
                tv_detail_work_contact_phone.setText(getTaskDetailResult.result.customer.phone + "");
                handleActions(getTaskDetailResult.result.customer);
            }

            List<ItemPrice> itemPrices = new ArrayList<>();
            if (getTaskDetailResult.result.service != null) {
                itemPrices.add(new ItemPrice(ItemPrice.TYPE_SERVICES, getTaskDetailResult.result.service._id, getTaskDetailResult.result.service.name, getTaskDetailResult.result.service.tax, getTaskDetailResult.result.service.price, 1));
            }
            if (getTaskDetailResult.result.process != null) {
                for (int i = 0; i < getTaskDetailResult.result.process.length; i++) {
                    GetTaskDetailResult.Result.Process process = getTaskDetailResult.result.process[i];
                    if (process != null && process.process != null) {
                        GetTaskDetailResult.Result.Process.ProcessDetail.Service[] services = process.process.services;
                        if (services != null) {
                            for (GetTaskDetailResult.Result.Process.ProcessDetail.Service service : services) {
                                if (service != null) {
                                    itemPrices.add(new ItemPrice(ItemPrice.TYPE_SERVICES, service._id, service.product.name, service.product.tax, service.product.price, service.quantity));
                                }
                            }
                        }

                        GetTaskDetailResult.Result.Process.ProcessDetail.Product[] products = process.process.products;
                        if (products != null) {
                            for (GetTaskDetailResult.Result.Process.ProcessDetail.Product product : products) {
                                if (product != null) {
                                    itemPrices.add(new ItemPrice(ItemPrice.TYPE_SERVICES, product._id, product.product.name, product.product.tax, product.product.price, product.quantity));
                                }
                            }
                        }
                    }
                }
            }

            long totalValue = 0;
            long totalTax = 0;
            for (ItemPrice itemPrice : itemPrices) {
                if (itemPrice != null) {
                    totalValue += itemPrice.getPrice() * itemPrice.getQuantity();
                    totalTax += totalValue * itemPrice.getTax() / 100;
                }
            }

            tv_total_value.setText(CommonMethod.formatCurrency(totalValue));
            tv_vat.setText(CommonMethod.formatCurrency(totalTax));
            tv_have_to_pay.setText(CommonMethod.formatCurrency((totalValue + totalTax)));
            tv_detail_work_total_payment.setText(CommonMethod.formatCurrency(totalValue + totalTax) + " đ");

            mWorkDetailServiceRecyclerViewAdapter.updateServiceList(itemPrices);

        } catch (Exception e) {
            Log.e(TAG, "onTaskDetailLoaded(), Exception: " + e);
        }
    }

    private void handleActions(final double targetLatitude, final double targetLongitude) {
        tv_detail_work_address_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + latitude + "," + longitude
                                + "&daddr=" + targetLatitude + "," + targetLongitude));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onLocationUpdate(double latitude, double longitude) {
        Log.d(TAG, "onLocationUpdate, latitude: " + latitude + ", longitude: " + longitude);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private void handleActions(final GetTaskDetailResult.Result.Customer customer) {
        tv_detail_work_call_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", customer.phone, null)));
            }
        });
        tv_detail_work_sms_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse("smsto:" + customer.phone);
                    Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
                    smsIntent.putExtra("sms_body", "sms content");
                    startActivity(smsIntent);
                } catch (Exception e) {
                    Log.e(TAG, "send sms --> Exception occurs.", e);
                }
            }
        });
    }

    public interface OnPayCompletedListener {
        void onPayCompleted();
    }

    public void setOnPayCompletedListener(OnPayCompletedListener onPayCompletedListener) {
        this.onPayCompletedListener = onPayCompletedListener;
    }
}
