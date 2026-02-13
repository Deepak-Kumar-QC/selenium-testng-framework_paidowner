package utils;

import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ApiUtils {

    public static long getUserRefNoFromAPI(WebDriver driver) {

        Map<String, String> cookies = ((RemoteWebDriver) driver).manage().getCookies().stream()
                .collect(Collectors.toMap(Cookie::getName, Cookie::getValue));

        io.restassured.response.Response response = io.restassured.RestAssured.given().relaxedHTTPSValidation()
                .cookies(cookies).when().get("https://www.magicbricks.com/odashboard/loginDeatils").then().extract()
                .response();

        // ✅ EXACT SAME LOGS AS EARLIER
        System.out.println("====== API RESPONSE ======");
        System.out.println("Status Code : " + response.statusCode());
        System.out.println("Response    : " + response.asString());
        System.out.println("==========================");

        Long ubirfnum = response.jsonPath().getLong("ubirfnum");

        if (ubirfnum == null) {
            throw new RuntimeException("userRefNo not found in API response");
        }

        System.out.println("✅ UBI (userRefNo) fetched from API: " + ubirfnum);

        return ubirfnum;
    }
}