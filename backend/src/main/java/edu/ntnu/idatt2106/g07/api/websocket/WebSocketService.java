package edu.ntnu.idatt2106.g07.api.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Service to send user sessions update notifications.
 */
@Service
public class WebSocketService {
    private final SimpMessagingTemplate template;

    @Autowired
    public WebSocketService(SimpMessagingTemplate template) {
        this.template = template;
    }

    /**
     * Send a user session an empty message to tell it to update.
     * 
     * @param username
     *            User that has pending data.
     */
    public void notify(String username) {
        template.convertAndSend("/queue/" + username, "");
    }

    /**
     * Send a user session a message to tell it to update.
     * 
     * @param username
     *            User that has pending data.
     * @param message
     *            Message to send to the client.
     */
    public void notify(String username, String message) {
        template.convertAndSend("/queue/" + username, message);
    }
}
