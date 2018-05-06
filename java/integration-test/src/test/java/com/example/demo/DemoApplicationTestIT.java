package com.example.demo;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.Matchers.*;


public class DemoApplicationTestIT {

    private static final String IT_SERVER_HOST = "IT_SERVER_HOST";

    @BeforeClass
    public static void setup() {

        String baseHost = System.getenv(IT_SERVER_HOST);
        if(baseHost==null){
            throw new RuntimeException(IT_SERVER_HOST + " must be set");
        }
        RestAssured.baseURI = baseHost;
        System.out.println("Base URI is " + RestAssured.baseURI);

        String port = System.getenv("IT_SERVER_PORT");
        if (port == null) {
            RestAssured.port = Integer.valueOf(80);
        }
        else{
            RestAssured.port = Integer.valueOf(port);
        }

        String basePath = System.getenv("IT_SERVER_BASE");
        if(basePath==null){
            basePath = "/";
        }
        RestAssured.basePath = basePath;

    }

    @Test
    public void getItemsShouldReturnBothItems() {

        RestAssured
                .get("/hello")
                .then()
                .statusCode(HttpStatus.SC_OK);

        RestAssured
            .get("/metrics")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body(containsString("endpoint_hello_total"));

    }

}
