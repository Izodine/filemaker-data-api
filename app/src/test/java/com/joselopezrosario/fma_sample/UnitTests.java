package com.joselopezrosario.fma_sample;

import com.joselopezrosario.androidfm.Fm;
import com.joselopezrosario.androidfm.FmData;
import com.joselopezrosario.androidfm.FmPortal;
import com.joselopezrosario.androidfm.FmScript;
import com.joselopezrosario.androidfm.FmQuery;
import com.joselopezrosario.androidfm.FmRequest;
import com.joselopezrosario.androidfm.FmResponse;
import com.joselopezrosario.androidfm.FmRecord;
import com.joselopezrosario.androidfm.FmSort;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

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
        FmRequest request = new FmRequest()
                .login(ENDPOINT, ACCOUNT, PASSWORD)
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
        token = response.getToken();

    }

    @Test
    public void validateToken() {
        assert token != null;
    }

    @Test
    public void getRecords() {
        FmRequest request = new FmRequest()
                .getRecords(ENDPOINT, token, LAYOUT_VGSALES)
                .setLimit(1)
                .setOffset(1)
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

        FmData data = new FmData().create(response);
        FmRecord record = data.getRecord(0);
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
        FmPortal fmPortal = new FmPortal()
                .set(LAYOUT_VGSALES).setLimit(3).setOffset(1)
                .set(LAYOUT_PUBLISHERS).setLimit(3).setOffset(1);
        FmRequest request = new FmRequest()
                .getRecords(ENDPOINT, token, LAYOUT_GENRES)
                .setLimit(10)
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
        FmData data = new FmData().create(response);
        FmRecord record = data.getRecord(0);
        FmRecord portalRecord = record.getPortalRecord(LAYOUT_VGSALES, 0);
        assert parseVgSales(portalRecord);
    }

    @Test
    public void getSortedRecords() {
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
        FmData data = new FmData().create(response);
        FmRecord record = data.getRecord(0);
        assert parseVgSales(record);
    }

    @Test
    public void getRecordsRunScript() {
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
        // Get the script error and result responses
        int scriptError = response.getScriptError();
        String scriptResult = response.getScriptResult();
        int scriptErrorPreRequest = response.getScriptErrorPreRequest();
        String scriptResultPreRequest = response.getScriptResultPreRequest();
        int scriptErrorPreSort = response.getScriptErrorPreSort();
        String scriptResultPreSort = response.getScriptResultPreSort();

        FmData data = new FmData().create(response);
        FmRecord record = data.getRecord(0);
        boolean parsed = parseVgSales(record);

        assert parsed
                && scriptError == 0
                && scriptResult.equals("Ok")
                && scriptErrorPreRequest == 0
                && scriptResultPreRequest.equals("Ok")
                && scriptErrorPreSort == 0
                && scriptResultPreSort.equals("Ok");
    }

    @Test
    // TODO: Complete the FmaRequest.findRecords() method and complete this test
    public void findRecords(){
        FmQuery findGames = new FmQuery();
        findGames.newFindRequest().set("Publisher","Nintendo").set("Year","1985");
        findGames.newFindRequest().set("Publisher","Sega").set("Year","1991...1996");
        findGames.newFindRequest().set("Publisher","Sega").set("Year","1994").omit();
        FmRequest request = new FmRequest()
                .findRecords(ENDPOINT, token, LAYOUT_VGSALES, findGames)
                .build();
        System.out.println(findGames.countFindRequests());
        System.out.println(findGames.countQueries());
        assert findGames.countQueries() == 6;

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

    private static boolean parseVgSales(FmRecord record) {
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