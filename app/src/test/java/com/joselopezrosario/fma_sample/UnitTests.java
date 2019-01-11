package com.joselopezrosario.fma_sample;

import com.joselopezrosario.fma.Fma;
import com.joselopezrosario.fma.FmaData;
import com.joselopezrosario.fma.FmaParamPortal;
import com.joselopezrosario.fma.FmaParamScript;
import com.joselopezrosario.fma.FmaRecord;
import com.joselopezrosario.fma.FmaRequest;
import com.joselopezrosario.fma.FmaResponse;
import com.joselopezrosario.fma.FmaParamSort;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class UnitTests {
    private static final String ENDPOINT = BuildConfig.API_HOST;
    private static final String ACCOUNT = BuildConfig.API_ACCOUNT;
    private static final String PASSWORD = BuildConfig.API_PASSWORD;
    private static final String LAYOUT_VGSALES = "vgsales";
    private static final String LAYOUT_GENRES = "genres";
    private static final String LAYOUT_PUBLISHERS = "publishers";
    private static String token;

    @BeforeClass
    public static void login() {
        FmaRequest request = new FmaRequest()
                .login(ENDPOINT, ACCOUNT, PASSWORD)
                .disableSSL(true)
                .build();
        if (!request.isOk()) {
            assert false;
            return;
        }
        FmaResponse response = Fma.execute(request);
        if (!response.isOk()) {
            assert false;
            return;
        }
        token = response.getToken();

    }

    @Test
    public void validateToken() {
        assert token != null;
    }

    @Test
    public void getRecords() {
        FmaRequest request = new FmaRequest()
                .getRecords(ENDPOINT, token, LAYOUT_VGSALES)
                .setLimit(1)
                .setOffset(1)
                .disableSSL(true)
                .build();
        if (!request.isOk()) {
            assert false;
            return;
        }
        FmaResponse response = Fma.execute(request);
        if (!response.isOk()) {
            assert false;
            return;
        }

        FmaData data = new FmaData().create(response);
        FmaRecord record = data.getRecord(0);
        String recordId = record.getRecordId();
        String modId = record.getModId();
        String rank = record.getValue("Rank");
        String name = record.getValue("Name");
        String platform = record.getValue("Platform");
        String publisher = record.getValue("Publisher");
        assert recordId != null;
        assert modId != null;
        assert rank != null;
        assert name != null;
        assert platform != null;
        assert publisher != null;

    }

    @Test
    public void getPortalData() {
        FmaParamPortal portalVgSales = new FmaParamPortal(LAYOUT_VGSALES, 3, 1);
        FmaParamPortal portalPublishers = new FmaParamPortal(LAYOUT_PUBLISHERS, 3, 1);
        ArrayList<FmaParamPortal> fmaParamPortalArrayList = new ArrayList<>();
        fmaParamPortalArrayList.add(portalVgSales);
        fmaParamPortalArrayList.add(portalPublishers);
        FmaRequest request = new FmaRequest()
                .getRecords(ENDPOINT, token, LAYOUT_GENRES)
                .setLimit(10)
                .setOffset(1)
                .setPortalParams(fmaParamPortalArrayList)
                .disableSSL(true)
                .build();
        if (!request.isOk()) {
            assert false;
            return;
        }
        FmaResponse response = Fma.execute(request);
        if (!response.isOk()) {
            assert false;
            return;
        }
        FmaData data = new FmaData().create(response);
        FmaRecord record = data.getRecord(0);
        FmaRecord portalRecord = record.getPortalRecord(LAYOUT_VGSALES, 0);
        assert parseVgSales(portalRecord);
    }

    @Test
    public void getSortedRecords() {
        FmaParamSort sortByGenre = new FmaParamSort("Genre");
        FmaParamSort sortByRank = new FmaParamSort("Rank").descend();
        ArrayList<FmaParamSort> sortParams = new ArrayList<>();
        sortParams.add(sortByGenre);
        sortParams.add(sortByRank);
        FmaRequest request = new FmaRequest()
                .getRecords(ENDPOINT, token, LAYOUT_VGSALES)
                .setLimit(1000)
                .setOffset(1)
                .setSortParams(sortParams)
                .disableSSL(true)
                .build();
        if (!request.isOk()) {
            assert false;
            return;
        }
        FmaResponse response = Fma.execute(request);
        if (!response.isOk()) {
            assert false;
            return;
        }
        FmaData data = new FmaData().create(response);
        FmaRecord record = data.getRecord(0);
        assert parseVgSales(record);
    }

    @Test
    public void getRecordsRunScript() {
        FmaParamScript script = new FmaParamScript()
                .setScript("log").setScriptParam("Hello from script")
                .setPreRequest("log").setPreRequestParam("Hello from pre-request script")
                .setPreSort("log").setPreSortParam("Hello from pre-sort script")
                .setLayoutReponse(LAYOUT_VGSALES);
        FmaRequest request = new FmaRequest()
                .getRecords(ENDPOINT, token, LAYOUT_VGSALES)
                .setLimit(10)
                .setOffset(1)
                .setScriptPrams(script)
                .disableSSL(true)
                .build();
        if (!request.isOk()) {
            assert false;
            return;
        }
        FmaResponse response = Fma.execute(request);
        if (!response.isOk()) {
            assert false;
            return;
        }
        // Get the script error and result responses
        int scriptError = response.getScriptError();
        String scriptResult = response.getScriptResult();
        int scriptErrorPreRequest = response.getScriptErrorPreRequest();
        String scriptResultPreRequest = response.getScriptResultPreRequest();
        int scriptErrorPreSort = response.getScriptErrorPreSort();
        String scriptResultPreSort = response.getScriptResultPreSort();

        FmaData data = new FmaData().create(response);
        FmaRecord record = data.getRecord(0);
        boolean parsed = parseVgSales(record);

        assert parsed
                && scriptError == 0
                && scriptResult.equals("Ok")
                && scriptErrorPreRequest == 0
                && scriptResultPreRequest.equals("Ok")
                && scriptErrorPreSort == 0
                && scriptResultPreSort.equals("Ok");
    }

    @AfterClass
    public static void logout() {
        FmaRequest request = new FmaRequest()
                .logout(ENDPOINT, token)
                .disableSSL(true)
                .build();
        if (!request.isOk()) {
            assert false;
            return;
        }
        FmaResponse response = Fma.execute(request);
        if (!response.isOk()) {
            assert false;
            return;
        }
        assert response.getHttpCode() == 200;

    }

    private static boolean parseVgSales(FmaRecord record) {
        String recordId = record.getRecordId();
        String modId = record.getModId();
        String rank = record.getValue("Rank");
        String name = record.getValue("Name");
        String platform = record.getValue("Platform");
        String publisher = record.getValue("Publisher");
        if (recordId == null) {
            return false;
        } else if (modId == null) {
            return false;
        } else if (rank == null) {
            return false;
        } else if (name == null) {
            return false;
        } else if (platform == null) {
            return false;
        } else if (publisher == null) {
            return false;
        } else {
            return true;
        }

    }
}