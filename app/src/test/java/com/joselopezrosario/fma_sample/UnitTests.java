package com.joselopezrosario.fma_sample;

import com.joselopezrosario.fma.Fma;
import com.joselopezrosario.fma.FmaData;
import com.joselopezrosario.fma.FmaPortalParams;
import com.joselopezrosario.fma.FmaRecord;
import com.joselopezrosario.fma.FmaRequest;
import com.joselopezrosario.fma.FmaResponse;
import com.joselopezrosario.fma.FmaSortParams;

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
    private static final String ACCOUNT =  BuildConfig.API_ACCOUNT;
    private static final String PASSWORD = BuildConfig.API_PASSWORD;
    private static final String LAYOUT_VGSALES = "vgsales";
    private static final String LAYOUT_GENRES = "genres";
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
        ArrayList<FmaPortalParams> fmaPortalParamsArrayList = new ArrayList<>();
        FmaPortalParams fmaPortal = new FmaPortalParams();
        fmaPortal.setName(LAYOUT_VGSALES);
        fmaPortal.setLimit("1");
        fmaPortal.setOffset("1");
        fmaPortalParamsArrayList.add(fmaPortal);
        FmaRequest request = new FmaRequest()
                .getRecords(ENDPOINT, token, LAYOUT_GENRES)
                .setLimit(1)
                .setOffset(1)
                .setFmaPortalParamsParams(fmaPortalParamsArrayList)
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
        String recordId = portalRecord.getRecordId();
        String modId = portalRecord.getModId();
        String rank = portalRecord.getValue("Rank");
        String name = portalRecord.getValue("Name");
        String platform = portalRecord.getValue("Platform");
        String publisher = portalRecord.getValue("Publisher");
        assert recordId != null;
        assert modId != null;
        assert rank != null;
        assert name != null;
        assert platform != null;
        assert publisher != null;
    }

    @Test
    public void getSortedRecords(){
        ArrayList<FmaSortParams> sortParams = new ArrayList<>();
        FmaSortParams sortByRank = new FmaSortParams();
        sortByRank.setFieldName("Rank");
        sortByRank.setSortOrder("descend");
        sortParams.add(sortByRank);
        FmaRequest request = new FmaRequest()
                .getRecords(ENDPOINT, token, LAYOUT_VGSALES)
                .setLimit(20)
                .setOffset(1)
                .setFmaSortParamsParams(sortParams)
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
}