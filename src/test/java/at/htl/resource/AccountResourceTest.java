package at.htl.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountResourceTest {

    Jsonb jsonb;

    @BeforeAll
    public void init() {
        this.jsonb = JsonbBuilder.create();
    }

    @Test
    public void testGetAllAccounts() {
        given()
            .when()
                .get("/account")
            .then()
                .statusCode(200)
                .contentType(JSON)
                .body("size()", is(0));
    }

    @Test
    public void testCreateAccount() {
        JsonObject payload = this.createAccountPayload("test-account-1", "For the test 'testCreateAccount'");
        Integer id = this.createAccount(payload);
        this.deleteAccount(id);
    }

    @Test
    public void testDeleteAccount() {
        JsonObject payload = this.createAccountPayload("test-account-2", "For the test 'testDeleteAccount'");
        Integer id = this.createAccount(payload);
        this.deleteAccount(id);
    }

    @Test
    public void testDeleteNotExistingAccount() {
        given()
            .pathParam("id", 999)
        .when()
            .delete("/account/{id}")
        .then()
            .statusCode(404);
    }

    @Test
    public void testGetAccountById() {
        JsonObject payload = this.createAccountPayload("test-account-3", "For the test 'testGetAccountById'");
        Integer id = this.createAccount(payload);

        given()
            .pathParam("id", id.longValue())
        .when()
            .get("/account/{id}")
        .then()
            .statusCode(200)
            .contentType(JSON)
            .body("name", is(payload.getString("name")))
            .body("description", is(payload.getString("description")))
            .body("id", isA(Number.class));

        deleteAccount(id);
    }

    ////////////////////
    // Util functions //
    ////////////////////

    private JsonObject createAccountPayload(String name, String description) {
        return Json.createObjectBuilder()
                .add("name", name)
                .add("description", description)
                .build();
    }

    private Integer createAccount(JsonObject payload) {
        Number id = given()
            .contentType(JSON)
            .body(payload.toString())
        .when()
            .post("/account")
        .then()
            .statusCode(200)
            .contentType(JSON)
            .body("name", is(payload.getString("name")))
            .body("description", is(payload.getString("description")))
            .body("id", isA(Number.class))
        .extract()
            .path("id");

        return id.intValue();
    }

    private void deleteAccount(Integer id) {
        given()
            .pathParam("id", id)
        .when()
            .delete("/account/{id}")
        .then()
            .statusCode(200)
            .contentType(JSON)
            .body("name", is(any(String.class)))
            .body("description", is(any(String.class)))
            .body("id", is(id));
    }
}
