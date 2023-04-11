package br.ce.rest.test.Conta.controller;


import br.ce.rest.core.Base;
import br.ce.rest.utils.Utils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContaController extends Base {
    @Test
    public void t01_inserirConta(){
        given()
            .body("{\"nome\": \"Conta nova\"}")
        .when()
            .post("/contas")
        .then()
            .statusCode(201);
    }
  @Test
    public void t02_alterarConta(){
        Integer idConta = Utils.getConta("Conta para alterar");
        given()
            .body("{\"nome\": \"Conta alterada\"}")
                .pathParam("id", idConta)
        .when()
            .put("/contas/{id}")
        .then()
            .statusCode(200)
            .body("nome", is("Conta alterada"));
    }
  @Test
    public void t03_naoDeveInserirContaMesmoNome(){
        given()
                .body("{\"nome\": \"Conta mesmo nome\"}")
                .when()
                .post("/contas")
                .then()
                .statusCode(400)
                .body("error", is("Já existe uma conta com esse nome!"));
    }

    @Test
    public void t04_excluirConta(){
        Integer idConta = Utils.getConta("Conta alterada");
        given()
                .pathParam("id", idConta)
                .when()
                .delete("/contas/{id}")
                .then()
                .statusCode(204);
    }
}
