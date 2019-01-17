package com.joselopezrosario.androidfm;

class TestUtils {
    //TODO: Create util methods for login and logout

    /**
     * parseVgSales
     * @param record
     * @return
     */
    static boolean parseVgSales(FmRecord record) {
        int recId = record.getRecordId();
        int modId = record.getModId();
        String rank = record.getValue("Rank");
        String name = record.getValue("Name");
        String platform = record.getValue("Platform");
        String publisher = record.getValue("Publisher");
        String genre = record.getValue("Genre");
        String year = record.getValue("Year");
        System.out.println("---PARSE RECORD");
        System.out.println("recordId: " + recId);
        System.out.println("modId: " + modId);
        System.out.println("rank: " + rank);
        System.out.println("name: " + name);
        System.out.println("platform: " + platform);
        System.out.println("publisher: " + publisher);
        System.out.println("genre: " + genre);
        System.out.println("year: " + year);
        return recId != 0 &&
                modId != 0 &&
                rank != null &&
                name != null &&
                platform != null &&
                publisher != null &&
                genre != null &&
                year != null;

    }
}
