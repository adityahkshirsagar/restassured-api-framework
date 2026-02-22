package WorkFlowLibrary;

import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;

import GenericUtilities.BaseClass;
import GenericUtilities.EndPoint;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class Custom_Token extends BaseClass{

	public String generateToken(String Auth){
		Response res= given()
				.header("Authorization", Auth)
				.header("contentType", "application/x-www-form-urlencoded")
				.header("Accept", "application/json")
				.formParam("grant_type", "client_credentials")
				.formParam("scopes", "*")
				.when().post("https://example.invalid");

		String customToken = res.getBody().jsonPath().getJsonObject("access_token").toString();
		return customToken;
	}

	public String generateTokenBifost(String Auth){
		Response res= given()
				.header("Authorization", Auth)
				.header("Content-Type", "application/x-www-form-urlencoded")
				.header("Accept", "application/json")
				.formParam("grant_type", "client_credentials")
				.formParam("scopes", "*")
				.when().post("https://example.invalid").then().log().all().extract().response();

		String customToken = res.getBody().jsonPath().getJsonObject("access_token").toString();
		return customToken;
	}

public String generateDialerToken() throws Throwable{

		String body="{\"email\":\"sample@example.invalid\",\"password\":\"<REDACTED_PASSWORD>\",\"is_password_encrypted\":0}";
		ValidatableResponse res= given()
                .header("Cookie", "laravel_session=hzx0L16wSmKtIWZ8IzBIhHging5LekaQL7EVZvoi")
                .header("content-type", "application/json")
                .header("accept", "application/json")
                .body(body)
                .when().post("https://example.invalid").then();

        String crmToken = res.extract().response().jsonPath().getString("token");
        Thread.sleep(2000);
        ValidatableResponse res1= given()
                .header("Cookie", "laravel_session=hzx0L16wSmKtIWZ8IzBIhHging5LekaQL7EVZvoi")
                .header("accept", "application/json")
                .when().get("https://example.invalid"+crmToken).then();

        String access_token = res1.extract().response().jsonPath().getString("data");

		return access_token;
	}

	public String generatespToken() throws Throwable{

		String body="{\"email\":\"sample@example.invalid\",\"password\":\"<REDACTED_PASSWORD>\",\"is_password_encrypted\":0}";
		ValidatableResponse res= given()
				.header("Cookie", "laravel_session=hzx0L16wSmKtIWZ8IzBIhHging5LekaQL7EVZvoi")
				.header("content-type", "application/json")
				.header("accept", "application/json")
				.body(body)
				.when().post("https://example.invalid").then();

		String crmToken = res.extract().response().jsonPath().getString("token");
		Thread.sleep(2000);
		ValidatableResponse res1= given()
				.header("Cookie", "laravel_session=hzx0L16wSmKtIWZ8IzBIhHging5LekaQL7EVZvoi")
				.header("accept", "application/json")
				.when().get("https://example.invalid"+crmToken).then();

		String sp_Token = res1.extract().response().jsonPath().getString("spToken");

		return sp_Token;
	}

public String generateTokenPut(String Mobileno) throws Throwable {

        setBaseURI("application");
        String bodyOtp="{\"mobile_no\":\""+Mobileno+"\",\"source\":\"cjp\",\"url\":\"offers.example.invalid\"}";
        ValidatableResponse rsp = given().contentType("application/json").header("Cookie","customermetadata-cookie=0.6307311465949683").body(bodyOtp).when().post(EndPoint.SEND_OTP).then().log().all();

        String bodyConfirm="{\"mobile_no\":\""+Mobileno+"\",\"otp\":\"1111\"}";

        ValidatableResponse rsp1 = given().contentType("application/json").header("Cookie","customermetadata-cookie=0.6307311465949683").body(bodyConfirm).when().post(EndPoint.CONFIRM_OTP).then().log().all();

        String AuthToken=rsp1.extract().body().jsonPath().getString("user_access_token");

         return AuthToken;

    }
public String generateDialerToken1() throws Throwable{

	String body="{\"email\":\"sample@example.invalid\",\"password\":\"<REDACTED_PASSWORD>\",\"is_password_encrypted\":0}";
	Response res= given()
            .header("Cookie", "laravel_session=hzx0L16wSmKtIWZ8IzBIhHging5LekaQL7EVZvoi")
            .header("content-type", "application/json")
            .header("accept", "application/json")
            .body(body)
            .when().post("https://example.invalid");

    String crmToken = res.jsonPath().getString("token");

    Response res1= given()
            .header("Cookie", "laravel_session=hzx0L16wSmKtIWZ8IzBIhHging5LekaQL7EVZvoi")
            .header("accept", "application/json")
            .when().get("https://example.invalid"+crmToken);

    String access_token = res1.jsonPath().getString("data");

	return access_token;
}

}

