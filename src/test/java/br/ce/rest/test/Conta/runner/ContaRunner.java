package br.ce.rest.test.Conta.runner;

import br.ce.rest.core.Base;
import br.ce.rest.test.Conta.controller.ContaController;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ContaController.class
})
public class ContaRunner extends Base {

    @BeforeClass
    public static void login() {
        Map<String, String> login = new HashMap<>();
        login.put("email", "an@an");
        login.put("senha", "123");

        String TOKEN = given()
                .body(login)
                .when()
                .post("/signin")
                .then()
                .statusCode(200)
                .extract().path("token");
        RestAssured.requestSpecification.header("Authorization", "JWT " + TOKEN);

        RestAssured.get("reset").then().statusCode(200);
    }
}
