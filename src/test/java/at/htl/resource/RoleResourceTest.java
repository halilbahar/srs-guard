package at.htl.resource;

import at.htl.util.Helper;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonArray;
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
                .add("permissions", Json.createArrayBuilder().add(
                        Json.createObjectBuilder().add("app", "test-app").add("stream", "test-stream")
                )).build();

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

    @Test
    public void testCreateRoleWithTooLongDescription() {
        JsonObject payload = Json.createObjectBuilder()
                .add("name", "test-role")
                .add("description", Helper.generateRandomString(256))
                .build();

        given()
            .contentType(JSON)
            .body(payload.toString())
        .when()
            .post("/role")
        .then()
            .statusCode(422)
            .contentType(JSON)
            .body("key[0]", is("description"))
            .body("message[0]", is("Description may not be longer than 255!"))
            .body("value[0]", is(payload.getString("description")));
    }

    @Test
    public void testGetRoleById() {
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

        given()
            .pathParam("id", id)
        .when()
            .get("/role/{id}")
        .then()
            .statusCode(200)
            .contentType(JSON)
            .body("name", is(payload.getString("name")))
            .body("description", is(payload.getString("description")))
            .body("id", is(id))
            .body("permissions", is(empty()));

        this.deleteRole(id);
    }

    @Test
    public void testGetNonExistingRole() {
        given()
            .pathParam("id", 999)
        .when()
            .get("/role/{id}")
        .then()
            .statusCode(404);
    }

    @Test
    public void testDeleteNonExistingRole() {
        given()
            .pathParam("id", 999)
        .when()
            .delete("/role/{id}")
        .then()
            .statusCode(404);
    }

    @Test
    public void addPermissionToNonExistingRole() {
        given()
            .contentType(JSON)
            .pathParam("id", 999)
        .when()
            .post("/role/{id}/permission")
        .then()
            .statusCode(404);
    }

    @Test
    public void addPermissionWithDuplicateAppStreams() {
        JsonObject rolePayload = Json.createObjectBuilder()
                .add("name", "test-role")
                .add("description", "test: addPermissionWithDuplicateAppStreams")
                .build();

        JsonArray payload = Json.createArrayBuilder()
                .add(Json.createObjectBuilder().add("app", "test-app").add("stream", "test-stream"))
                .add(Json.createObjectBuilder().add("app", "test-app").add("stream", "test-stream"))
                .build();

        int id = given()
            .contentType(JSON)
            .body(rolePayload.toString())
        .when()
            .post("/role")
        .then()
            .statusCode(200)
            .contentType(JSON)
            .body("name", is(rolePayload.getString("name")))
            .body("description", is(rolePayload.getString("description")))
            .body("id", isA(Number.class))
        .extract()
            .path("id");

        given()
            .contentType(JSON)
            .pathParam("id", id)
            .body(payload)
        .when()
            .post("role/{id}/permission")
        .then()
            .statusCode(422)
            .contentType(JSON)
            .body("app[0]", is("test-app"))
            .body("stream[0]", is("test-stream"));

        this.deleteRole(id);
    }

    @Test
    public void addExistingPermissionToRole() {
        JsonObject rolePayload = Json.createObjectBuilder()
                .add("name", "test-role")
                .add("description", "test: addExistingPermissionToRole")
                .build();

        int id = given()
            .contentType(JSON)
            .body(rolePayload.toString())
        .when()
            .post("/role")
        .then()
            .statusCode(200)
            .contentType(JSON)
            .body("name", is(rolePayload.getString("name")))
            .body("description", is(rolePayload.getString("description")))
            .body("id", isA(Number.class))
        .extract()
            .path("id");

        JsonArray payload = Json.createArrayBuilder()
                .add(Json.createObjectBuilder().add("app", "test-app").add("stream", "test-stream"))
                .build();

        given()
            .contentType(JSON)
            .pathParam("id", id)
            .body(payload.toString())
        .when()
            .post("/role/{id}/permission")
        .then()
            .statusCode(204);

        given()
            .contentType(JSON)
            .pathParam("id", id)
            .body(payload.toString())
        .when()
            .post("/role/{id}/permission")
        .then()
            .statusCode(409)
            .contentType(JSON)
            .body("app[0]", is("test-app"))
            .body("stream[0]", is("test-stream"));

        this.deleteRole(id);
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
