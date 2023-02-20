package br.ce.restBarriga.tests.refactore;

import br.ce.restBarriga.DataUtilis.DataUtils;
import br.ce.restBarriga.core.BaseTest;
import br.ce.restBarriga.tests.Movimentacao;
import org.junit.Test;


import static br.ce.restBarriga.DataUtilis.BarrigaUtils.getIdContaPeloNome;
import static br.ce.restBarriga.DataUtilis.BarrigaUtils.getIdMuviPeloNome;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;

public class MovimentacaoTest extends BaseTest {

    @Test
    public void deveInserirMovimentaco(){
        Movimentacao movi = getMovimentacao();
         given()
                .body(movi)
                .when()
                .post("/transacoes")
                .then()
                .statusCode(201)
                .extract().path("id");
    }
    @Test
    public void deveValidarCampoObrigatorioMovimentacao(){
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
    public void naoDeveInserirDataMovimentacaoFutura(){
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
    public void naoDeveRemoverContaMovimentacao(){
        Integer CONTA_ID = getIdContaPeloNome("Conta com movimentacao");
        given()
                .pathParam("id", CONTA_ID)
                .when()
                .delete("/contas/{id}")
                .then()
                .statusCode(500)
                .body("constraint",is("transacoes_conta_id_foreign"));
    }

    @Test
    public void deveRemoverMovimentacao(){
        Integer MOVI_ID = getIdMuviPeloNome("Movimentacao para exclusao");
        given()
                .pathParam("id", MOVI_ID)
                .when()
                .delete("/transacoes/{id}")
                .then()
                .statusCode(204);
    }




    private Movimentacao getMovimentacao(){
        Movimentacao movi = new Movimentacao();
        movi.setConta_id(getIdContaPeloNome("Conta para movimentacoes"));
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
