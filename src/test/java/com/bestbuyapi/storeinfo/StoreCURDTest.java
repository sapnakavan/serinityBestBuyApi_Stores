package com.bestbuyapi.storeinfo;


import com.bestbuyapi.constants.EndPoints;
import com.bestbuyapi.model.StorePojo;
import com.bestbuyapi.testbase.TestBase;
import com.bestbuyapi.utils.TestUtils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasValue;



public class StoreCURDTest extends TestBase {

    static String name ="ted"+TestUtils.getRandomValue();
    static String type = "dyson"+TestUtils.getRandomValue();
    static String add = "22 dev"+TestUtils.getRandomValue();
    static String state = "united kingdom";
    static String zip = "ha8 ulb";
    static String city = "london";
    static Double lat = 20.80;

    static int storeID;



    @Title("This is will get all information of all store")
    @Test
    public void test001(){
       SerenityRest. given().log().all()
                .when().get()
                .then()
                .log().all()
                .statusCode(200);
    }
    @Title("This will create new store")
    @Test
    public void test002(){
        StorePojo storePojo = new StorePojo();
        storePojo.setName(name);
        storePojo.setType(type);
        storePojo.setAddress(add);
        storePojo.setCity(city);
        storePojo.setState(state);
        storePojo.setZip(zip);
        storePojo.setLat(lat);


      SerenityRest. given().log().all()
                .header("Content-Type","application/json")
                .body(storePojo)
                .when()
                .post()
             .then().log().all().statusCode(201);

    }
    @Title("verify if store was created")
    @Test
    public void test003(){
        String p1="data.findAll{it.name='";
        String p2="'}.get(0)";
        HashMap<String,Object> storemap =SerenityRest.given().log().all()
                .when()
                .get()
                .then().statusCode(200)
                .extract()
                .path(p1+name+p2);
        Assert.assertThat(storemap,hasValue(name));
        storeID =(int) storemap.get("id");
    }
    @Title("update the store and verify the update information")
    @Test
    public void test004(){
        name=name+"updated";
        type = type +"updated";
        StorePojo storePojo = new StorePojo();
        storePojo.setName(name);
        storePojo.setType(type);
        storePojo.setAddress(add);
        storePojo.setCity(city);
        storePojo.setState(state);
        storePojo.setZip(zip);
        storePojo.setLat(lat);
        SerenityRest. given().log().all()
                .header("Content-Type","application/json")
                .pathParam("storeID",storeID)
                .body(storePojo)
                .when()
                .put(EndPoints.UPDATE_SINGLE_STORE_BY_ID)
                .then().log().all().statusCode(200);
        String p1="data.findAll{it.name='";
        String p2="'}.get(0)";
        HashMap<String,Object> storemap =SerenityRest.given().log().all()
                .when()
                .get()
                .then().statusCode(200)
                .extract()
                .path(p1+name+p2);
        Assert.assertThat(storemap,hasValue(name));
        Assert.assertThat(storemap,hasValue(type));
    }


    @Title("Delete the store and verify if the store is deleted")
    @Test
    public void test005(){
        SerenityRest. given()
                .pathParam("storeID",storeID)
                .when()
                .delete(EndPoints.DELETE_SINGLE_STORE_BY_ID)
                .then()
                .statusCode(200);
       SerenityRest. given().log().all()
                .pathParam("storeID",storeID)
                .when()
                .get(EndPoints.GET_SINGLE_STORE_BY_ID)
                .then()
                .statusCode(404);

    }

}
