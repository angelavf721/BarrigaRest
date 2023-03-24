package br.ce.rest.test;

import br.ce.rest.core.Base;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class Auth extends Base {

    @Test
    public void acessarAPIsemTOKEN(){
        FilterableRequestSpecification specification = (FilterableRequestSpecification) RestAssured.requestSpecification;
        specification.removeHeader("Authorization");

        given()
        .when()
            .get("/contas")
        .then()
            .statusCode(401);
    }


}
