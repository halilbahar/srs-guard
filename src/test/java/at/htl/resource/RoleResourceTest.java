package at.htl.resource;

import at.htl.util.Helper;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class RoleResourceTest {

    @Test
    public void testGetAllRoles() {
        given()
        .when()
            .get("/role")
        .then()
            .statusCode(200)
            .contentType(JSON)
            .body("size()", is(0));
    }

    @Test
    public void testCreateAndDeleteRole() {
        JsonObject payload = Json.createObjectBuilder()
                .add("name", "test-role")
                .add("description", "test: testCreateAndDeleteRole")
                .build();

        int id = given()
            .contentType(JSON)
            .body(payload.toString())
        .when()
            .post("/role")
        .then()
            .statusCode(200)
            .contentType(JSON)
            .body("name", is(payload.getString("name")))
            .body("description", is(payload.getString("description")))
            .body("id", isA(Number.class))
        .extract()
            .path("id");

        this.deleteRole(id);
    }

    @Test
    public void testCreateRoleWithTooShortName() {
        JsonObject payload = Json.createObjectBuilder()
                .add("name", "ab")
                .add("description", "test: testCreateAccountWithTooShortName")
                .build();

        given()
            .contentType(JSON)
            .body(payload.toString())
        .when()
            .post("/role")
        .then()
            .statusCode(422)
            .contentType(JSON)
            .body("key[0]", is("name"))
            .body("message[0]", is("Name needs to be between 3 and 255 characters!"))
            .body("value[0]", is(payload.getString("name")));
    }

    @Test
    public void testCreateRoleWithTooLongName() {
        JsonObject payload = Json.createObjectBuilder()
                .add("name", Helper.generateRandomString(256))
                .add("description", "test: testCreateRoleWithTooLongName")
                .build();

        given()
            .contentType(JSON)
            .body(payload.toString())
        .when()
            .post("/role")
        .then()
            .statusCode(422)
            .contentType(JSON)
            .body("key[0]", is("name"))
            .body("message[0]", is("Name needs to be between 3 and 255 characters!"))
            .body("value[0]", is(payload.getString("name")));
    }

    ////////////////////
    // Util functions //
    ////////////////////

    private void deleteRole(Integer id) {
        given()
            .pathParam("id", id)
        .when()
            .delete("/role/{id}")
        .then()
            .statusCode(200)
            .contentType(JSON)
            .body("name", is(any(String.class)))
            .body("description", is(any(String.class)))
            .body("id", is(id));
    }
}
