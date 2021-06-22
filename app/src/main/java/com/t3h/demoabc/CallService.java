package com.t3h.demoabc;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.telecom.Call;
import android.telecom.InCallService;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.M)
public class CallService extends InCallService {
    private static final String LOG_TAG = "CallService";

    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);
        Log.i(LOG_TAG, "onCallAdded: $call");
        call.registerCallback(callCallback);
        startActivity(new Intent(this, CallActivity.class));
        CallManager.updateCall(call);
    }

    @Override
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
        Log.i(LOG_TAG, "onCallRemoved: $call");
        call.unregisterCallback(callCallback);
        CallManager.updateCall(null);
    }

    private Call.Callback callCallback = new Call.Callback() {
        @Override
        public void onStateChanged(Call call, int state) {
            Log.i(LOG_TAG, "Call.Callback onStateChanged: " + call + " state:" + state);
            CallManager.updateCall(call);
        }
    };
}
