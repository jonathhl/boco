package edu.ntnu.idatt2106.g07.api.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ntnu.idatt2106.g07.api.dto.listing.ListingDTO;
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

import javax.transaction.Transactional;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class ListingIntegrationTest {
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setup() {
        RestAssuredMockMvc.webAppContextSetup(context);
    }

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @Transactional
    @WithMockUser(username = "john@doe.org")
    @DisplayName("Listing can be created successfully")
    void testListingCanBeCreatedSuccessfully() throws JsonProcessingException {
        String json = mapper.writeValueAsString(
                new ListingDTO("Test", "This is for testing", 20.0, "1111", null, "0000", "john@doe.org", "John"));

        given().contentType(ContentType.JSON).body(json).when().post("/listing").then().statusCode(200)
                .body("title", Matchers.equalTo("Test")).body("description", Matchers.equalTo("This is for testing"));
    }

    @Test
    @WithMockUser(username = "john@doe.org")
    @DisplayName("Listing can be retrieved successfully")
    void testListingCanBeRetrievedSuccessfully() {
        when().get("/listing/101").then().log().ifValidationFails().statusCode(200).body("id", Matchers.is(101))
                .body("title", Matchers.is("Toothbrush"));
    }

    @Test
    @Transactional
    @WithMockUser(username = "john@doe.org")
    @DisplayName("Listing can be updated successfully")
    void testListingCanBeUpdatedSuccessfully() throws JsonProcessingException {
        String title = "Toothbrushes";
        String json = mapper.writeValueAsString(new ListingDTO(title, null, null, null, null, null, null, null));

        given().contentType(ContentType.JSON).body(json).when().put("/listing/101").then().statusCode(200).body("title",
                Matchers.equalTo(title));
    }

    @Test
    @Transactional
    @WithMockUser(username = "joe@doe.org")
    @DisplayName("Listing cannot be updated by wrong user")
    void testListingCannotBeUpdatedByWrongUser() throws JsonProcessingException {
        String json = mapper.writeValueAsString(new ListingDTO("", null, null, null, null, null, null, null));

        given().contentType(ContentType.JSON).body(json).when().put("/listing/101").then().statusCode(401)
                .body("message", Matchers.is("No permission to edit this listing."));
    }

    @Test
    @WithMockUser(username = "john@doe.org")
    @DisplayName("Listing cannot be updated with invalid info")
    void testListingCannotBeUpdatedWithInvalidInfo() {
        when().put("/listing/101").then().statusCode(400);
    }

    @Test
    @Transactional
    @WithMockUser(username = "john@doe.org")
    @DisplayName("Listing can be deleted successfully")
    void testListingCanBeDeletedSuccessfully() {
        when().delete("/listing/101").then().log().ifValidationFails().statusCode(200).body("message",
                Matchers.equalTo("listing: 101 deleted"));
    }

    @Test
    @WithMockUser(username = "john@doe.org")
    @DisplayName("Listings endpoint returns correct number of elements")
    void testGetListingsReturnsCorrectElements() {
        when().get("/listing").then().log().ifValidationFails().statusCode(200).body("$", Matchers.hasSize(3));
    }

    @Test
    @WithMockUser(username = "john@doe.org")
    @DisplayName("Search endpoint returns correct number of elements")
    void testGetListingsByQueryReturnsCorrectElements() {
        when().get("/listing/search/probably").then().log().ifValidationFails().statusCode(200).body("$",
                Matchers.hasSize(2));
    }

    @Test
    @WithMockUser(username = "john@doe.org")
    @DisplayName("Personal listings endpoint returns correct number of elements")
    void testGetPersonalListingsFromLoggedInUser() {
        when().get("/listing/user").then().log().ifValidationFails().statusCode(200).body("$", Matchers.hasSize(3));
    }
}
