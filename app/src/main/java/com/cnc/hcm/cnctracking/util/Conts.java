package com.cnc.hcm.cnctracking.util;

/**
 * Created by giapmn on 9/15/17.
 */

public class Conts {

    public static final String BLANK = "";
    public static final String UNKNOWN = "Unknown";

    //Default value int
    public static final int DEFAULT_VALUE_INT_INVALID = Integer.MIN_VALUE;
    public static final int DEFAULT_VALUE_INT_0 = 0;
    public static final int DEFAULT_VALUE_INT_1 = 1;
    public static final int DEFAULT_VALUE_INT_2 = 2;
    public static final int DEFAULT_VALUE_INT_3 = 3;
    public static final int DEFAULT_VALUE_INT_4 = 4;
    public static final int DEFAULT_VALUE_INT_5 = 5;
    public static final int DEFAULT_VALUE_INT_6 = 6;
    public static final int DEFAULT_VALUE_INT_7 = 7;
    public static final int DEFAULT_VALUE_INT_8 = 8;
    public static final int DEFAULT_VALUE_INT_9 = 9;
    public static final int DEFAULT_VALUE_INT_10 = 10;
    public static final int DEFAULT_VALUE_INT_11 = 11;
    public static final int DEFAULT_VALUE_INT_12 = 12;


    public static final long RESPONSE_STATUS_OK = 200;
    public static final long RESPONSE_STATUS_TOKEN_WRONG = 403;
    public static final int RESPONSE_STATUS_404 = 404;
    public static final int RESPONSE_STATUS_500 = 500;

    public static final String BASE_URL = "http://35.198.195.55:3001/";
    public static final String URL_BASE = "http://35.198.195.55:3001";

    public static final String PATH_TRACKING = "/tracking";

    public static final String PATH_USER = "/user";
    public static final String PATH_USER_LOGIN = PATH_USER + "/login";

    public static final String PATH_TASK_LIST = "/tasks";
    public static final String PATH_TASK_DETAIL = "/task/{id}";
    public static final String PATH_TASK_DETAIL_PROCESS_DEVICE = PATH_TASK_DETAIL + "/process-device";
    public static final String PATH_UPDATE_STATUS_IS_READ = PATH_TASK_DETAIL + "/read";

    public static final String PATH_PRODUCTS_BRAND = "/product-brands";
    public static final String PATH_PRODUCTS_CATEGORY = "/devices/categories";
    public static final String PATH_GET_PRODUCTS_DETAIL = "/task/{id}/get-device";

    public static final String PATH_GET_PRODUCT_BY_ID = "/customer-product/{id}";
    public static final String PATH_ADD_CUSTOMER_PRODUCTS = "/customer-products";
    public static final String PATH_USER_PROFILE = PATH_USER + "/profile";
    public static final String PATH_ADD_DEVICE_CONTAIN = "/task/{id}/add-device";
    public static final String PATH_COUNT_TASK = "/tasks/statistics";
    public static final String PATH_UPLOAD_IMAGE_TO_SERVER = "/task/{id}/device-photo";
    public static final String PATH_UPDATE_PROCESS = "/task/{id}/submit-process";
    public static final String PATH_COMPLETE_PROCESS = "/task/{id}/complete-process";
    public static final String PATH_COMPLETE_TICKET = "/task/{id}/done";
    public static final String PATH_CONFIRM_CHARGE = "/task/{id}/confirm-charge";
    public static final String PATH_GET_TRADING_PRODUCT = "/trading-products";
    public static final String PATH_GET_SERVICES = "/services";
    public static final String PATH_REASONS_CHANGE_TICKET_APPOINTMENT_DATE = "/reasons/change-ticket-appointment-date";
    public static final String PATH_CHANGE_TICKET_APPOINTMENT_DATE = "/task/{id}/change-appointment-date";

    public static final String KEY_AUTHORIZATION = "Authorization";
    public static final String KEY_CUSTOMER_ID = "customerId";
    public static final String KEY_DEVICE_ID = "deviceId";
    public static final String KEY_ACCESS_TOKEN = "accessToken";

    public static final String ACTION_NETWORK_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    public static final String FILE_LOCATION_NO_NETWORK = "NoNetwork.txt";
    public static final String FILE_LOCATION_NETWORK = "Network.txt";
    public static final String FILE_LOCATION_UPLOAD_SIZE = "UploadSize.txt";
    public static final String FILE_RESPONSE = "FileResponse.txt";

    public static final int TYPE_NEW_TASK = 0;
    public static final int TYPE_DOING_TASK = 1;
    public static final int TYPE_COMPLETE_TASK = 2;
    public static final int TYPE_ALL_TASK = 99;
    public static final int TYPE_CANCEL_TASK = 3;


    public static final int TYPE_VIEW_BY_MONTH = 0;
    public static final int TYPE_VIEW_BY_YEARS = 1;


    public static final String KEY_ID_TASK = "KEY_ID_TASK";
    public static final String KEY_PRODUCT_ID = "KEY_PRODUCT_ID";
    public static final String FORMAT_DATE_FULL = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String FORMAT_DATE_FULL2 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String FORMAT_TIME_FULL = "T00:00:00Z";
    public static final String KEY_START_DATE = "startDate";
    public static final String KEY_END_DATE = "endDate";

    public static final String SOCKET_EVENT_NEW_TASK = "new task";
    public static final String SOCKET_EVENT_LOGIN_OTHER_DEVICE = "login conflict";
    public static final String SOCKET_EVENT_ERROR = "error";
    public static final String SOCKET_EVENT_CANCEL_TASK = "cancel task";
    public static final String SOCKET_EVENT_UNASSIGNED_TASK = "unassigned task";
    public static final String SOCKET_EVENT_DISCONNECT = "disconnect";


    public static final String KEY_WORK_NAME = "KEY_WORK_NAME";
    public static final String KEY_WORK_LOCATION = "KEY_WORK_LOCATION";
    public static final String KEY_WORK_TIME = "KEY_WORK_TIME";
    public static final String KEY_ID_TASK_TO_SHOW_DETAIL = "KEY_ID_TASK_TO_SHOW_DETAIL";
    public static final String KEY_SERVICE_PRODUCT_RESULT = "KEY_SERVICE_PRODUCT_RESULT";
    public static final String KEY_SERVICE = "KEY_SERVICE";
    public static final String KEY_PRODUCT = "KEY_PRODUCT";
    public static final String KEY_CHECK_TYPE_RESULT = "KEY_SERVICE";
    public static final String KEY_RESULT_ADD_NOTE = "KEY_RESULT_ADD_NOTE";
    public static final String KEY_CURRENT_NOTE = "KEY_CURRENT_NOTE";
    public static final String KEY_APPOINTMENT_DATE = "appointmentDate";
    public static final String KEY_REASON_ID = "reasonId";
    public static final String TIME_ZONE_VN = "Asia/Ho_Chi_Minh";
}
