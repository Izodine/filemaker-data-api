# FileMaker Data API (Fma)

[![Build Status](https://travis-ci.org/joselopezrosario/filemaker-data-api.svg?branch=master)](https://travis-ci.org/joselopezrosario/filemaker-data-api)

An Android library to seamlessly interact with databases hosted on FileMaker Server 17+.

## Classes

* FmaRequest: create an API request via instance methods
* Fma: execute the API request against the FileMaker Data API
* FmaResponse: retrieve the result from the FileMaker Data API
* FmaData: parse records from the results
* FmaRecord: parse a record's field and portal data

## Methods

### FmaRequest.login()

To use the FileMaker Data API, you must log in with an account that has the fmrest extended privilege enabled so you can receive a session token. All subsequent calls will require this token.

 ```java
    /**
     * login
     * @param endpoint the FileMaker Data API endpoint (ex: https://host/fmi/data/v1/databases/MyDatabase)
     * @param account the FileMaker account with fmrest extended privileges
     * @param password the FileMaker account's password
     * @return an FmaRequest object
     */
    public FmaRequest login(String endpoint, String account, String password)
```
**Usage:**
```java
        FmaRequest request = new FmaRequest()
                .login(URL, ACCOUNT, PASSWORD)
                .build();
        FmaResponse response = Fma.execute(request);
        token = response.getToken();
```

### FmaRequest.logout()

Logout from the FileMaker session and release the connection.

```java
    /**
     * logout
     * @param endpoint the FileMaker Data API endpoint (ex: https://host/fmi/data/v1/databases/MyDatabase)
     * @param token the token returned in the response by executing the login request
     * @return an FmaRequest object
     */
    public FmaRequest logout(String endpoint, String token)
```    
**Usage:**
```java
        FmaRequest request = new FmaRequest()
                .logout(URL, token)
                .build();
        FmaResponse response = Fma.execute(request);
```
### FmaRequest.getRecords()

Get a foundset of records.

```java
   /**
     * getRecords
     *
     * @param endpoint the FileMaker Data API endpoint (ex: https://host/fmi/data/v1/databases/MyDatabase)
     * @param token    the token returned in the response by executing the login request
     * @param layout   the layout from where to retrieve the records
     * @return an FmaRequest object
     */
    public FmaRequest getRecords(String endpoint, String token, String layout)
```
**Usage:**
Get the first 20 records from the vgsales layout.
```java
        FmaRequest request = new FmaRequest()
                .getRecords(ENDPOINT, token, LAYOUT_VGSALES)
                .setLimit(20)
                .setOffset(1)
                .build();
        FmaResponse response = Fma.execute(request);
```

Specifying the portal parametes. The genres layout has a portal to vgsales. Get the first genre record and the top 20 vgsales records.
```java
        ArrayList<Portal> portalParams = new ArrayList<>();
        Portal portal = new Portal();
        portal.setName(LAYOUT_VGSALES);
        portal.setLimit("20");
        portal.setOffset("1");
        portalParams.add(portal);
        FmaRequest request = new FmaRequest()
                .getRecords(ENDPOINT, token, LAYOUT_GENRES)
                .setLimit(1)
                .setOffset(1)
                .setPortalParams(portalParams)
                .build();
        FmaResponse response = Fma.execute(request);
```

## License

This project is licensed under the "GNU LGPLv3" License.
