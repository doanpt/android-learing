package com.cnc.hcm.cnctracking.api;


import com.cnc.hcm.cnctracking.model.AddContainProductResult;
import com.cnc.hcm.cnctracking.model.AddProductItem;
import com.cnc.hcm.cnctracking.model.AddProductResult;
import com.cnc.hcm.cnctracking.model.CategoryListResult;
import com.cnc.hcm.cnctracking.model.CheckContainProductResult;
import com.cnc.hcm.cnctracking.model.ConfirmChargeResponse;
import com.cnc.hcm.cnctracking.model.CountTaskResult;
import com.cnc.hcm.cnctracking.model.GetProductDetailResult;
import com.cnc.hcm.cnctracking.model.GetTaskDetailResult;
import com.cnc.hcm.cnctracking.model.GetTaskListResult;
import com.cnc.hcm.cnctracking.model.GetUserProfileResponseStatus;
import com.cnc.hcm.cnctracking.model.ItemTrackLocation;
import com.cnc.hcm.cnctracking.model.LoginResponseStatus;
import com.cnc.hcm.cnctracking.model.ProcessDeviceResult;
import com.cnc.hcm.cnctracking.model.ProductListResult;
import com.cnc.hcm.cnctracking.model.SubmitProcessParam;
import com.cnc.hcm.cnctracking.model.UpdateLocationResponseStatus;
import com.cnc.hcm.cnctracking.model.UpdateProcessResult;
import com.cnc.hcm.cnctracking.model.UploadImageResult;
import com.cnc.hcm.cnctracking.util.Conts;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIService {

    @PATCH(Conts.PATH_TRACKING)
    Call<UpdateLocationResponseStatus> updateLocation(@Body ItemTrackLocation body);

    @GET(Conts.PATH_USER_LOGIN)
    Call<LoginResponseStatus> login();

    @GET(Conts.PATH_USER_PROFILE)
    Call<GetUserProfileResponseStatus> getUserProfile();

    @GET(Conts.PATH_TASK_LIST)
    Call<GetTaskListResult> getTaskList();

    @GET(Conts.PATH_TASK_DETAIL)
    Call<GetTaskDetailResult> getTaskDetails(@Path("id") String taskId);

    @PATCH(Conts.PATH_TASK_DETAIL_PROCESS_DEVICE)
    Call<ProcessDeviceResult> processDevice(@Path("id") String taskId);

    @GET(Conts.PATH_GET_PRODUCT_BY_ID)
    Call<CheckContainProductResult> getProductById(@Path("id") String productID);

    @GET(Conts.PATH_GET_PRODUCTS_DETAIL)
    Call<GetProductDetailResult> getDetailProduct(@Path("id") String idTask);

    @PATCH(Conts.PATH_ADD_DEVICE_CONTAIN)
    Call<AddContainProductResult> addProductContain(@Path("id") String productID);

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @POST(Conts.PATH_ADD_CUSTOMER_PRODUCTS)
    Call<AddProductResult> addCustomerProduct(@Body AddProductItem product);

    @GET(Conts.PATH_PRODUCTS_BRAND)
    Call<ProductListResult> getListManufactures();

    @GET(Conts.PATH_PRODUCTS_CATEGORY)
    Call<CategoryListResult> getListCategory();

    @GET(Conts.PATH_COUNT_TASK)
    Call<CountTaskResult> getCountTask();

    @Multipart
    @POST(Conts.PATH_UPLOAD_IMAGE_TO_SERVER)
    Call<UploadImageResult> uploadPhoto(@Part MultipartBody.Part filePart,@Path("id") String idTask);

    @Headers({"Content-Type: application/json"})
    @PATCH(Conts.PATH_UPDATE_PROCESS)
    Call<UpdateProcessResult> updateProcess(@Path("id") String productID ,@Body SubmitProcessParam param);

    @PATCH(Conts.PATH_COMPLETE_PROCESS)
    Call<UpdateProcessResult> completeProcess(@Path("id") String productID );

    @PATCH(Conts.PATH_CONFIRM_CHARGE)
    Call<ConfirmChargeResponse> confirmCharge(@Path("id") String idTask);
}