package tests;

import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.Specs.*;

public class ReqresAPITests {


    @Test
    @DisplayName("Check successful registration")
    void checkSuccessfulRegistration() {

        RegisterBody requestBody = new RegisterBody();
        requestBody.setEmail("eve.holt@reqres.in");
        requestBody.setPassword("pistol");

        RegisterResponse response = step("Make the request", () ->
                given()
                        .spec(requestSpec)
                        .body(requestBody)
                        .when()
                        .post("/register")
                        .then()
                        .spec(response200spec)
                        .extract().as(RegisterResponse.class));

        step("Check user ID in a response", () ->
                assertThat(response.getId()).isNotNull());
        step("Check token in response", () ->
                assertThat(response.getToken()).isNotNull());
    }

    @Test
    @DisplayName("Check unsuccessful registration without password")
    void checkUnsuccessfulRegistration() {

        String expectedError = "Missing password";

        RegisterBody requestBody = new RegisterBody();
        requestBody.setEmail("sydney@fife");

        BadRequestResponse response = step("Make the request", () ->
                given()
                        .spec(requestSpec)
                        .body(requestBody)
                        .when()
                        .post("/register")
                        .then()
                        .spec(response400spec)
                        .extract().as(BadRequestResponse.class));

        step("Check error in the response", () ->
                assertThat(response.getError()).isEqualTo(expectedError));

    }

    @Test
    @DisplayName("Check successful user creation")
    void checkSuccessfulUserCreation() {

        String userName = "morpheus";
        String job = "leader";

        CreateUserBody requestBody = new CreateUserBody();
        requestBody.setName("morpheus");
        requestBody.setJob("leader");

        CreateUserResponse response = step("Make a request", () ->
                given()
                        .spec(requestSpec)
                        .body(requestBody)
                        .when()
                        .post("/users")
                        .then()
                        .spec(response201spec)
                        .extract().as(CreateUserResponse.class));

        step("Check user name in the response", () ->
                assertThat(response.getName()).isEqualTo(userName));
        step("Check user id in the response", () ->
                assertThat(response.getJob()).isEqualTo(job));
    }

    @Test
    @DisplayName("Check scheme structure of user's list page 2")
    void checkSchemeStructureUsersList() {


        ListUsersResponse response = step("Make a request", () ->
        given()
                .spec(requestSpec)
                .when()
                .get("/users?page=2")
                .then()
                .spec(response200listUsers)
                .extract().as(ListUsersResponse.class));
        step("Check JSON scheme in the request", () ->
                assertThat(response.getData()).isNotEmpty());
    }

    @Test
    @DisplayName("Check sending unknown user request")
    void checkUnknownUserRequest() {

        step("Make a request", () ->
        given()
                .spec(requestSpec)
                .when()
                .get("/users/23")
                .then()
                .spec(response404spec));
        step("Check error in the request");
    }
}
