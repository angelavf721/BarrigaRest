package br.ce.restBarriga.tests;

import br.ce.restBarriga.DataUtilis.DataUtils;
import br.ce.restBarriga.core.BaseTest;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BarrigaTest extends BaseTest {



    private static String CONTA_NAME = "Conta " + System.nanoTime();
    private static Integer CONTA_ID;
    private static Integer MOVI_ID;

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

    }


    @Test
    public void t02_deveIncluirContaComSucesso(){
        CONTA_ID = given()
            .body("{\"nome\": \""+CONTA_NAME+"\"}")
       .when()
            .post("/contas")
       .then()
            .statusCode(201)
            .extract().path("id");
    }

    @Test
    public void t03_deveEditarContaComSucesso(){
       given()
            .body("{\"nome\": \""+CONTA_NAME+" alterada\"}")
               .pathParam("id", CONTA_ID)
       .when()
            .put("/contas/{id}")
       .then()
            .statusCode(200)
            .body("nome",is(CONTA_NAME +" alterada"));
    }

    @Test
    public void t04_naoDeveInserirContaMesmoNome(){
       given()
            .body("{\"nome\": \""+CONTA_NAME+" alterada\"}")
       .when()
            .post("/contas")
       .then()
            .statusCode(400)
            .body("error",is("Já existe uma conta com esse nome!"));
    }

    @Test
    public void t05_deveInserirMovimentaco(){
        Movimentacao movi = getMovimentacao();
        MOVI_ID = given()
            .body(movi)
       .when()
            .post("/transacoes")
       .then()
            .statusCode(201)
               .extract().path("id");
    }
    @Test
    public void t06_deveValidarCampoObrigatorioMovimentacao(){
        given()
                .body("{}")
                .when()
                .post("/transacoes")
                .then()
                .statusCode(400)
                .body("$",hasSize(8))
                .body("msg",hasItems(
                        "Data da Movimentação é obrigatório",
                        "Data do pagamento é obrigatório",
                        "Descrição é obrigatório",
                        "Interessado é obrigatório",
                        "Valor é obrigatório",
                        "Valor deve ser um número",
                        "Conta é obrigatório",
                        "Situação é obrigatório"
                ));
    }
    @Test
    public void t07_naoDeveInserirDataMovimentacaoFutura(){
        Movimentacao movi = getMovimentacao();
        movi.setData_transacao(DataUtils.getDataDiferencaDias(2));
        given()
                .body(movi)
                .when()
                .post("/transacoes")
                .then()
                .statusCode(400)
                .body("$",hasSize(1))
                .body("msg",hasItem(
                        "Data da Movimentação deve ser menor ou igual à data atual"
                ));
    }
    @Test
    public void t08_naoDeveRemoverContaMovimentacao(){
        given()
                .pathParam("id", CONTA_ID)
                .when()
                .delete("/contas/{id}")
                .then()
                .statusCode(500)
                .body("constraint",is("transacoes_conta_id_foreign"));
    }
    @Test
    public void t09_deveCalcularSaldoContas(){
        given()
                .when()
                .get("/saldo")
                .then()
                .statusCode(200)
                .body("saldo",hasItem("100.00"));
    }

    @Test
    public void t10_deveRemoverMovimentacao(){
        given()
                .pathParam("id", MOVI_ID)
                .when()
                .delete("/transacoes/{id}")
                .then()
                .statusCode(204);
    }
    @Test
    public void t11_naoDeveAcessarAPISemToken(){
        FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
        req.removeHeader("Authorization");

        given()
                .when()
                .get("/contas")
                .then()
                .statusCode(401);
    }

    private Movimentacao getMovimentacao(){
        Movimentacao movi = new Movimentacao();
        movi.setConta_id(CONTA_ID);
        movi.setDescricao("Sou a desc, prazer");
        movi.setEnvolvido("Envolvido da desc");
        movi.setTipo("REC");
        movi.setData_transacao(DataUtils.getDataDiferencaDias(-1));
        movi.setData_pagamento(DataUtils.getDataDiferencaDias(5));
        movi.setValor(100f);
        movi.setStatus(true);
        return movi;
    }
}
