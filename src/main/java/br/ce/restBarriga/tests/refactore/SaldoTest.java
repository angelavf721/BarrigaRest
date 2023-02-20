package br.ce.restBarriga.tests.refactore;

import br.ce.restBarriga.core.BaseTest;
import org.junit.Test;

import static br.ce.restBarriga.DataUtilis.BarrigaUtils.getIdContaPeloNome;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class SaldoTest extends BaseTest {

    @Test
    public void deveCalcularSaldoContas(){
        Integer CONTA_ID = getIdContaPeloNome("Conta para saldo");
        given()
                .when()
                .get("/saldo")
                .then()
                .statusCode(200)
            .body("find{it.conta_id == "+CONTA_ID+"}.saldo",is("534.00"));
    }


}
