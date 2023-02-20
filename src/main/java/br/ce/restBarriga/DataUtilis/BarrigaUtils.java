package br.ce.restBarriga.DataUtilis;

import io.restassured.RestAssured;

public class BarrigaUtils {
    public static Integer getIdContaPeloNome(String nome){
        return RestAssured.get("/contas?nome="+nome).then().extract().path("id[0]");
    }

    public static Integer getIdMuviPeloNome(String desc){
        return RestAssured.get("/transacoes?desc="+desc).then().extract().path("id[0]");
    }
}
