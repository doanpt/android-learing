package com.cnc.hcm.cnctrack.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnc.hcm.cnctrack.R;
import com.cnc.hcm.cnctrack.adapter.WorkDetailRequireRecyclerViewAdapter;
import com.cnc.hcm.cnctrack.adapter.WorkDetailServiceRecyclerViewAdapter;
import com.cnc.hcm.cnctrack.api.ApiUtils;
import com.cnc.hcm.cnctrack.api.MHead;
import com.cnc.hcm.cnctrack.base.BaseFragment;
import com.cnc.hcm.cnctrack.model.ConfirmChargeResponse;
import com.cnc.hcm.cnctrack.model.GetTaskDetailResult;
import com.cnc.hcm.cnctrack.model.ItemPrice;
import com.cnc.hcm.cnctrack.util.CommonMethod;
import com.cnc.hcm.cnctrack.util.Conts;
import com.cnc.hcm.cnctrack.util.UserInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkDetailServiceFragment extends BaseFragment implements
        View.OnClickListener, DialogDetailTaskFragment.TaskDetailLoadedListener {

    private static final String TAG = WorkDetailServiceFragment.class.getSimpleName();

    private RelativeLayout rlExpandHeaderBill;
    //    private LinearLayout ll_content_bill;
    private LinearLayout tv_detail_work_call_action;
    private LinearLayout tv_detail_work_sms_action;
    private LinearLayout tv_detail_work_address_action;
    private LinearLayout llExpandService;
    //    private ImageView imv_expand_bill;
    private ImageView imvExpandService;
    private TextView tv_detail_work_contact_name;
    private TextView tv_detail_work_contact_phone;
    private TextView tv_number_service;
    private TextView tv_number_device;
    private TextView tv_note;
    private TextView tv_total_value;
    private TextView tv_vat;
    private TextView tv_have_to_pay;
    private TextView tv_detail_work_total_payment;
    private TextView btn_confirm_charge;
    private RecyclerView rv_service;
    private RecyclerView rv_require;

//    private OnPayCompletedListener onPayCompletedListener;

    private WorkDetailServiceRecyclerViewAdapter mWorkDetailServiceRecyclerViewAdapter;
    private WorkDetailRequireRecyclerViewAdapter mWorkDetailRequireRecyclerViewAdapter;

    public WorkDetailServiceFragment() {
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_work_detail_service;
    }

    @Override
    public void onViewReady(View view) {
        iniViews(view);
    }

    private void iniViews(View view) {
        llExpandService = view.findViewById(R.id.ll_require_expand);
        imvExpandService = view.findViewById(R.id.imv_expand_require);
        imvExpandService.setOnClickListener(this);

        rlExpandHeaderBill = (RelativeLayout) view.findViewById(R.id.rl_expand_header_bill);
        rlExpandHeaderBill.setOnClickListener(this);

        tv_detail_work_contact_name = (TextView) view.findViewById(R.id.tv_detail_work_contact_name);
        tv_detail_work_contact_phone = (TextView) view.findViewById(R.id.tv_detail_work_contact_phone);
        tv_detail_work_call_action = (LinearLayout) view.findViewById(R.id.tv_detail_work_call_action);
        tv_detail_work_sms_action = (LinearLayout) view.findViewById(R.id.tv_detail_work_sms_action);
        tv_detail_work_address_action = (LinearLayout) view.findViewById(R.id.tv_detail_work_address_action);

        tv_note = view.findViewById(R.id.tv_note);
        tv_number_service = view.findViewById(R.id.tv_number_service);
        tv_number_device = view.findViewById(R.id.tv_number_device);
        tv_total_value = (TextView) view.findViewById(R.id.tv_total_value);
        tv_vat = (TextView) view.findViewById(R.id.tv_vat);
        tv_have_to_pay = (TextView) view.findViewById(R.id.tv_have_to_pay);
        tv_detail_work_total_payment = (TextView) view.findViewById(R.id.tv_detail_work_total_payment);
        btn_confirm_charge = view.findViewById(R.id.btn_confirm_charge);
        btn_confirm_charge.setOnClickListener(this);

        rv_require = view.findViewById(R.id.rv_require);
        rv_require.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mWorkDetailRequireRecyclerViewAdapter = new WorkDetailRequireRecyclerViewAdapter(getContext());
        rv_require.setAdapter(mWorkDetailRequireRecyclerViewAdapter);

        rv_service = (RecyclerView) view.findViewById(R.id.rv_service);
        rv_service.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mWorkDetailServiceRecyclerViewAdapter = new WorkDetailServiceRecyclerViewAdapter(getContext());
        rv_service.setAdapter(mWorkDetailServiceRecyclerViewAdapter);

        ((DialogDetailTaskFragment) getParentFragment()).setTaskDetailLoadedListener(this);
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
            case R.id.imv_expand_require:
                if (llExpandService.getVisibility() == View.GONE) {
                    llExpandService.setVisibility(View.VISIBLE);
                    imvExpandService.setImageResource(R.drawable.ic_expand_task_details);
                } else {
                    llExpandService.setVisibility(View.GONE);
                    imvExpandService.setImageResource(R.drawable.ic_chevron_right);
                }
                break;
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
                        int statusCode = response.body().getStatusCode();
                        if (response.isSuccessful()) {
                            if (statusCode == Conts.RESPONSE_STATUS_OK) {
                                ConfirmChargeResponse confirmChargeResponse = response.body();
                                if (confirmChargeResponse != null && confirmChargeResponse.getStatusCode() == Conts.RESPONSE_STATUS_OK) {
                                    CommonMethod.makeToast(getActivity(), "Xác nhận thanh toán thành công");
                                    btn_confirm_charge.setEnabled(false);
                                    btn_confirm_charge.setBackgroundColor(Color.parseColor("#BDBDBD"));
                                    btn_confirm_charge.setText("Đã thanh toán");

//                                if (onPayCompletedListener != null) {
//                                    onPayCompletedListener.onPayCompleted();
//                                }
                                } else {
                                    CommonMethod.makeToast(getActivity(), "Chưa hoàn thành dịch vụ");
                                }
                            } else if (statusCode == Conts.RESPONSE_STATUS_OK) {
                                actionLogout();
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

//                if (onPayCompletedListener != null) {
//                    onPayCompletedListener.onPayCompleted();
//                }
            }
        } catch (Exception e) {
            Log.e(TAG, "onTaskDetailLoaded(), Exception1: " + e);
        }

        try {
            if (getTaskDetailResult.result.address != null && getTaskDetailResult.result.address.getLocation() != null) {
                handleActions(getTaskDetailResult.result.address.getLocation().latitude, getTaskDetailResult.result.address.getLocation().longitude);
            } else if (getTaskDetailResult.result.customer.address != null && getTaskDetailResult.result.customer.address.location != null) {
                handleActions(getTaskDetailResult.result.customer.address.location.latitude, getTaskDetailResult.result.customer.address.location.longitude);
            } else {
                Toast.makeText(getActivity(), "Not found the work address.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "onTaskDetailLoaded(), Exception2: " + e);
        }

        try {
            if (getTaskDetailResult.result.recipient != null) {
                tv_detail_work_contact_name.setText(getTaskDetailResult.result.recipient.getFullname() + "");
                tv_detail_work_contact_phone.setText(getTaskDetailResult.result.recipient.getPhone() + "");
                handleActions(getTaskDetailResult.result.recipient.getPhone());
            } else {
                if (getTaskDetailResult.result.customer != null) {
                    tv_detail_work_contact_name.setText(getTaskDetailResult.result.customer.fullname + "");
                    tv_detail_work_contact_phone.setText(getTaskDetailResult.result.customer.phone + "");
                    handleActions(getTaskDetailResult.result.customer.phone);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "onTaskDetailLoaded(), Exception3: " + e);
        }

        try {
            String noteLable = "Ghi chú: ";
            String note = noteLable;
            if (getTaskDetailResult.result.note != null) {
                note += getTaskDetailResult.result.note;
            }

            SpannableStringBuilder ssBuilder = new SpannableStringBuilder(note);
            ssBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)), 0, noteLable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, noteLable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            tv_note.setText(ssBuilder);
        } catch (Exception e) {
            Log.e(TAG, "onTaskDetailLoaded(), Exception4: " + e);
        }

        try {
            GetTaskDetailResult.Result.RecommendedServices[] recommendedServices = getTaskDetailResult.result.recommendedServices;
            mWorkDetailRequireRecyclerViewAdapter.updateRequireList(Arrays.asList(recommendedServices));

            int totalService = 0;
            int totalDevice = 0;
            for (GetTaskDetailResult.Result.RecommendedServices item : recommendedServices) {
                if (item != null) {
                    totalService += 1;
                    totalDevice += item.quantity;
                }
            }

            tv_number_service.setText(totalService + Conts.BLANK);
            tv_number_device.setText(totalDevice + Conts.BLANK);
        } catch (Exception e) {
            Log.e(TAG, "onTaskDetailLoaded(), Exception5: " + e);
        }

        try {
            List<ItemPrice> itemPrices = new ArrayList<>();
//            if (getTaskDetailResult.result.service != null) {
//                itemPrices.add(new ItemPrice(ItemPrice.TYPE_SERVICES, getTaskDetailResult.result.service._id, getTaskDetailResult.result.service.name, getTaskDetailResult.result.service.tax, getTaskDetailResult.result.service.price, 1));
//            }
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
                    totalTax += itemPrice.getPrice() * itemPrice.getQuantity() * itemPrice.getTax() / 100;
                }
            }

            tv_total_value.setText(CommonMethod.formatCurrency(totalValue));
            tv_vat.setText(CommonMethod.formatCurrency(totalTax));
            tv_have_to_pay.setText(CommonMethod.formatCurrency((totalValue + totalTax)));
            tv_detail_work_total_payment.setText(CommonMethod.formatCurrency(totalValue + totalTax) + " đ");

            mWorkDetailServiceRecyclerViewAdapter.updateServiceList(itemPrices);

        } catch (Exception e) {
            Log.e(TAG, "onTaskDetailLoaded(), Exception6: " + e);
        }
    }

    private void handleActions(final double targetLatitude, final double targetLongitude) {
        DialogDetailTaskFragment dialogDetailTaskFragment = (DialogDetailTaskFragment) getParentFragment();
        final double latitude = dialogDetailTaskFragment.getMainActivity().getLatitude();
        final double longtitude = dialogDetailTaskFragment.getMainActivity().getLongtitude();
        tv_detail_work_address_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + latitude + "," + longtitude
                                + "&daddr=" + targetLatitude + "," + targetLongitude));
                startActivity(intent);
            }
        });
    }

    private void handleActions(final String numPhone) {
        tv_detail_work_call_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", numPhone, null)));
            }
        });
        tv_detail_work_sms_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse("smsto:" + numPhone);
                    Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
                    smsIntent.putExtra("sms_body", "sms content");
                    startActivity(smsIntent);
                } catch (Exception e) {
                    Log.e(TAG, "send sms --> Exception occurs.", e);
                }
            }
        });
    }

//    public interface OnPayCompletedListener {
//        void onPayCompleted();
//    }
//
//    public void setOnPayCompletedListener(OnPayCompletedListener onPayCompletedListener) {
//        this.onPayCompletedListener = onPayCompletedListener;
//    }
}
