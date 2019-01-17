# Android-Fm

[![HitCount](http://hits.dwyl.io/joselopezrosario/filemaker-data-api.svg)](http://hits.dwyl.io/joselopezrosario/filemaker-data-api) [![Build Status](https://travis-ci.com/joselopezrosario/filemaker-data-api.svg?branch=master)](https://travis-ci.com/joselopezrosario/filemaker-data-api) [![Download](https://api.bintray.com/packages/joselopezrosario/maven/android-fm/images/download.svg)](https://bintray.com/joselopezrosario/maven/android-fm/_latestVersion)

An Android library to seamlessly interact with databases hosted on FileMaker Server 17+ through the Data API.

___
# Installation

Add the repository to your project's build.gradle file.

```groovy
repositories {
    maven {
        url  "https://dl.bintray.com/joselopezrosario/maven"
    }
}
```

Add the dependency to your application's build.gradle file.

```groovy
    implementation 'com.joselopezrosario:android-fm:0.0.1'
```
___
# About the following examples

The variable `url` is your host and database. You don't need to pass any additional parameters like `sessions` or `layout` in the url.

This is all you need `https://yourHost/fmi/data/v1/databases/yourDatabase`
___
# Classes

* **FmRequest** to create API requests via instance methods
    * *FmRequest support classes*
        * **FmSort** to build sort parameters
        * **FmPortal** to build portal parameters
        * **FmScript** to build script parameters
        * **FmFind** to build find requests
        * **FmEdit** to build field name value parameters for create and edit
* **Fm** to execute the API request against the FileMaker Data API
* **FmResponse** to retrieve the response from the FileMaker Data API
* **FmData** to get the foundset from a response
* **FmRecord** to parse a record's field and portal data
---
## FmRequest
Use this class to create a request object that contains:

* The information required by the API - like the url, credentials, and body
* Other optional information created through support classes - like the sort, portal, and script parameters

### login(url, account, password)
***To log in to FileMaker and receive a token***

To use the FileMaker Data API, you must log in with an account that has the fmrest extended privilege enabled so you can receive a session token. All subsequent calls will require this token.

**Usage**:
```java
    // Create a request object, call the login method, and build it
    FmRequest request = new FmRequest()
            .login(url, account, password)
            .build();
    // Pass the request to Fm.execute() to receive a response
    FmResponse response = Fm.execute(request);
    // Use the getToken() method to get the token
    String token = response.getToken();
```

### logout(url, token)

***To logout from the FileMaker session and release the connection***
   
**Usage**:
```java
    FmRequest request = new FmRequest()
        .logout(url, token)
        .build();
```
### getRecords(url, token, layout)

***To get a foundset of records***

For example, to get 20 records starting from the 10th record.

**Usage**:
```java
    FmRequest request = new FmRequest()
        .getRecords(url, token, "vgsales")
        .setOffset(10)
        .setLimit(20)
        .build();
```


### getRecord(url, token, layout, recordId)
***To get a specific record by its id***

For example, to get record id 100.
```java
    FmRequest request = new FmRequest()
        .getRecord(url, token, "vgsales", 100)
        .build();
```


### findRecords(url, token, layout, fmFind)
***To find records by one or multiple criteria***

Use the FmFind class to easily build complex queries. FmFind has the following methods that you must call in order.

1. `newRequest()` to start a new find request
2. `set()` to set the field name value pairs (you can call this method multiple times)
3. `omit()` to omit the records (optional: defaults to false)

You can chain the methods to create multiple find requests at once. 

This example shows how to to find all the games published by Nintendo in 1985, and all games published by Sega between 1991 and 1996, but it omits the games puiblished by Sega in 1994.

**Usage**:

```java
    FmFind fmFind = new FmFind()
        .newRequest().set("Publisher", "Nintendo").set("Year", "1985")
        .newRequest().set("Publisher", "Sega").set("Year", "1991...1996")
        .newRequest().set("Publisher", "Sega").set("Year", "1994").omit();
```
Then pass the FmFind object to the FmRequest.findRecords() method.
```java
    FmRequest request = new FmRequest()
        .findRecords(url, token, "vgsales", fmFind)
        .build();
```
### create(url, token, layout)
***To create a blank record.***

**Usage**:
```java
    FmRequest request = new FmRequest()
        .create(url, token, "vgsales")
        .build();
 ```
### create(url, token, layout, fmEdit)
***To create a record with values***

To set the initial values use the FmEdit class. Set the name of the field and its value through the `set()` method.

**Usage**:
```java
    FmEdit edit = new FmEdit();
        .set("Rank", "999732")
        .set("Name", "Jose's game")
        .set("Publisher", "Lorem ipsum")
        .set("Genre", "Arcade")
        .set("Platform", "Nes")
        .set("Year", "1981");
    FmRequest request = new FmRequest()
        .create(url, token, "vgsales", fmEdit)
        .build();
 ```

 ### delete(url, token, layout, recordId)
 ***To delete a record***

To delete a record, call the `delete()` method and pass the record's id.

**Usage**:

```java
    FmRequest request = new FmRequest()
        .delete(url, token, "vgsales", 245)
        .build();
```

Optionally, you can pass an FmScript object through the setScriptParams() method.

For example:

```java
    int recordId = 1300;
    FmScript script = new FmScript()
        .setScript("log").setScriptParam("Deleted record #" + recordId);
    FmRequest request = new FmRequest()
        .delete(url, token, "vgsales", recordId)
        .setScriptPrams(script)
        .build();
```
___

## Fm
The `Fm` class provides a static method called `execute()` to run a request. 

**Usage**:

```java
    Fm.execute(request);
```    
___
## FmResponse
The `FmResponse` class is used to retrieve the results from the FileMaker Server Data API.

**Usage**:
```java
    FmResponse response = Fm.execute(request);
```

## getHttpCode()

To get the server's http response code.

**Usage**:

```java
    FmRequest request = new FmRequest()
            .login("url", "jose", "syaP.F;9'b+F#q#Y")
            .build();
    FmResponse response = Fm.execute(request);
    if (response.getHttpCode()) {
        // Success
    }
___

# Optional Support Classes
Use these classes to build the optional request parameters.
## FmPortal
***To get related records***
This example shows how to get the first 10 records from the genres layout, and for each genre record, to get the first 3 related vgsales records, and the first 3 related publisher records.

Create an FmPortal object and call the `setName()`, `setLimit()` and `setOffset()` methods.

For example:
```java
    FmPortal fmPortal = new FmPortal()
        .setName("vgsales").setLimit(3).setOffset(1)
        .setName("publishers").setLimit(3).setOffset(1);
```
Then pass the FmPortal object to your FmRequest through the `setPortalParams()` method.
```java
    FmRequest request = new FmRequest()
        .getRecords(url, token, "vgsales")
        .setPortalParams(fmPortal)
        .build();
```
## FmSort

***To sort records***
The FileMaker Data API allows you to sort records by multiple fields in ascending or descending order. This example shows you how to get the first 1,000 records from the vgsales layout and sort them by genre ascending and rank descending. 

First, build an FmSort object and call the `sortAsc()` and `sortDesc()` methods with the names of the fields you want to sort by.

```java
    FmSort fmSort = new FmSort()
        .sortAsc("Genre")
        .sortDesc("Rank");
```
Then, pass it to the FmRequest object through the `sortParams()` method.

```java    
    FmRequest request = new FmRequest()
        .getRecords(url, token, "vgsales")
        .setLimit(1000)
        .setOffset(1)
        .setSortParams(fmSort)
        .build();        
```
## FmScript

***To call scripts***
The FileMaker Data API has three options for running scripts:

1. **script**: runs after the action and sort are executed
2. **script pre-request**: runs before the action and sort are executed
3. **script pre-sort**: runs after executing the request but before sorting the records

You can set these options through an FmScript object. Create the object using this constructor `FmScript()`.  Then set the options using the following methods:

* `setScript()` - sets the script name
* `setScriptParam()` - sets the script parameter
* `setPreRequest()` - sets the pre-request script name
* `setPreRequestParam()` - sets the pre-request script paramter
* `setPreSort()` - sets the pre-sort script name
* `setPreSortParam()` - sets the pre-sort script paramter
* `setLayoutResponse()` - the layout to switch to when processing the response

For example, the VideoGameSales FileMaker databases has a script named log that goes to the log layout, creates a new record, and sets the Message field with the value of the script parameter. We can create an FmScript object to run the `log` script using the three options.

**Usage**:
```java
    FmScript script = new FmScript()
        .setPreRequest("log").setPreRequestParam("Hello from pre-request script")
        .setPreSort("log").setPreSortParam("Hello from pre-sort script")
        .setScript("log").setScriptParam("Hello from script")
```
And then pass it to the FmRequest object through the `setScriptParams()` method.
```java
    FmRequest request = new FmRequest()
        .getRecords(url, token, "vgsales")
        .setScriptPrams(script)
        .build();
```
After executing the request, the script creates the following records in the log table.
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
