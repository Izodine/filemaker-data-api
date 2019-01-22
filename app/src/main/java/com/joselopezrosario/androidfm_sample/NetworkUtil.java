package com.joselopezrosario.androidfm_sample;

import android.os.Build;

import com.joselopezrosario.androidfm.Fm;
import com.joselopezrosario.androidfm.FmData;
import com.joselopezrosario.androidfm.FmFind;
import com.joselopezrosario.androidfm.FmRequest;
import com.joselopezrosario.androidfm.FmResponse;

public final class NetworkUtil {

    private NetworkUtil() { /*Suppressed Constructor*/}

    public static FmData getAllRecords() {
        String url = BuildConfig.API_HOST;

        // Login to receive the token
        FmRequest request = new FmRequest().login(url, BuildConfig.API_ACCOUNT, BuildConfig.API_PASSWORD).build();
        FmResponse response = Fm.execute(request);
        String token = response.getToken();

        // Build the find request to find all assets where id = * (that means not empty)
        // ASSET ID MATCH FIELD is the primary id
        FmFind fmFind = new FmFind().newRequest().set("ASSET ID MATCH FIELD", "*");
        request = new FmRequest().findRecords(url, token, "API_Assets", fmFind).build();
        response = Fm.execute(request);
        if (response.isOk()) {
            // Create the data object
            FmData fmData = new FmData(response);
            // Logout
            request = new FmRequest().logout(url, token).build();
            Fm.execute(request);

            return fmData;
        } else {
            return new FmData(response);
        }

    }

}
