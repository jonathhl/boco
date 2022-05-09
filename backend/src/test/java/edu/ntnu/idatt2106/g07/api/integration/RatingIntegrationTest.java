package edu.ntnu.idatt2106.g07.api.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ntnu.idatt2106.g07.api.dto.listing.ListingDTO;
import edu.ntnu.idatt2106.g07.api.dto.rating.RatingDTO;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
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

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class RatingIntegrationTest {
    @Autowired
    private WebApplicationContext context;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        RestAssuredMockMvc.webAppContextSetup(context);
    }

    @Test
    @WithMockUser(username = "john@doe.org")
    @DisplayName("Ratings can be retrieved successfully")
    void testRatingsCanBeRetrievedSuccessfully() {
        when().get("/rating/john@doe.org").then().statusCode(200).body("average", Matchers.is(1.0F));
    }

    @Test
    @WithMockUser(username = "john@doe.org")
    @DisplayName("Rating can be added if none exists")
    void testRatingCanBeAddedSuccessfully() throws JsonProcessingException {
        String json = mapper.writeValueAsString(new RatingDTO(5, "Cool!", 102L));
        given().contentType(ContentType.JSON).body(json).when().put("/rating/102").then().statusCode(200).body("rating",
                Matchers.is(5));
    }

    @Test
    @WithMockUser(username = "john@doe.org")
    @DisplayName("Rating cannot be added if one exists")
    void testRatingCannotBeAddedIfOneExists() throws JsonProcessingException {
        String json = mapper.writeValueAsString(new RatingDTO(5, "Cool!", 101L));
        given().contentType(ContentType.JSON).body(json).when().put("/rating/101").then().statusCode(400);
    }
}
