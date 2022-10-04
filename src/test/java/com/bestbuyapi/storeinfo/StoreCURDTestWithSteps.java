package com.bestbuyapi.storeinfo;

import com.bestbuyapi.testbase.TestBase;
import com.bestbuyapi.utils.TestUtils;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static org.hamcrest.Matchers.hasValue;


@RunWith(SerenityRunner.class)
public class StoreCURDTestWithSteps extends TestBase {
    static String name ="ted"+ TestUtils.getRandomValue();
    static String type = "dyson"+TestUtils.getRandomValue();
    static String add = "22 dev"+TestUtils.getRandomValue();
    static String state = "united kingdom";
    static String zip = "ha8 ulb";
    static String city = "london";
    static Double lat = 20.80;

    static int storeID;
    @Steps
    StoreSteps storeSteps;

    @Title("This will create new store")
    @Test
    public void test001(){
        ValidatableResponse response = storeSteps.createstore(name,type,add,state,zip,city,lat);
        response.log().all().statusCode(201);
    }

    @Title("verify if store was created")
    @Test
    public void test002(){
        HashMap<String,Object> storemap = storeSteps.getstoreinfobyname(name);
        Assert.assertThat(storemap,hasValue(name));
        storeID =(int) storemap.get("id");
        System.out.println(storeID);

    }

    @Title("update the store and verify the update information")
    @Test
    public void test003(){
        name=name+"updated";
        type = type +"updated";
        storeSteps.updatestore(storeID,name,type,add,state,zip,city,lat);
        HashMap<String,Object> storemap = storeSteps.getstoreinfobyname(name);
        Assert.assertThat(storemap,hasValue(name));
        Assert.assertThat(storemap,hasValue(type));
    }

    @Title("Delete the store and verify if the store is deleted")
    @Test
    public void test004(){
        storeSteps.deletestore(storeID).log().all().statusCode(200);
        storeSteps.verifystoreDeleted(storeID).log().all().statusCode(404);
    }
}
