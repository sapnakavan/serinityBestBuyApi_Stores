package com.bestbuyapi.storeinfo;


import com.bestbuyapi.constants.EndPoints;
import com.bestbuyapi.model.StorePojo;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;

import java.util.HashMap;

import static org.hamcrest.Matchers.hasValue;

public class StoreSteps {
static  int storeID;

@Step("creating store with name:{0} ,type:{1},address:{2},state:{3},zip:{4},city:{5},lat:{6}")
    public ValidatableResponse createstore (String name ,String type ,String add ,String state,String zip ,String city,Double lat){
    StorePojo storePojo = new StorePojo();
    storePojo.setName(name);
    storePojo.setType(type);
    storePojo.setAddress(add);
    storePojo.setCity(city);
    storePojo.setState(state);
    storePojo.setZip(zip);
    storePojo.setLat(lat);
  return   SerenityRest. given().log().all()
            .header("Content-Type","application/json")
            .body(storePojo)
            .when()
            .post()
            .then().log().all().statusCode(201);
}
@Step("getting store info by name :{0}")
    public HashMap<String,Object> getstoreinfobyname (String name){
    String p1="data.findAll{it.name='";
    String p2="'}.get(0)";
    return SerenityRest.given().log().all()
            .when()
            .get()
            .then().statusCode(200)
            .extract()
            .path(p1+name+p2);

}
    @Step("update store with storeID :{0} name:{1} ,type:{2},address:{3},state:{4},zip:{5},city:{6},lat:{7}")
    public ValidatableResponse updatestore (int storeID,String name ,String type ,String add ,String state,String zip ,String city,Double lat){
        StorePojo storePojo = new StorePojo();
        storePojo.setName(name);
        storePojo.setType(type);
        storePojo.setAddress(add);
        storePojo.setCity(city);
        storePojo.setState(state);
        storePojo.setZip(zip);
        storePojo.setLat(lat);
        return  SerenityRest. given().log().all()
                .header("Content-Type","application/json")
                .pathParam("storeID",storeID)
                .body(storePojo)
                .when()
                .put(EndPoints.UPDATE_SINGLE_STORE_BY_ID)
                .then();

    }
    @Step("Delete store with storeID :{0} ")
    public ValidatableResponse deletestore (int storeID){
    return SerenityRest. given()
            .pathParam("storeID",storeID)
            .when()
            .delete(EndPoints.DELETE_SINGLE_STORE_BY_ID)
            .then();
    }

    @Step("Verify store has been deleted for storeID: {0}")
    public ValidatableResponse verifystoreDeleted(int storeID){
    return SerenityRest. given().log().all()
            .pathParam("storeID",storeID)
            .when()
            .get(EndPoints.GET_SINGLE_STORE_BY_ID)
            .then();
    }
}
