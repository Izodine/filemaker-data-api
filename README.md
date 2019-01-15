# Android-Fm

[![Build Status](https://travis-ci.com/joselopezrosario/filemaker-data-api.svg?branch=master)](https://travis-ci.com/joselopezrosario/filemaker-data-api)

An Android library to seamlessly interact with databases hosted on FileMaker Server 17+ through the Data API.
___
# Classes

* FmRequest: create an API request via instance methods
* Fm: execute the API request against the FileMaker Data API
    * FmSort: build the sort parameters
    * FmPortal: build the portal parameters
    * FmScript: build the script parameters
    * FmFind: build find requests
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
   
**Usage:**
```java
    FmRequest request = new FmRequest()
        .logout(URL, token)
        .build();
```
### ***FmRequest.getRecords()***

Get a foundset of records.

**Usage:**
Get the first 20 records from the vgsales layout.
```java
    FmRequest request = new FmRequest()
        .getRecords(ENDPOINT, token, LAYOUT_VGSALES)
        .setLimit(20)
        .setOffset(1)
        .build();
```
### ***FmRequest.findRecords()***

Find records through one or many find criteria.

***To build find requests***
The FileMaker Data API allows you to send multiple find requests in one query. The FmFind class allows you to easily build a complex query.

FmFind has the following methods that you must call in order.

1. `newFindRequest()` to start a new find request
2. `set()` to set the field name value pairs (you can call this method multiple times)
3. `omit()` to omit the records (optional: defaults to false)

You can chain the methods to create multiple find requests at once.

For example:

```java
// Create a new FmFind object
    FmFind findGames = new FmFind();
        findGames
            .newRequest().set("Publisher", "Nintendo").set("Year", "1985")
            .newRequest().set("Publisher", "Sega").set("Year", "1991...1996")
            .newRequest().set("Publisher", "Sega").set("Year", "1994").omit();
```
Then pass the FmFind object to the FmRequest.findRecords() method.
```java
    FmRequest request = new FmRequest()
        .findRecords(ENDPOINT, token, LAYOUT_VGSALES, findGames)
        .build();
```
#### ***FmRequest.create()***
Create a record. 

***To set the initial values of a new record***
Use the FmEdit class to create a new object and set the field name value pairs through the 'set()' method.

For example:

```java
 FmEdit edit = new FmEdit();
    .set("Rank", "999732")
    .set("Name", "Jose's game")
    .set("Publisher", "Lorem ipsum")
    .set("Genre", "Arcade")
    .set("Platform", "Nes")
    .set("Year", "1981");
```
Then, pass the FmEdit object to the FmRequest.create() method.

```java
 FmRequest request = new FmRequest()
    .create(ENDPOINT, token, LAYOUT_VGSALES, edit)
    .build();
---
# Optional Parameter Classes
Use these classes to build the optional request parameters.
## FmPortal
***To get related records***
This example shows how to get the first 10 records from the genres layout, and for each genre record, to get the first 3 related vgsales records, and the first 3 related publisher records.

You will set the portal name, limit, and offset via FmPortal objects. Create them using this constructor: `FmPortal(String name, int limit, int offset)`

Then add all your FmPortal objects to an `ArrayList()`.

For example:
```java
    FmPortal fmPortal = new FmPortal()
        .set(LAYOUT_VGSALES).setLimit(3).setOffset(1)
        .set(LAYOUT_PUBLISHERS).setLimit(3).setOffset(1);
```

Then, pass the ArrayList to the FmRequest object through the `setPortalParams()` method.

```java
    FmRequest request = new FmRequest()
        .getRecords(ENDPOINT, token, LAYOUT_GENRES)
        .setPortalParams(fmaParamPortalArrayList)
        .build();
```
## FmSort

***To sort records***
The FileMaker Data API allows you to sort records by multiple fields in ascending or descending order.

This example shows you how to get the first 1,000 records from the vgsales layout and sort them by genre ascending and rank descending. 

First, build an FmSort object and call the `sortAsc()` and `sortDesc()` methods with the names of the fields you want to sort by.

```java
    FmSort fmSort = new FmSort()
        .sortAsc("Genre")
        .sortDesc("Rank");
```
Then, pass it to the FmRequest object.

```java    
    FmRequest request = new FmRequest()
        .getRecords(ENDPOINT, token, LAYOUT_VGSALES)
        .setLimit(1000)
        .setOffset(1)
        .setSortParams(fmSort)
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