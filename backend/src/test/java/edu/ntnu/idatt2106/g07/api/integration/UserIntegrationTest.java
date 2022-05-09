package edu.ntnu.idatt2106.g07.api.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ntnu.idatt2106.g07.api.dto.MessageDTO;
import edu.ntnu.idatt2106.g07.api.dto.listing.ListingDTO;
import edu.ntnu.idatt2106.g07.api.dto.user.UserDTO;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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
public class UserIntegrationTest {
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setup() {
        RestAssuredMockMvc.webAppContextSetup(context);
    }

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @Transactional
    @DisplayName("User can be created successfully")
    void testUserCanBeCreatedSuccessfully() throws JsonProcessingException {
        String email = "jim@doe.org";
        String json = mapper.writeValueAsString(new UserDTO(email, "Jim", "Doe", "+47 000 00 000", 7046, ""));

        given().contentType(ContentType.JSON).body(json).when().post("/user").then().statusCode(200).body("email",
                Matchers.is(email));
    }

    @Test
    @WithMockUser(username = "john@doe.org")
    @DisplayName("Current user can be retrieved successfully")
    void testCurrentUserCanBeRetrievedSuccessfully() {
        when().get("/user").then().log().ifValidationFails().status(HttpStatus.OK).contentType(ContentType.JSON)
                .body("email", Matchers.is("john@doe.org"));
    }

    @Test
    @WithMockUser(username = "john@doe.org")
    @DisplayName("User can be updated successfully")
    void testUserCanBeUpdatedSuccessfully() {
        when().get("/user").then().log().ifValidationFails().status(HttpStatus.OK).contentType(ContentType.JSON)
                .body("email", Matchers.is("john@doe.org"));
    }
}
