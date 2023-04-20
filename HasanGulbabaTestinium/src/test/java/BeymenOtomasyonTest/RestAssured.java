package BeymenOtomasyonTest;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import io.restassured.RestAssured;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestAssured {


    String baseUrl="https://api.trello.com/1/boards";
    String token = "ATTAa5ab786309a555134a6faca42773341502b0af35949717343c7a29aecfbae2c9A759267B";
    String key = "26e63079e027a6e57934a399560e9f31";


    public static WebDriver driver=null;


    //GET 1
    @Test
    public void createBoard() throws InterruptedException{

        Map<String, String> httpRequest = new HashMap<>();
        io.restassured.RestAssured.given().header("Authorization", "Bearer " + token);
        httpRequest.put("name", "My New Board");
        httpRequest.put("defaultLists", "false");


        Response response =
                 given()
                        .log().all()
                         .when()
                         .headers(httpRequest)
                         .post(baseUrl)
                         .then()
                         .assertThat()
                         .contentType("application/json; charset=UTF-8")
                         .and()
                         .extract()
                         .response();

        System.out.println("Response code: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody().asString());

    }
}
