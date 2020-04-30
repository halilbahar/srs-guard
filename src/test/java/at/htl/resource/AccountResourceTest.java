package at.htl.resource;

import at.htl.util.Helper;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import java.util.Random;

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
    public void testCreateAndDeleteAccount() {
        JsonObject payload = this.createAccountPayload("test-account", "test: testCreateAccount");
        Integer id = this.createAccount(payload);
        this.deleteAccount(id);
    }

    @Test
    public void testCreateAccountWithProvidedKeyAndId() {
        String accountName = "test-account";
        String accountDescription = "test: testCreateAccountWithProvidedKey";
        String accountKey = "key999";
        int accountId = 999;
        JsonObject payload = Json.createObjectBuilder()
                .add("name", accountName)
                .add("description", accountDescription)
                .add("key", accountKey)
                .add("id", accountId)
                .build();

        Number id = given()
            .contentType(JSON)
            .body(payload.toString())
        .when()
            .post("/account")
        .then()
            .statusCode(200)
            .contentType(JSON)
            .body("name", is(accountName))
            .body("description", is(accountDescription))
            .body("key", is(not(accountKey)))
            .body("id", is(not(accountId)))
        .extract()
            .path("id");

        deleteAccount(id.intValue());
    }

    @Test
    public void testCreateAccountWithTooShortName() {
        String accountName = "ab";
        JsonObject payload = this.createAccountPayload(accountName, "test: testCreateAccountWithTooShortName");
        given()
            .contentType(JSON)
            .body(payload.toString())
        .when()
            .post("/account")
        .then()
            .statusCode(422)
            .contentType(JSON)
            .body("key[0]", is("name"))
            .body("message[0]", is("Name needs to be between 3 and 255 characters!"))
            .body("value[0]", is(accountName));
    }

    @Test
    public void testCreateAccountWithTooLongName() {
        String accountName = Helper.generateRandomString(256);
        JsonObject payload = this.createAccountPayload(accountName, "test: testCreateAccountWithTooLongName");
        given()
            .contentType(JSON)
            .body(payload.toString())
        .when()
            .post("/account")
        .then()
            .statusCode(422)
            .contentType(JSON)
            .body("key[0]", is("name"))
            .body("message[0]", is("Name needs to be between 3 and 255 characters!"))
            .body("value[0]", is(accountName));
    }

    @Test
    public void testCreateAccountWithTooLongDescription() {
        String accountDescription = Helper.generateRandomString(256);
        JsonObject payload = this.createAccountPayload("test-account", accountDescription);
        given()
            .contentType(JSON)
            .body(payload.toString())
        .when()
            .post("/account")
        .then()
            .statusCode(422)
            .contentType(JSON)
            .body("key[0]", is("description"))
            .body("message[0]", is("Description may not be longer than 255!"))
            .body("value[0]", is(accountDescription));
    }

    @Test
    public void testCreateAccountWithEmptyBody() {
        given()
            .contentType(JSON)
        .when()
            .post("/account")
        .then()
            .statusCode(422);
    }

    @Test
    public void testGetAccountById() {
        JsonObject payload = this.createAccountPayload("test-account", "test: testGetAccountById");
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
            .body("key", hasLength(32))
            .body("id", isA(Number.class));

        this.deleteAccount(id);
    }

    @Test
    public void testDeleteAccount() {
        JsonObject payload = this.createAccountPayload("test-account", "test: testDeleteAccount");
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
            .body("key", hasLength(32))
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
            .body("key", hasLength(32))
            .body("id", is(id));
    }
}
