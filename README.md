# FileMaker Data API (Fma)

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
***Getting related records***
This example shows how to get the first 10 records from the genres layout, and for each genre record, to get the first 3 related vgsales records, and the first 3 related publisher records.

You will set the name, limit, and offset via FmaParamPortal objects. Create them using this constructor: `FmaParamPortal(String name, int limit, int offset)`

Then add all your FmaParamPortal objects to an `ArrayList()`.

For example:
```java
        FmaParamPortal portalVgSales = new FmaParamPortal(LAYOUT_VGSALES, 3, 1);
        FmaParamPortal portalPublishers = new FmaParamPortal(LAYOUT_PUBLISHERS, 3, 1);
        ArrayList<FmaParamPortal> fmaParamPortalArrayList = new ArrayList<>();
        fmaParamPortalArrayList.add(portalVgSales);
        fmaParamPortalArrayList.add(portalPublishers);
```

Then, pass the ArrayList to the FmaRequest object through the `setPortalParams()` method.

```java
        FmaRequest request = new FmaRequest()
                .getRecords(ENDPOINT, token, LAYOUT_GENRES)
                .setLimit(10)
                .setOffset(1)
                .setPortalParams(fmaParamPortalArrayList)
                .build();
```
***Sorting records***
This example shows how to get the first 1,000 records from the vgsales layout, and to sort them by genre ascending and rank descending. 

You will set the field name and the sort order via FmaParamSort objects. Create them using this constructor: `FmaParamSort(String name)`. And then set the sort order by calling the `ascend()` or `descend()` method. 

For example: `new FmaParamSort("Rank").descend()` to sort by rank descending. 

Finally, add all objects to an `ArrayList()`.

Here is a complete example:

```java
        // Create the FmaParamSort objects
        FmaParamSort sortByGenre = new FmaParamSort("Genre"); // note: sort order defaults to ascend
        FmaParamSort sortByRank = new FmaParamSort("Rank").descend();
       
       // Add them to an ArrayList()
        ArrayList<FmaParamSort> sortParams = new ArrayList<>();
        sortParams.add(sortByGenre);
        sortParams.add(sortByRank);
       
       // Create a new FmaRequest and pass the sort parameters through the setSortParams() method
        FmaRequest request = new FmaRequest()
                .getRecords(ENDPOINT, token, LAYOUT_VGSALES)
                .setLimit(1000)
                .setOffset(1)
                .setSortParams(sortParams) // set the sort parameters
                .build();        
```


## Testing

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

## License

This project is licensed under the "GNU LGPLv3" License.