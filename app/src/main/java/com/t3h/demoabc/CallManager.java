package com.t3h.demoabc;

import android.annotation.TargetApi;
import android.os.Build;
import android.telecom.Call;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

@TargetApi(Build.VERSION_CODES.M)
public class CallManager {
    private static final String LOG_TAG = "CallManager";
    private static BehaviorSubject<GsmCall> subject = BehaviorSubject.create();
    private static Call currentCall;

    public static Observable<GsmCall> updates() {
        return subject;
    }

    public static void updateCall(Call call) {
        currentCall = call;
        if (call != null) {
            subject.onNext(Utils.toGsmCall(call));
        }
    }

    public static void cancelCall() {
        if (currentCall != null) {
            switch (currentCall.getState()) {
                case Call.STATE_RINGING:
                    rejectCall();
                    break;
                default:
                    disconnectCall();
                    break;
            }
        }
    }
    public static void acceptCall() {
        Log.i(LOG_TAG, "acceptCall");
        if(currentCall!=null){
            currentCall.answer(currentCall.getDetails().getVideoState());
        }
    }
    private static void rejectCall() {
        Log.i(LOG_TAG, "rejectCall");
        currentCall.reject(false, "");
    }


    private static void disconnectCall() {
        Log.i(LOG_TAG, "disconnectCall");
        currentCall.disconnect();
    }
}

