# Fm Library

[![Build Status](https://travis-ci.com/joselopezrosario/filemaker-data-api.svg?branch=master)](https://travis-ci.com/joselopezrosario/filemaker-data-api)

An Android library to seamlessly interact with databases hosted on FileMaker Server 17+ through the Data API.
___
# Classes

* FmRequest: create an API request via instance methods
* Fm: execute the API request against the FileMaker Data API
    * FmSort: build the sort parameters
    * FmPortal: build the portal parameters
    * FmScript: build the script parameters
* FmResponse: retrieve the result from the FileMaker Data API
* FmData: parse records from the results
* FmRecord: parse a record's field and portal data
---
## FmRequest
Use this class to create a request object that contains:

* The information required by the API - like the endpoint url, credentials, and body
* Other optional information - like the sort, portal, and script parameters

### Methods
#### ***FmRequest.login()***

To use the FileMaker Data API, you must log in with an account that has the fmrest extended privilege enabled so you can receive a session token. All subsequent calls will require this token.

 ```java
    /**
     * login
     * @param endpoint the FileMaker Data API endpoint (ex: https://host/fmi/data/v1/databases/MyDatabase)
     * @param account the FileMaker account with fmrest extended privileges
     * @param password the FileMaker account's password
     * @return an FmRequest object
     */
    public FmRequest login(String endpoint, String account, String password)
```
**Usage:**
```java
    FmRequest request = new FmRequest()
            .login(URL, ACCOUNT, PASSWORD)
            .build();
            
    // Execute the request and get the token
    FmResponse response = Fm.execute(request);
    String token = response.getToken();
```

### ***FmRequest.logout()***

Logout from the FileMaker session and release the connection.

```java
    /**
     * logout
     * @param endpoint the FileMaker Data API endpoint (ex: https://host/fmi/data/v1/databases/MyDatabase)
     * @param token the token returned in the response by executing the login request
     * @return an FmRequest object
     */
    public FmRequest logout(String endpoint, String token)
```    
**Usage:**
```java
    FmRequest request = new FmRequest()
        .logout(URL, token)
        .build();
```
### ***FmRequest.getRecords()***

Get a foundset of records.

```java
   /**
     * getRecords
     *
     * @param endpoint the FileMaker Data API endpoint (ex: https://host/fmi/data/v1/databases/MyDatabase)
     * @param token    the token returned in the response by executing the login request
     * @param layout   the layout from where to retrieve the records
     * @return an FmRequest object
     */
    public FmRequest getRecords(String endpoint, String token, String layout)
```
**Usage:**
Get the first 20 records from the vgsales layout.
```java
    FmRequest request = new FmRequest()
        .getRecords(ENDPOINT, token, LAYOUT_VGSALES)
        .setLimit(20)
        .setOffset(1)
        .build();
```

---
# Optional Classes
Use these classes to build the optional request parameters.
## FmPortal
***To get related records***
This example shows how to get the first 10 records from the genres layout, and for each genre record, to get the first 3 related vgsales records, and the first 3 related publisher records.

You will set the portal name, limit, and offset via FmPortal objects. Create them using this constructor: `FmPortal(String name, int limit, int offset)`

Then add all your FmPortal objects to an `ArrayList()`.

For example:
```java
    FmPortal portalVgSales = new FmPortal(LAYOUT_VGSALES, 3, 1);
    FmPortal portalPublishers = new FmPortal(LAYOUT_PUBLISHERS, 3, 1);
    ArrayList<FmPortal> fmaParamPortalArrayList = new ArrayList<>();
    fmaParamPortalArrayList.add(portalVgSales);
    fmaParamPortalArrayList.add(portalPublishers);
```

Then, pass the ArrayList to the FmRequest object through the `setPortalParams()` method.

```java
    FmRequest request = new FmRequest()
        .getRecords(ENDPOINT, token, LAYOUT_GENRES)
        .setLimit(10)
        .setOffset(1)
        .setPortalParams(fmaParamPortalArrayList)
        .build();
```
## FmSort

***To sort records***
This example shows how to get the first 1,000 records from the vgsales layout, and to sort them by genre ascending and rank descending. 

You will set the field name and the sort order via FmSort objects. Create them using this constructor: `FmSort(String name)`. And then set the sort order by calling the `ascend()` or `descend()` method. 

For example: `new FmSort("Rank").descend()` to sort by rank descending. 

Finally, add all objects to an `ArrayList()`.

Here is a complete example:

```java
    // Create the FmSort objects
    FmSort sortByGenre = new FmSort("Genre"); // note: sort order defaults to ascend
    FmSort sortByRank = new FmSort("Rank").descend();
       
    // Add them to an ArrayList()
    ArrayList<FmSort> sortParams = new ArrayList<>();
    sortParams.add(sortByGenre);
    sortParams.add(sortByRank);
       
    // Create a new FmRequest and pass the sort parameters through the setSortParams() method
    FmRequest request = new FmRequest()
        .getRecords(ENDPOINT, token, LAYOUT_VGSALES)
        .setLimit(1000)
        .setOffset(1)
        .setSortParams(sortParams) // set the sort parameters
        .build();        
```
## FmScript

***To call scripts***
This example shows how to run scripts. The FileMaker Data API has three options for running scripts.

1. **script**: runs after the action and sort are executed
2. **script pre-request**: runs before the action and sort are executed
3. **script pre-sort**: runs after executing the request but before sorting the records

You can set these options through an FmScript object. Create the object using this constructor `FmScript()`. 

Set the options using the following methods:

* `setScript(String script)` - sets the script name
* `setScriptParam(String scriptParam)` - sets the script parameter
* `setPreRequest(String preRequest)` - sets the pre-request script name
* `setPreRequestParam(String preRequestParam)` - sets the pre-request script paramter
* `setPreSort(String preSort)` - sets the pre-sort script name
* `setPreSortParam(String preSortParam)` - sets the pre-sort script paramter

For example, the VideoGameSales FileMaker databases has a `log` script that goes to the `log` layout, creates a new record, and sets the `Message` field with the value of the script parameter. We can create an FmScript object to run the `log` script using the three options.

```java
    FmScript script = new FmScript()
        .setScript("log").setScriptParam("Hello from script")
        .setPreRequest("log").setPreRequestParam("Hello from pre-request script")
        .setPreSort("log").setPreSortParam("Hello from pre-sort script")
        .setLayoutReponse(LAYOUT_VGSALES);
```
And then pass it to the FmRequest object through the `setScriptParams()` method.
```java
    FmRequest request = new FmRequest()
        .getRecords(ENDPOINT, token, LAYOUT_VGSALES)
        .setLimit(10)
        .setOffset(1)
        .setScriptPrams(script)
        .build();
```
After executing the request, the script creates the following records in the `log` table.
```
CreationTimestamp	    Message
01/11/2019 13:44:29	    Hello from pre-request-script
01/11/2019 13:44:29	    Hello from pre-sort-script
01/11/2019 13:44:29	    Hello from script
```
## FmQuery

1/12/2019: This feature is not complete!
https://github.com/joselopezrosario/filemaker-data-api/issues/2

***To build find requests***
The FileMaker Data API allows you to send multiple find requests in one query. This class allows you to easily create one or many find requests.

FmQuery is composed of the following methods.

`newFindRequest()` to start a new find request
`set()` to set the field name value pairs
`omit()` to omit the records (defaults to false)

**Usage:**
The following query finds all the games published by Nintendo in 1985, all the games published by Sega between 1991 and 1996, but it omits the games published by Sega in 1994.

```java
    FmQuery findGames = new FmQuery();
    findGames.newFindRequest()
        .set("Publisher","Nintendo")
        .set("Year","1985");
    findGames.newFindRequest()
        .set("Publisher","Sega")
        .set("Year","1991...1996");
    findGames.newFindRequest()
        .set("Publisher","Sega")
        .set("Year","1994")
        .omit();
```
You can then pass the FmQuery object to the FmRequest.findRecords() method.
```java
    FmRequest request = new FmRequest()
        .findRecords(ENDPOINT, token, LAYOUT_VGSALES, findGames)
        .build();
```
---
# Testing

To run the unit tests, modify the the gradle.properties file to include your own values:

```groovy
API_HOST="https://yourhost/fmi/data/v1/databases/VideoGameSales"
API_ACCOUNT="APIUser"
API_PASSWORD="syaP.F;9'b+F#q#Y"
```

If using [TravisCI](https://travis-ci.org/), also modify the travis.yml file to include your environment variables:

```yml
env:
  global:
    - API_HOST="https://yourhost/fmi/data/v1/databases/VideoGameSales"
    - API_ACCOUNT="APIUser"
    - API_PASSWORD="syaP.F;9'b+F#q#Y"
```
---
# License

This project is licensed under the "GNU LGPLv3" License.