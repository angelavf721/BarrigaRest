package br.ce.restBarriga.tests.refactore;

import br.ce.restBarriga.core.BaseTest;
import org.junit.Test;

import static br.ce.restBarriga.DataUtilis.BarrigaUtils.getIdContaPeloNome;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class ContasTest extends BaseTest {

    @Test
    public void deveIncluirContaComSucesso(){
        given()
                .body("{\"nome\": \"Conta Nova\"}")
                .when()
                .post("/contas")
                .then()
                .statusCode(201);
    }

    @Test
    public void deveEditarContaComSucesso(){
        Integer CONTA_ID = getIdContaPeloNome("Conta para alterar");
        given()
                .body("{\"nome\": \"Conta alterada\"}")
                .pathParam("id", CONTA_ID)
                .when()
                .put("/contas/{id}")
                .then()
                .statusCode(200)
                .body("nome",is("Conta alterada"));
    }

    @Test
    public void naoDeveInserirContaMesmoNome(){
        given()
                .body("{\"nome\": \"Conta mesmo nome\"}")
                .when()
                .post("/contas")
                .then()
                .statusCode(400)
                .body("error",is("Já existe uma conta com esse nome!"));
    }

}
