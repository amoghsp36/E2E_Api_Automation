package org.example.SpecBuilder;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class SpecBuilder {
    public void reqResSpecBuilder(){
        RequestSpecification reqSpec = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com/api/ecom/")
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();

        ResponseSpecification resSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();
    }
}
