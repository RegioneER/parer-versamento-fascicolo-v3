package it.eng.parer.fascicolo.runner.rest;

import static io.restassured.RestAssured.given;
import static it.eng.parer.fascicolo.runner.util.EndPointCostants.URL_GET_STATUS;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import it.eng.parer.Profiles;

@QuarkusTest
@TestProfile(Profiles.EndToEnd.class)
class StatusEndpointTest {

    @Test
    @TestSecurity(user = "test_microservizi", roles = { "admin" })
    void success() {
        given().when().get(URL_GET_STATUS).then().statusCode(200).body("$", hasKey("status")).body("status",
                not(empty()));
    }

    @Test
    @TestSecurity(authorizationEnabled = true)
    void authRequest() {
        given().when().get(URL_GET_STATUS).then().statusCode(401);
    }

    @Test
    @TestSecurity(user = "fakeuser", roles = { "not_admin" })
    void notAuthRequest() {
        given().when().get(URL_GET_STATUS).then().statusCode(403);
    }
}
