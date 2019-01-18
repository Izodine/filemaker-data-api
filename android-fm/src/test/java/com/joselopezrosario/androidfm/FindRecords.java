package com.joselopezrosario.androidfm;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.ThreadLocalRandom;

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
        FmResponse loginResponse = Fm.execute(loginRequest);
        if (!loginResponse.isOk()) {
            assert false;
            return;
        }
        token = loginResponse.getToken();
    }

    /**
     * findRecords
     */
    @Test
    public void findRecords() {
        System.out.println("-----------------------");
        System.out.println("Find Records");
        System.out.println("-----------------------");
        FmFind findGames = new FmFind()
                .newRequest().set("Publisher", "Nintendo").set("Year", "1985")
                .newRequest().set("Publisher", "Sega").set("Year", "1991...1996")
                .newRequest().set("Publisher", "Sega").set("Year", "1994").omit();
        FmRequest request = new FmRequest()
                .findRecords(ENDPOINT, token, LAYOUT_VGSALES, findGames)
                .build();
        FmResponse response = Fm.execute(request);
        FmData fmData = new FmData(response);
        if (!response.isOk()) {
            assert false;
            return;
        }
        int foundcount = fmData.size();
        System.out.println("Foundcount: " + foundcount);
        assert findGames.countQueries() == 6 && fmData.size() > 0;


    }

    /**
     * findRecordWithPortalData
     */
    @Test
    public void findRecordWithPortalData() {
        System.out.println("-----------------------");
        System.out.println("Find records with portal data");
        System.out.println("-----------------------");
        FmFind findGames = new FmFind();
        findGames.newRequest().set("Genre", "Platform");
        findGames.newRequest().set("Genre", "Adventure");
        findGames.newRequest().set("Genre", "Puzzle");
        findGames.newRequest().set("Genre", "Simulation");
        findGames.newRequest().set("Genre", "Shooter");
        FmPortal fmPortal = new FmPortal()
                .setName(LAYOUT_VGSALES).setLimit(5000).setOffset(1);
        FmRequest request = new FmRequest()
                .findRecords(ENDPOINT, token, LAYOUT_GENRES, findGames)
                .setPortalParams(fmPortal)
                .build();
        FmResponse response = Fm.execute(request);
        if (!response.isOk()) {
            assert false;
            return;
        }
        FmData fmData = new FmData(response);
        int foundcount = fmData.size();
        System.out.println("Foundcount: " + foundcount);
        int randomNumber = ThreadLocalRandom.current().nextInt(0, foundcount - 1);
        System.out.println("Get record #: " + randomNumber);
        FmRecord record = fmData.getRecord(randomNumber);
        int portalSize = record.portalSize(LAYOUT_VGSALES);
        System.out.println("Record's portal size : " + portalSize);
        randomNumber = ThreadLocalRandom.current().nextInt(0, portalSize - 1);
        System.out.println("Get portal record #: " + randomNumber);
        FmRecord portalRecord = record.getPortalRecord(LAYOUT_VGSALES, randomNumber);
        assert TestUtils.parseVgSales(portalRecord);
    }

    @AfterClass
    public static void logout() {
        FmRequest request = new FmRequest()
                .logout(ENDPOINT, token)
                .build();
        FmResponse response = Fm.execute(request);
        if (!response.isOk()) {
            assert false;
            return;
        }
        assert response.getHttpCode() == 200;

    }
}