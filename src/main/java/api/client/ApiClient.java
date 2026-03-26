package api.client;
import config.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class ApiClient {

    static {
        RestAssured.baseURI = ConfigReader.get("baseUrl");
    }

    public static Response postRequest(String resource, String body) {
        return given()
                .queryParam("key", ConfigReader.get("key"))
                .header("Content-Type", "application/json")
                .body(body)
                .log().all()
                .when()
                .post(resource)
                .then()
                .log().all()
                .extract().response();
    }

    public static Response getRequest(String resource, String placeId) {
        return given()
                .queryParam("key", ConfigReader.get("key"))
                .queryParam("place_id", placeId)
                .log().all()
                .when()
                .get(resource)
                .then()
                .log().all()
                .extract().response();
    }

    public static Response putRequest(String resource, String body) {
        return given()
                .queryParam("key", ConfigReader.get("key"))
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .put(resource)
                .then()
                .extract().response();
    }

    public static Response deleteRequest(String resource, String body) {
        return given()
                .queryParam("key", ConfigReader.get("key"))
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post(resource)
                .then()
                .extract().response();
    }
}
