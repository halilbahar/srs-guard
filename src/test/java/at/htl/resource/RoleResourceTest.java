package at.htl.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.json.JsonObject;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.any;

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

    ////////////////////
    // Util functions //
    ////////////////////

    private Integer createRole(JsonObject payload) {
        Number id = given()
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

        return id.intValue();
    }

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
