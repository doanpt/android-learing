package com.t3h.demoabc;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.telecom.Call;

public class Utils {
    @TargetApi(Build.VERSION_CODES.M)
    public static GsmCall toGsmCall(Call call) {
        if (call != null) {
            GsmCall.Status status = toGsmCallStatus(call.getState());
            Call.Details details = call.getDetails();
            Uri var1 = details.getHandle();
            return new GsmCall(status, var1.getSchemeSpecificPart());
        }
        return null;
    }

    private static GsmCall.Status toGsmCallStatus(int receiver) {
        GsmCall.Status status;
        switch (receiver) {
            case 1:
                status = GsmCall.Status.DIALING;
                break;
            case 2:
                status = GsmCall.Status.RINGING;
                break;
            case 4:
                status = GsmCall.Status.ACTIVE;
                break;
            case 7:
                status = GsmCall.Status.DISCONNECTED;
                break;
            case 9:
                status = GsmCall.Status.CONNECTING;
                break;
            case 3:
            case 5:
            case 6:
            case 8:
            default:
                status = GsmCall.Status.UNKNOWN;
                break;
        }

        return status;
    }
}
