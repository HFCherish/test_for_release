package com.tw.metadata.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.tw.metadata.Application;
import com.tw.metadata.util.ResourceUtil;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.swagger.util.Json;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * Created by pzzheng on 10/30/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class HicApiTest {
    @BeforeClass
    public static void setUp() {
        RestAssured.port = 8080;
        RestAssured.basePath = "/tw";
    }

    @Test
    public void should_201() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(ResourceUtil.SWAGGER_PATH + "/petstore.json");
        JsonNode apiBody = Json.mapper().readTree(fileInputStream);
        given()
                .contentType(ContentType.JSON)
                .body(apiBody)

                .when()
                .post("/api-specs")

                .then()
                .statusCode(201)
                .body("data", is(notNullValue()));
    }
}