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
                .contentType(JSON)
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    public void testCreateAccount() {
        String accountName = "test-account-1";
        String accountDescription = "For the test 'testCreateAccount'";

        JsonObject payload = Json.createObjectBuilder()
                .add("name", accountName)
                .add("description", accountDescription)
                .build();

        Number id = this.createAccount(payload);
        System.out.println(id);
        this.deleteAccount(id.longValue());
    }

    @Test
    public void testDeleteAccount() {
        String accountName = "test-account-2";
        String accountDescription = "For the test 'testDeleteAccount'";

        JsonObject payload = Json.createObjectBuilder()
                .add("name", accountName)
                .add("description", accountDescription)
                .build();

        Number id = this.createAccount(payload);
        this.deleteAccount(id.longValue());
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

    private Number createAccount(JsonObject payload) {
        return given()
            .contentType(JSON)
            .body(payload.toString())
        .when()
            .post("/account")
        .then()
            .contentType(JSON)
            .statusCode(200)
            .body("name", is(payload.getString("name")))
            .body("description", is(payload.getString("description")))
            .body("id", isA(Number.class))
        .extract()
            .path("id");
    }

    private void deleteAccount(Long id) {
        given()
            .pathParam("id", id)
        .when()
            .delete("/account/{id}")
        .then()
            .contentType(JSON)
            .statusCode(200)
            .body("name", is(any(String.class)))
            .body("description", is(any(String.class)))
            .body("id", is(id.intValue()));
    }
}
