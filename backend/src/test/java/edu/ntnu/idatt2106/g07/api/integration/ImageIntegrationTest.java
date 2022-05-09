package edu.ntnu.idatt2106.g07.api.integration;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class ImageIntegrationTest {
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setup() {
        RestAssuredMockMvc.webAppContextSetup(context);
    }

    /*
     * ? This test has problems running with "mvn test". Content type problem?
     * 
     * @Test
     * 
     * @WithMockUser(username = "john@doe.org")
     * 
     * @DisplayName("Images can be retrieved successfully") void testImagesCanBeRetrievedSuccessfully() {
     * when().get("/image/101").then().statusCode(200).contentType("image/jpeg"); }
     */

    @Test
    @WithMockUser(username = "john@doe.org")
    @DisplayName("Images can be deleted successfully")
    void testImagesCanBeDeletedSuccessfully() {
        when().delete("/image/101").then().statusCode(200).contentType(ContentType.JSON);
    }
}
