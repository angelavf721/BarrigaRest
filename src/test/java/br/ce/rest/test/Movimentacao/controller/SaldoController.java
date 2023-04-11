package br.ce.rest.test.Movimentacao.controller;


import br.ce.rest.core.Base;
import org.junit.Test;

import static br.ce.rest.utils.Utils.getConta;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class SaldoController extends Base {

    @Test
    public void valorSaldo(){
        Integer contaId = getConta("Conta para saldo");
        given()
        .when()
            .get("/saldo")
        .then()
            .statusCode(200)
            .body("find{it.conta_id == "+contaId+"}.saldo", is("534.00"));


    }
}
