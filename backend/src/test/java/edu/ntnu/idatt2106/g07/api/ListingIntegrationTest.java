package edu.ntnu.idatt2106.g07.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ntnu.idatt2106.g07.api.dto.ListingDTO;
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

import javax.transaction.Transactional;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class ListingIntegrationTest {
    @Autowired
    private WebApplicationContext context;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        RestAssuredMockMvc.webAppContextSetup(context);
    }

    @Transactional
    @WithMockUser(username = "john@doe.org")
    @Test
    @DisplayName("Listing is saved with correct information")
    void testEndpointReturnsStatusOkWhenListingIsSaved() throws JsonProcessingException {
        String json = mapper.writeValueAsString(
                new ListingDTO("testListing", "This is just a testlisting", 40.0, "7014", null, null, "john@doe.org"));
        given().contentType(ContentType.JSON).body(json).when().post("/listing").then().statusCode(200).body("title",
                equalTo("testListing"));
    }

    @WithMockUser(username = "john@doe.org")
    @Test
    @DisplayName("Error status is returned when invalid listing is posted")
    void testEndpointReturnErrorStatus() {
        given().contentType(ContentType.JSON).when().post("listing").then().statusCode(400);
    }
}
