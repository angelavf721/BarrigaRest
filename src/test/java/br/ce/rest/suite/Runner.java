package br.ce.rest.suite;

import br.ce.rest.core.Base;
import br.ce.rest.test.Auth;
import br.ce.rest.test.Conta;
import br.ce.rest.test.MovimentacaoTest;
import br.ce.rest.test.Saldo;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        Conta.class,
        MovimentacaoTest.class,
        Saldo.class,
        Auth.class
})
public class Runner extends Base {
    @BeforeClass
    public static void login(){
        Map<String, String> login = new HashMap<>();
        login.put("email","an@an");
        login.put("senha","123");

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
