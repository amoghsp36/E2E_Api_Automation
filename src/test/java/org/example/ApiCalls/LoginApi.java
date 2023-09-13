package org.example.ApiCalls;

import static io.restassured.RestAssured.given;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapper;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.example.Pojo.*;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoginApi {

    @BeforeTest
    public void setUp(){

    }
    static RequestSpecification reqSpec2(){
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBaseUri("https://rahulshettyacademy.com/api/ecom/");
        builder.setContentType(ContentType.JSON);
        builder.setAccept(ContentType.JSON);
        RequestSpecification requestSpec = builder.build();
        return requestSpec;
    }

    static ResponseSpecification resSpec2(){
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.expectStatusCode(200);
        builder.expectContentType(ContentType.JSON);

        ResponseSpecification responseSpecification = builder.build();
        return responseSpecification;
    }
    String token;
    String userId;
    String productId;
    String orderId;

    @Test
    public void loginRequests() {
        //LoginCall
        RequestSpecification reqSpec = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com/api/ecom/")
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();

        ResponseSpecification resSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .build();

        LoginRequest loginPojo = new LoginRequest();
        loginPojo.setUserEmail("e2eapi@gmail.com");
        loginPojo.setUserPassword("Abc@1234");

        //SpecBuilder specBuilder = new SpecBuilder();

        RequestSpecification reqSpecification = given().log().all().spec(reqSpec).body(loginPojo);
        LoginResponse loginResponse = reqSpecification.when().post("auth/login")
                .then().spec(resSpec)
                .extract()
                .response()
                .as(LoginResponse.class);          //or extract().response().asString() if the response is not complex

        System.out.println(loginResponse.getToken());
        token = loginResponse.getToken();
        System.out.println(loginResponse.getUserId());
        userId = loginResponse.getUserId();
        System.out.println(loginResponse.getMessage());

        //Add Product Call

        RequestSpecification createProduct = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com/api/ecom/")
                .addHeader("authorization", token).build();

        RequestSpecification productParams = given().log().all().spec(createProduct)
                .param("productName", "playStation")   //form data-form parameters
                .param("productAddedBy", userId)
                .param("productCategory", "Electronic")
                .param("productSubCategory", "console")
                .param("productPrice", "50000")
                .param("productDescription", "Sony Playstation 5")
                .param("productFor", "Anyone")
                .multiPart("productImage", new File("/Users/testvagrant/Downloads/ps5.jpeg"));

        CreateProduct createProduct1 = productParams.when().post("product/add-product")
                .then()
                .extract().response()
                .as(CreateProduct.class);

        System.out.println(createProduct1.getProductId());
        productId = createProduct1.getProductId();
        System.out.println(createProduct1.getMessage());

        //Create Order
        RequestSpecification createOrder = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com/api/ecom/")
                .setContentType(ContentType.JSON)
                .addHeader("authorization",token).build();

        OrderList placeOrder = new OrderList();
        placeOrder.setCountry("india");
        placeOrder.setProductOrderedId(productId);

        CreateOrder order = new CreateOrder();
        List<OrderList> orders = new ArrayList<>();
        orders.add(placeOrder);
        order.setOrders(orders);

//        ObjectMapper objectMapper = new ObjectMapper();
//        com.fasterxml.jackson.databind.ObjectMapper


        RequestSpecification orderCreation = given().spec(createOrder).body(order);

//        String createOrderResponse = orderCreation.when().post("order/create-order")
//                .then().extract().response().asString();
//        System.out.println(createOrderResponse);

        CreateOrderResponse createOrderResponse1 = orderCreation.when().post("order/create-order")
                .then().extract().response()
                .as(CreateOrderResponse.class);

        orderId = createOrderResponse1.getOrders().toString();
        System.out.println("the order id is "+orderId);
        System.out.println(createOrderResponse1.getProductOrderId());
        System.out.println(createOrderResponse1.getMessage());

        //Delete Order

        RequestSpecification deleteProduct = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com/api/ecom/")
                .addHeader("authorization",token).build();

        RequestSpecification deleteReq = given().spec(deleteProduct).pathParam("productId", productId);
        String deleteRes = deleteReq.when().delete("/product/delete-product/{productId}")
                .then().log().all().extract().response().asString();
        JsonPath jsonPath = new JsonPath(deleteRes);
        System.out.println(jsonPath.getString("message"));
        System.out.println(deleteRes);

    }

}
