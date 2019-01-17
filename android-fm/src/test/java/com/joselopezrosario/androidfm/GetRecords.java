package com.joselopezrosario.androidfm;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("ConstantConditions")
@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class GetRecords {
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
    public void validateToken() {
        System.out.println("-----------------------");
        System.out.println("Validate the token");
        System.out.println("-----------------------");
        System.out.println("Token: " + token);
        assert token != null;
    }

    @Test
    public void getRecords() {
        System.out.println("-----------------------");
        System.out.println("Get records");
        System.out.println("-----------------------");
        FmRequest request = new FmRequest()
                .getRecords(ENDPOINT, token, LAYOUT_VGSALES)
                .setLimit(100)
                .setOffset(1)
                .build();
        if (!request.isOk()) {
            assert false;
            return;
        }
        FmResponse response = Fm.execute(request);
        FmData fmData = new FmData(response);
        int max = fmData.size() - 1;
        System.out.println("Total records: " + max);
        int randomNum = ThreadLocalRandom.current().nextInt(0, max);
        System.out.println("Random record: " + randomNum);
        FmRecord record = fmData.getRecord(randomNum);
        assert TestUtils.parseVgSales(record);
    }

    /**
     * getRecord
     * Test the getRecord endpoint. First, call getRecords to get 3,000 records. Then, select a random
     * record from the foundset, get its id, and pass the id to a new getRecord request.
     */
    @Test
    public void getRecord() {
        System.out.println("-----------------------");
        System.out.println("Get record");
        System.out.println("-----------------------");
        FmRequest request = new FmRequest()
                .getRecords(ENDPOINT, token, LAYOUT_VGSALES)
                .setLimit(3000)
                .setOffset(1)
                .build();
        if (!request.isOk()) {
            assert false;
            return;
        }
        FmResponse response = Fm.execute(request);
        FmData fmData = new FmData(response);
        int max = fmData.size() - 1;
        int randomNum = ThreadLocalRandom.current().nextInt(0, max);
        System.out.println("Total records: " + max);
        System.out.println("Random record: " + randomNum);
        FmRecord record = fmData.getRecord(randomNum);
        int recId = record.getRecordId();
        request = new FmRequest()
                .getRecord(ENDPOINT, token, LAYOUT_VGSALES, recId)
                .setLimit(1)
                .setOffset(1)
                .build();
        if (!request.isOk()) {
            assert false;
            return;
        }
        response = Fm.execute(request);
        fmData = new FmData(response);
        record = fmData.getRecord(0);
        assert TestUtils.parseVgSales(record);

    }

    @Test
    public void getRecordPortalData() {
        System.out.println("-----------------------");
        System.out.println("Get record with portal data");
        System.out.println("-----------------------");
        FmPortal fmPortal = new FmPortal()
                .setName(LAYOUT_VGSALES).setLimit(3).setOffset(1)
                .setName(LAYOUT_PUBLISHERS).setLimit(3).setOffset(1);
        FmRequest request = new FmRequest()
                .getRecords(ENDPOINT, token, LAYOUT_GENRES)
                .setLimit(100)
                .setOffset(1)
                .setPortalParams(fmPortal)
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
        FmData fmData = new FmData(response);
        int max = fmData.size() - 1;
        int randomNumber = ThreadLocalRandom.current().nextInt(0, max);
        System.out.println("Record #: " + randomNumber);
        FmRecord record = fmData.getRecord(randomNumber);
        randomNumber = ThreadLocalRandom.current().nextInt(0, record.portalSize(LAYOUT_VGSALES)-1);
        FmRecord portalRecord = record.getPortalRecord(LAYOUT_VGSALES, randomNumber);
        System.out.println("Portal record #: " + randomNumber);
        assert TestUtils.parseVgSales(portalRecord);
    }

    @Test
    public void getSortedRecords() {
        System.out.println("-----------------------");
        System.out.println("Get sorted records");
        System.out.println("-----------------------");
        FmSort fmSort = new FmSort()
                .sortAsc("Genre")
                .sortDesc("Rank");
        FmRequest request = new FmRequest()
                .getRecords(ENDPOINT, token, LAYOUT_VGSALES)
                .setLimit(1000)
                .setOffset(1)
                .setSortParams(fmSort)
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
        FmData fmData = new FmData(response);
        int max = fmData.size();
        FmRecord firstRecord = fmData.getRecord(0);
        int rank1 = Integer.valueOf(firstRecord.getValue("Rank"));
        System.out.println("First record rank: " + rank1);
        FmRecord lastRecord = fmData.getRecord(max - 1);
        int rank2 = Integer.valueOf(lastRecord.getValue("Rank"));
        System.out.println("Last record rank: " + rank2);
        assert rank1 > rank2;
    }

    @Test
    public void getRecordsRunScript() {
        System.out.println("-----------------------");
        System.out.println("Get records and run script");
        System.out.println("-----------------------");
        FmScript script = new FmScript()
                .setScript("log").setScriptParam("Hello from script")
                .setPreRequest("log").setPreRequestParam("Hello from pre-request script")
                .setPreSort("log").setPreSortParam("Hello from pre-sort script")
                .setLayoutReponse(LAYOUT_VGSALES);
        FmRequest request = new FmRequest()
                .getRecords(ENDPOINT, token, LAYOUT_VGSALES)
                .setLimit(10)
                .setOffset(1)
                .setScriptPrams(script)
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
        int scriptError = response.getScriptError();
        String scriptResult = response.getScriptResult();
        int scriptErrorPreRequest = response.getPreRequestScriptError();
        String scriptResultPreRequest = response.getPreRequestScriptResult();
        int scriptErrorPreSort = response.getPreSortScriptError();
        String scriptResultPreSort = response.getPreSortScriptResult();
        System.out.println("scriptResult: " + scriptResult);
        System.out.println("scriptResultPreRequest: " + scriptResultPreRequest);
        System.out.println("scriptResultPreSort: " + scriptResultPreSort);
        assert scriptError == 0
                && scriptResult.equals("Ok")
                && scriptErrorPreRequest == 0
                && scriptResultPreRequest.equals("Ok")
                && scriptErrorPreSort == 0
                && scriptResultPreSort.equals("Ok");
    }

    /**
     * testInvalidGetRecord
     * Test all the different cases in which the FmRequest.build() method on getRecord() fails.
     */
    @Test
    public void testInvalidGetRecord() {
        System.out.println("-----------------------");
        System.out.println("Test invalid get record");
        System.out.println("-----------------------");
        FmRequest request;
        request = new FmRequest().getRecord(null, token, LAYOUT_VGSALES, 1).build();
        System.out.println("Error: " + request.getMessage());
        assert !request.isOk();
        request = new FmRequest().getRecord("", token, LAYOUT_VGSALES, 1).build();
        System.out.println("Error: " + request.getMessage());
        assert !request.isOk();
        request = new FmRequest().getRecord(ENDPOINT, null, LAYOUT_VGSALES, 1).build();
        System.out.println("Error: " + request.getMessage());
        assert !request.isOk();
        request = new FmRequest().getRecord(ENDPOINT, "", LAYOUT_VGSALES, 1).build();
        System.out.println("Error: " + request.getMessage());
        assert !request.isOk();
        request = new FmRequest().getRecord(ENDPOINT, token, null, 1).build();
        System.out.println("Error: " + request.getMessage());
        assert !request.isOk();
        request = new FmRequest().getRecord(ENDPOINT, token, "", 1).build();
        System.out.println("Error: " + request.getMessage());
        assert !request.isOk();
        request = new FmRequest().getRecord(ENDPOINT, token, LAYOUT_VGSALES, 0).build();
        System.out.println("Error: " + request.getMessage());
        assert !request.isOk();
        request = new FmRequest().getRecord(ENDPOINT, token, LAYOUT_VGSALES, -1).build();
        System.out.println("Error: " + request.getMessage());
        assert !request.isOk();

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