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

And then add the dependency to your application's build.gradle file (use the latest version).

```groovy
    implementation 'com.joselopezrosario:android-fm:+'
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

Complete the request by calling `build()` method.

### login(url, account, password)
To log in to FileMaker and receive a token

To use the FileMaker Data API, you must log in with an account that has the fmrest extended privilege enabled so you can receive a session token. All subsequent calls will require this token.


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

### loginOAuth(url, requestId, identifier)
You can also use OAuth to authenticate. 


```java
    FmRequest request = new FmRequest()
        .loginOAuth(url, requestId, identifier)
        .build();
```

### logout(url, token)

To logout from the FileMaker session and release the connection
   

```java
    FmRequest request = new FmRequest()
        .logout(url, token)
        .build();
```
### getRecords(url, token, layout)

To get a foundset of records

For example, to get 20 records starting from the 10th record.


```java
    FmRequest request = new FmRequest()
        .getRecords(url, token, "vgsales")
        .setOffset(10)
        .setLimit(20)
        .build();
```


### getRecord(url, token, layout, recordId)
To get a specific record by its id

For example, to get record id 100.
```java
    FmRequest request = new FmRequest()
        .getRecord(url, token, "vgsales", 100)
        .build();
```


### findRecords(url, token, layout, fmFind)
To find records by one or multiple criteria

Use the FmFind class to easily build complex queries. FmFind has the following methods that you must call in order.

1. `newRequest()` to start a new find request
2. `set()` to set the field name value pairs (you can call this method multiple times)
3. `omit()` to omit the records (optional: defaults to false)

You can chain the methods to create multiple find requests at once. 

This example shows how to to find all the games published by Nintendo in 1985, and all games published by Sega between 1991 and 1996, but it omits the games puiblished by Sega in 1994.



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
To create a blank record


```java
    FmRequest request = new FmRequest()
        .create(url, token, "vgsales")
        .build();
 ```
### create(url, token, layout, fmEdit)
To create a record with values

To set the initial values use the FmEdit class. Set the name of the field and its value through the `set()` method.


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
 To delete a record

To delete a record, call the `delete()` method and pass the record's id.



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



```java
    Fm.execute(request);
```    
The `execute()` method returns and `FmResponse` object.
___
## FmResponse
The `FmResponse` class is used to retrieve the results from the FileMaker Server Data API.


```java
    FmResponse response = Fm.execute(request);
```

### getHttpCode()

To get the server's http response code.



```java
    FmRequest request = new FmRequest()
            .login("url", "jose", "syaP.F;9'b+F#q#Y")
            .build();
    FmResponse response = Fm.execute(request);
    if (response.getHttpCode() == 200) {
        // Success
    }
```

### getFmCode() and getFmMessage()

To get the error code and error message returned by the FileMaker Data API.



```java
    // Execute the request and get a response
    FmResponse response = Fm.execute(request);
    // Get the message from FileMaker, for example "Ok"
    String fmMessage = response.getFmMessage();
    // Get the message code from FileMaker, for example 0
    int fmCode = response.getFmMessageCode();
```

### isOk()

To simply return `true` if the request was successful.



```java
    FmResponse response = Fm.execute(request);
    response.isOk(){
        // success!
    }
```

### getToken()

To get the authorization token after executing the `login()` request.



```java
    String token = response.getToken();
```

### getRecordId() and getModId()

To get the record's id and mod id. 

The table below shows when to use these methods.

| Request       |  getRecordId()| getModId()| 
|---            |---            |---        |
| create()      | Yes           | Yes       |
| edit()        | No            | Yes       | 
| getRecord()   | Yes           | Yes       | 

### The script response methods

To get the script error and script result from the response.

**On pre-request**
* `getPreRequestScriptError()`
* `getPreRequestScriptResult()`

**On pre-sort**
* `getPreSortScriptError()`
* `getPreSortScriptResult()`

**After request and sort**
* `getScriptError()`
* `getScriptResult()`



```java
    int preRequestError = response.getPreRequestScriptError();
    String preRequestResult = response.getPreRequestScriptResult();
    
    int preSortError = response.getPreSortScriptError();
    String preSortResult = response.getPreSortScriptResult();
    
    int scriptError = response.getScriptError();
    String scriptResult = response.getScriptResult();
```
___

# FmData

A class to handle the records that come through a response.

First, create an `FmData` object while passing an `FmResponse`.



```java
    FmData data = new FmData(response);
```

### size()

To get the number of records in the data object.



```java
    int size = data.size();
```

### getRecord(index)

To get the specified record pass the record's index position. This method returns an FmRecord object which you will see in the next section.



```java
    FmRecord record = fmData.getRecord(200);
```
___
# FmRecord
A class to facilitate parsing an individual record.

First, create an `FmRecord` object while passing the results of `FmData.getRecord()`.



To create a new object.
```java
    FmRecord record = data.getRecord(100);
```

To loop through all the records.

```java
    FmData fmData = new FmData(response);
    int size = fmData.size();
    int i = 0;
    while ( i < size ){
        FmRecord record = fmData.getRecord(i);
        // Do something with each record
        i ++;
    }       
```

## getRecordId() & getModId()

To get the record's id and mod it.


```java
    int recId = record.getRecordId();
    int modId = record.getModId();
```

## getValue(fieldName)

To get a record's field value. The values are always returned as String.


```java
    String rank = record.getValue("Rank");
    String name = record.getValue("Name");
    String platform = record.getValue("Platform");
    String publisher = record.getValue("Publisher");
    String genre = record.getValue("Genre");
    String year = record.getValue("Year");
```

## getPortalSize()

To get the number of related portal records.

```java
    int size = record.getPortalSize();
```

## getPortalRecord(portalName, index)

To create a new record out of a related portal record.

```java
    FmRecord portalRecord = record.getPortalRecord("vgsales", 100);
```
___
# Optional & Support Classes

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

1. **script pre-request**: runs before the action and sort are executed
2. **script pre-sort**: runs after executing the request but before sorting the records
3. **script**: runs after the action and sort are executed

You can set these options through an FmScript object. Create the object using this constructor `FmScript()`.  Then set the options using the following methods:

**Pre-request**
* `setPreRequest()` sets the pre-request script name
* `setPreRequestParam()` sets the pre-request script paramter

**Pre-sort**
* `setPreSort()` sets the pre-sort script name
* `setPreSortParam()` sets the pre-sort script paramter

**After request and sort**
* `setScript()` sets the script name
* `setScriptParam()` sets the script parameter
* `setLayoutResponse()` the layout to switch to when processing the response

For example, the VideoGameSales FileMaker databases has a script named log that goes to the log layout, creates a new record, and sets the Message field with the value of the script parameter. We can create an FmScript object to run the `log` script using the three options.


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

The table below shows which requests support the `setScriptParam()` method.

| Request       |  Supports Scripts | 
|---            |---                |
| create()      | No                | 
| delete()      | Yes               | 
| edit()        | Yes               | 
| getRecord()   | Yes               | 
| getRecords()  | Yes               | 
| upload()      | No                | 
| findRecords() | Yes               | 

___

# Testing

To run the unit tests, modify the the gradle.properties file to include your own values:

```groovy
API_HOST="https://yourhost/fmi/data/v1/databases/VideoGameSales"
API_ACCOUNT="APIUser"
API_PASSWORD="syaP.F;9'b+F#q#Y"
```

If you are using [TravisCI](https://travis-ci.org/), also modify the travis.yml file to include your environment variables:

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
