package com.joselopezrosario.androidfm;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class ParseRecords {
    private static final String ENDPOINT = BuildConfig.API_HOST;
    private static final String ACCOUNT = BuildConfig.API_ACCOUNT;
    private static final String PASSWORD = BuildConfig.API_PASSWORD;
    private static final String LAYOUT_VGSALES = "vgsales";
    private static final String LAYOUT_GENRES = "genres";
    private static final String LAYOUT_PUBLISHERS = "publishers";
    private static String token;

    @BeforeClass
    public static void startUp() {
        FmRequest loginRequest = new FmRequest()
                .login(ENDPOINT, ACCOUNT, PASSWORD)
                .build();
        if (!loginRequest.isOk()) {
            assert false;
            return;
        }
        FmResponse loginResponse = Fm.execute(loginRequest);
        if (!loginResponse.isOk()) {
            assert false;
            return;
        }
        token = loginResponse.getToken();
    }

    @Test
    public void loopThroughFmData(){
        System.out.println("-----------------------");
        System.out.println("loopThroughFmData");
        System.out.println("-----------------------");
        FmRequest request = new FmRequest()
                .getRecords(ENDPOINT, token, LAYOUT_VGSALES)
                .build();
        FmResponse response = Fm.execute(request);
        FmData fmData = new FmData(response);
        int size = fmData.size();
        if ( size == 0){
            assert false;
        }
        int i = 0;
        while ( i < size){
            FmRecord record = fmData.getRecord(i);
            assert TestUtils.parseVgSales(record);
            i ++;
        }
        assert true;
    }

    @Test
    public void testFmDataErrors(){
        System.out.println("-----------------------");
        System.out.println("Test FmData Errors");
        System.out.println("-----------------------");
        FmRequest request = new FmRequest()
                .getRecords(ENDPOINT, token, LAYOUT_VGSALES)
                .build();
        FmResponse response = Fm.execute(request);
        FmData fmData = new FmData(null);
        String error = fmData.getError();
        System.out.println("Error: " + error);
        assert error != null;
        fmData = new FmData(response);
        int size = fmData.size();
        fmData.getRecord(size+100);
        assert fmData.getError() !=null;
    }


    @AfterClass
    public static void logout() {
        FmRequest request = new FmRequest()
                .logout(ENDPOINT, token)
                .build();
        if (!request.isOk()) {
            assert false;
            return;
        }
        FmResponse response = Fm.execute(request);
        if (!response.isOk()) {
            assert false;
            return;
        }
        assert response.getHttpCode() == 200;

    }
}