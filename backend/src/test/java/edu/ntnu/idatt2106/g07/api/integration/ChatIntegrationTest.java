package edu.ntnu.idatt2106.g07.api.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ntnu.idatt2106.g07.api.dto.NotificationDTO;
import edu.ntnu.idatt2106.g07.api.entity.Notification;
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

import java.util.Map;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class ChatIntegrationTest {
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setup() {
        RestAssuredMockMvc.webAppContextSetup(context);
    }

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @WithMockUser(username = "john@doe.org")
    @DisplayName("Chat can be created successfully")
    void testChatCanBeCreatedSuccessfully() throws JsonProcessingException {
        String username = "jane@doe.org";
        String json = mapper.writeValueAsString(Map.of("username", username));

        given().contentType(ContentType.JSON).body(json).when().post("/chat").then().statusCode(200).body("users",
                Matchers.hasSize(2));
    }

    @Test
    @WithMockUser(username = "john@doe.org")
    @DisplayName("Chat can be retrieved successfully")
    void testChatCanBeRetrievedSuccessfully() {
        when().get("/chat/101").then().statusCode(200).contentType(ContentType.JSON).body("users", Matchers.hasSize(1));
    }

    @Test
    @WithMockUser(username = "joe@doe.org")
    @DisplayName("Chat cannot be retrieved by unexpected user")
    void testChatCannotBeRetrievedWhenUnauthorized() {
        when().get("/chat/101").then().statusCode(403).body("message",
                Matchers.containsString("No permission to view"));
    }

    @Test
    @WithMockUser(username = "joe@doe.org")
    @DisplayName("Chat message cannot be sent by unexpected user")
    void testMessageCannotBeSentWhenUnauthorized() throws JsonProcessingException {
        String json = mapper.writeValueAsString(Map.of("message", "Hello!"));
        given().contentType(ContentType.JSON).body(json).when().post("/chat/101").then().statusCode(403).body("message",
                Matchers.containsString("No permission to send message"));
    }

    @Test
    @WithMockUser(username = "john@doe.org")
    @DisplayName("Chat message can be sent successfully.")
    void testMessageCanBeSentWhenAuthorized() throws JsonProcessingException {
        String json = mapper.writeValueAsString(Map.of("message", "Hello!"));
        given().contentType(ContentType.JSON).body(json).when().post("/chat/101").then().statusCode(200).body("id",
                Matchers.is(101));
    }

    @Test
    @WithMockUser(username = "john@doe.org")
    @DisplayName("Notification status is updated successfully")
    void testNotificationStatusIsUpdatedSuccessfully() throws JsonProcessingException {
        boolean read = true;
        String json = mapper.writeValueAsString(new NotificationDTO(read, "test@test.org", 1L));
        given().contentType(ContentType.JSON).body(json).when().put("/chat/101/notification").then().statusCode(200);
    }

    @Test
    @WithMockUser(username = "joe@doe.org")
    @DisplayName("Notification status cannot be updated by wrong user")
    void testNotificationStatusCannotBeUpdatedByWrongUser() throws JsonProcessingException {
        boolean read = true;
        String json = mapper.writeValueAsString(new NotificationDTO(read, "test@test.org", 1L));
        given().contentType(ContentType.JSON).body(json).when().put("/chat/101/notification").then().statusCode(401)
                .body("message", Matchers.is("No permission to edit this notification"));
    }

    @Test
    @WithMockUser(username = "john@doe.org")
    @DisplayName("Notification status can be retrieved successfully")
    void testNotificationStatusIsRetrievedSuccessfully() {
        when().get("/chat/101/notification").then().statusCode(200);
    }
}
