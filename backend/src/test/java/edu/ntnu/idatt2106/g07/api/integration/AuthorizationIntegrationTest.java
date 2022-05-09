package edu.ntnu.idatt2106.g07.api.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ntnu.idatt2106.g07.api.dto.user.AuthorizationDTO;
import edu.ntnu.idatt2106.g07.api.dto.MessageDTO;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
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

import java.io.IOException;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class AuthorizationIntegrationTest {
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setup() {
        RestAssuredMockMvc.webAppContextSetup(context);
    }

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("Token can be retrieved by providing username and password")
    void testTokenCanBeRetrieved() throws IOException {
        String json = mapper.writeValueAsString(new AuthorizationDTO("john@doe.org", "password"));

        given().contentType(ContentType.JSON).body(json).when().post("/auth").then().log().ifValidationFails()
                .status(HttpStatus.OK).contentType(ContentType.JSON).body(not(blankOrNullString()));
    }

    @Test
    @WithMockUser(username = "john@doe.org")
    @DisplayName("Success message is received when user is authenticated")
    void testEndpointReturnsCorrectMessageWhenAuthenticated() throws JsonProcessingException {
        String json = mapper.writeValueAsString(new MessageDTO("Successfully authenticated as john@doe.org."));

        when().get("/auth").then().log().ifValidationFails().status(HttpStatus.OK).contentType(ContentType.JSON)
                .body(equalTo(json));
    }
}
