package com.joselopezrosario.androidfm;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class CreateDelete {
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
    public void createBlankRecordAndDeleteIt(){
        System.out.println("-----------------------");
        System.out.println("Create blank record and delete it");
        System.out.println("-----------------------");
        FmRequest request = new FmRequest()
                .create(ENDPOINT, token, LAYOUT_VGSALES)
                .build();
        FmResponse response = Fm.execute(request);
        assert response.isOk();
        int recordId = response.getRecordId();
        request = new FmRequest()
                .delete(ENDPOINT, token, LAYOUT_VGSALES, recordId)
                .build();
        response = Fm.execute(request);
        assert response.isOk();
    }

    @Test
    public void createBlankRecordAndDeleteItWithScripts(){
        System.out.println("-----------------------");
        System.out.println("Create blank record, delete it, and run script");
        System.out.println("-----------------------");
        FmRequest request = new FmRequest()
                .create(ENDPOINT, token, LAYOUT_VGSALES)
                .build();
        FmResponse response = Fm.execute(request);
        if ( !response.isOk()){
            assert false;
            return;
        }
        int recordId = response.getRecordId();
        FmScript script = new FmScript()
                .setScript("log").setScriptParam("Delete record " + recordId);
        request = new FmRequest()
                .delete(ENDPOINT, token, LAYOUT_VGSALES, recordId)
                .setScriptPrams(script)
                .build();
        response = Fm.execute(request);
        assert response.isOk();
    }

    /**
     * createRecordAndSetValues
     */
    @Test
    public void createRecordAndSetValues() {
        System.out.println("-----------------------");
        System.out.println("Create a record and set values");
        System.out.println("-----------------------");
        FmEdit edit = new FmEdit()
                .set("Rank", "999732")
                .set("Name", "Jose's game")
                .set("Publisher", "Lorem ipsum")
                .set("Genre", "Arcade")
                .set("Platform", "Nes")
                .set("Year", "1981");
        FmRequest request = new FmRequest()
                .create(ENDPOINT, token, LAYOUT_VGSALES, edit)
                .build();
        FmResponse response = Fm.execute(request);
        int recordId = response.getRecordId();
        request = new FmRequest()
                .getRecord(ENDPOINT, token, LAYOUT_VGSALES, recordId)
                .build();
        response = Fm.execute(request);
        FmData fmData = new FmData(response);
        TestUtils.parseVgSales(fmData.getRecord(0));
        assert response.isOk();


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