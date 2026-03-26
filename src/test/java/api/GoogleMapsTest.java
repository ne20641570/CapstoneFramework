package api;

import api.client.ApiClient;
import api.endpoints.Routes;
import api.payloads.GoogleMapsPayload;
import org.testng.annotations.BeforeMethod;
import utils.ExtentReportUtil;
import utils.ExtentTestManager;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import java.lang.reflect.Method;

public class GoogleMapsTest {

    static String placeId;

    ExtentTest tests = ExtentTestManager.getTest();
    ExtentTest test;

    @BeforeMethod
    public void beforeMethod(Method method) {
        tests = ExtentTestManager.startTest(method.getName(), "Test description");
    }

    @Test(priority = 0)
    public void addPlaceTest() {
        test = ExtentTestManager.startNode(tests, "Add Place Test");
        try {
            Response response = ApiClient.postRequest(
                    Routes.ADD_PLACE,
                    GoogleMapsPayload.addPlacePayload()
            );

            placeId = response.jsonPath().getString("place_id");

            ExtentReportUtil.logInfo(test,"Place ID: " + placeId);

            Assert.assertEquals(response.getStatusCode(), 200);
            ExtentReportUtil.logPass(test,"Add Place Successful");

        } catch (Exception e) {
            ExtentReportUtil.logFail(test,"Exception occurred: " + e.getMessage());
            Assert.fail();
        }
    }

    @Test(dependsOnMethods = "addPlaceTest",priority = 1)
    public void getPlaceTest() {

        test = ExtentTestManager.startNode(tests, "Get Place Test");


        Response response = ApiClient.getRequest(Routes.GET_PLACE, placeId);

        Assert.assertEquals(response.getStatusCode(), 200);
        ExtentReportUtil.logPass(test,"Get Place Successful");
    }

    @Test(dependsOnMethods = "addPlaceTest",priority = 2)
    public void updatePlaceTest() {

        test = ExtentTestManager.startNode(tests, "Update Place Test");

        Response response = ApiClient.putRequest(
                Routes.UPDATE_PLACE,
                GoogleMapsPayload.updatePayload(placeId)
        );

        Assert.assertEquals(response.getStatusCode(), 200);
        ExtentReportUtil.logPass(test,"Update Successful");
    }

    @Test(dependsOnMethods = "addPlaceTest",priority = 3)
    public void deletePlaceTest() {

        test = ExtentTestManager.startNode(tests, "Delete Place Test");

        Response response = ApiClient.deleteRequest(
                Routes.DELETE_PLACE,
                GoogleMapsPayload.deletePayload(placeId)
        );

        Assert.assertEquals(response.getStatusCode(), 200);
        ExtentReportUtil.logPass(test,"Delete Successful");
    }
}
