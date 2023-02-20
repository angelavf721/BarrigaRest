package br.ce.restBarriga.tests.refactore.suite;

import br.ce.restBarriga.core.BaseTest;
import br.ce.restBarriga.tests.refactore.AuthTest;
import br.ce.restBarriga.tests.refactore.ContasTest;
import br.ce.restBarriga.tests.refactore.MovimentacaoTest;
import br.ce.restBarriga.tests.refactore.SaldoTest;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ContasTest.class,
        MovimentacaoTest.class,
        SaldoTest.class,
        AuthTest.class
})

public class Runner_Suite extends BaseTest {
    @BeforeClass
    public static void login(){
        Map<String, String > login = new HashMap<>();
        login.put("email","an@an");
        login.put("senha","123");

        String TOKEN = given()
                .body(login)
                .when()
                .post("/signin")
                .then()
                .statusCode(200)
                .extract().path("token");
        RestAssured.requestSpecification .header("Authorization", "JWT " + TOKEN);

        RestAssured.get("/reset").then().statusCode(200);
    }
}
