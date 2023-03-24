package br.ce.rest.test;

import br.ce.rest.core.Base;
import org.junit.Test;

import static br.ce.rest.utils.Utils.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;

public class MovimentacaoTest extends Base {

    @Test
    public void criarMovimentacaoComSucesso(){
        MovimentacaoClass movi = getMovimentacao();
        given()
            .body(movi)
        .when()
            .post("/transacoes")
        .then()
            .statusCode(201);
    }

    @Test
    public void valaidarCamposObrigatorios(){
        given()
        .when()
            .post("/transacoes")
        .then()
            .statusCode(400)
            .body("$",hasSize(8))
            .body("msg", hasItems("Data da Movimentação é obrigatório",
                    "Data do pagamento é obrigatório", "Descrição é obrigatório",
                    "Interessado é obrigatório","Valor é obrigatório",
                    "Valor deve ser um número","Conta é obrigatório",
                    "Situação é obrigatório"));
    }
    @Test
    public void dataMovimentacaoFutura(){
        MovimentacaoClass movi = getMovimentacao();
        movi.setData_transacao(dataDiferencaDias(5));
        given()
            .body(movi)
        .when()
            .post("/transacoes")
        .then()
            .statusCode(400)
            .body("$",hasSize(1))
            .body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"));
    }
    @Test
    public void naoRemoverContaMovimentacao(){
      Integer contaId = getConta("Conta com movimentacao");
      given()
            .pathParam("id", contaId)
      .when()
            .delete("/contas/{id}")
      .then()
           .statusCode(500)
           .body("constraint",is("transacoes_conta_id_foreign"));
    }

    @Test
    public void RemoverMovimentacao(){
      Integer movi = getMovimentacaoDes("Movimentacao para exclusao");
      given()
            .pathParam("id", movi)
      .when()
            .delete("/transacoes/{id}")
      .then()
           .statusCode(204);
    }



    public MovimentacaoClass getMovimentacao(){
        MovimentacaoClass movi = new MovimentacaoClass();
        movi.setConta_id(getConta("Conta para movimentacoes"));
        movi.setDescricao("Sou a desc, prazer");
        movi.setEnvolvido("Envolvido da desc");
        movi.setTipo("REC");
        movi.setData_transacao(dataDiferencaDias(-1));
        movi.setData_pagamento(dataDiferencaDias(5));
        movi.setValor(100f);
        movi.setStatus(true);
        return movi;
    }

}
