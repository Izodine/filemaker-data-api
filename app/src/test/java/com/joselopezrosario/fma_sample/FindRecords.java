package com.joselopezrosario.fma_sample;


import com.joselopezrosario.androidfm.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class FindRecords {
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
    // TODO: Complete the FmaRequest.findRecords() method and complete this test
    public void findRecords(){
        FmFind findGames = new FmFind();
        findGames.newRequest().set("Publisher","Nintendo").set("Year","1985");
        findGames.newRequest().set("Publisher","Sega").set("Year","1991...1996");
        findGames.newRequest().set("Publisher","Sega").set("Year","1994").omit();
        FmRequest request = new FmRequest()
                .findRecords(ENDPOINT, token, LAYOUT_VGSALES, findGames)
                .build();
        System.out.println(findGames.countFindRequests());
        System.out.println(findGames.countQueries());
        FmResponse response = Fm.execute(request);
        FmData fmData = new FmData().create(response);
        assert findGames.countQueries() == 6 && fmData.size() > 0;


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