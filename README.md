# WAES - Assignment Scalable Web

## Assignment

* Provide 2 http endpoints that accepts JSON base64 encoded binary data on both
endpoints
  * _host_/v1/diff/_ID_/left 
  * _host_/v1/diff/_ID_/right
* The provided data needs to be diff-ed and the results shall be available on a third end
point
  * _host_/v1/diff/_ID_

* The results shall provide the following info in JSON format
  * If equal return that
  * If not of equal size just return that
  * If of same size provide insight in where the diffs are, actual diffs are not needed.
     * So mainly offsets + length in the data

* Make assumptions in the implementation explicit, choices are good but need to be
communicated

* Must haves
  * Solution written in Java
  * Internal logic shall be under unit test
  * Functionality shall be under integration test
  * Documentation in code
  * Clear and to the point readme on usage

* Nice to haves
  * Suggestions for improvement

## Tech Stack

  * Java 8
  * Spring Boot 2.1.5
  * H2 Database
  * JUnit 5
  * Lombok


## Running the application

To build application: 

```bash
gradlew clean build
```

To run application: 

```bash
gradlew clean bootRun
```

To start application: 

```bash
http://localhost:9001/
```

## Testing


```bash
gradlew clean test
```

### Documentation

Swagger2 documentation is available.

http://localhost:9001/swagger-ui.html#