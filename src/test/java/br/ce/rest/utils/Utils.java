package br.ce.rest.utils;


import io.restassured.RestAssured;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.google.gson.internal.bind.util.ISO8601Utils.format;

public class Utils {
    public static Integer getConta(String nome) {
        return RestAssured.get("/contas?nome="+nome).then().extract().path("id[0]");
    }
    public static Integer getMovimentacaoDes(String desc){
        return RestAssured.get("/transacoes?desc="+desc).then().extract().path("id[0]");
    }


    public static String dataDiferencaDias(Integer qtdDias){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH,qtdDias);
        return getDataFormatada(cal.getTime());
    }

    public static String getDataFormatada(Date data){
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(data);
    }
}