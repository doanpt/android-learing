package com.cnc.hcm.cnctrack.api;


import com.cnc.hcm.cnctrack.model.AddContainProductResult;
import com.cnc.hcm.cnctrack.model.AddProductItemRequest;
import com.cnc.hcm.cnctrack.model.CommonAPICallBackResult;
import com.cnc.hcm.cnctrack.model.CategoryListResult;
import com.cnc.hcm.cnctrack.model.CheckContainProductResult;
import com.cnc.hcm.cnctrack.model.CountTaskResult;
import com.cnc.hcm.cnctrack.model.GetChangeTicketAppointmentReasonsResult;
import com.cnc.hcm.cnctrack.model.GetTaskDetailResult;
import com.cnc.hcm.cnctrack.model.GetTaskListResult;
import com.cnc.hcm.cnctrack.model.GetUserProfileResponseStatus;
import com.cnc.hcm.cnctrack.model.ItemTrackLocation;
import com.cnc.hcm.cnctrack.model.LoginResponseStatus;
import com.cnc.hcm.cnctrack.model.ProcessDeviceResult;
import com.cnc.hcm.cnctrack.model.ProductListResult;
import com.cnc.hcm.cnctrack.model.RemoveDeviceFromTaskResult;
import com.cnc.hcm.cnctrack.model.ResponseCNC;
import com.cnc.hcm.cnctrack.model.SubmitProcessParam;
import com.cnc.hcm.cnctrack.model.UpdateLocationResponseStatus;
import com.cnc.hcm.cnctrack.model.UpdateProcessResult;
import com.cnc.hcm.cnctrack.model.UploadImageResult;
import com.cnc.hcm.cnctrack.model.DetailProduct;
import com.cnc.hcm.cnctrack.model.common.Product_Products;
import com.cnc.hcm.cnctrack.model.common.Service_Services;
import com.cnc.hcm.cnctrack.util.Conts;

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
    Call<DetailProduct> getDetailProduct(@Path("id") String idTask);

    @PATCH(Conts.PATH_ADD_DEVICE_CONTAIN)
    Call<AddContainProductResult> addProductContain(@Path("id") String productID);

    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @POST(Conts.PATH_ADD_CUSTOMER_PRODUCTS)
    Call<CommonAPICallBackResult> addCustomerProduct(@Body AddProductItemRequest product);

    @GET(Conts.PATH_PRODUCTS_BRAND)
    Call<ProductListResult> getListManufactures();

    @GET(Conts.PATH_PRODUCTS_CATEGORY)
    Call<CategoryListResult> getListCategory();

    @GET(Conts.PATH_COUNT_TASK)
    Call<CountTaskResult> getCountTask();

    @Multipart
    @POST(Conts.PATH_UPLOAD_IMAGE_TO_SERVER)
    Call<UploadImageResult> uploadPhoto(@Part MultipartBody.Part filePart, @Path("id") String idTask);

    @Headers({"Content-Type: application/json"})
    @PATCH(Conts.PATH_UPDATE_PROCESS)
    Call<DetailProduct> updateProcess(@Path("id") String productID, @Body SubmitProcessParam param);

    @PATCH(Conts.PATH_COMPLETE_PROCESS)
    Call<UpdateProcessResult> completeProcess(@Path("id") String productID);

    @PATCH(Conts.PATH_CONFIRM_CHARGE)
    Call<CommonAPICallBackResult> confirmCharge(@Path("id") String idTask);

    @PATCH(Conts.PATH_COMPLETE_TICKET)
    Call<CommonAPICallBackResult> completeTicket(@Path("id") String idTask);

    @PATCH(Conts.PATH_UPDATE_STATUS_IS_READ)
    Call<ResponseCNC> updateStatusIsRead(@Path("id") String idTask);

    @GET(Conts.PATH_GET_TRADING_PRODUCT)
    Call<Product_Products> getListTraddingProduct();

    @GET(Conts.PATH_GET_SERVICES)
    Call<Service_Services> getServices();

    @GET(Conts.PATH_REASONS_CHANGE_TICKET_APPOINTMENT_DATE)
    Call<GetChangeTicketAppointmentReasonsResult> getChangeTicketAppointmentReasons();

    @PATCH(Conts.PATH_CHANGE_TICKET_APPOINTMENT_DATE)
    Call<CommonAPICallBackResult> changeTicketAppointment(@Path("id") String idTask);

    @PATCH(Conts.PATH_REMOVE_DEVICE_FROM_TASK)
    Call<RemoveDeviceFromTaskResult> removeDeviceFromTask(@Path("id") String taskId);
}