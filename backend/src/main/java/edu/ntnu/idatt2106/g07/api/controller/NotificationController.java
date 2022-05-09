package edu.ntnu.idatt2106.g07.api.controller;

import edu.ntnu.idatt2106.g07.api.dto.NotificationDTO;
import edu.ntnu.idatt2106.g07.api.dto.UnreadDTO;
import edu.ntnu.idatt2106.g07.api.entity.Notification;
import edu.ntnu.idatt2106.g07.api.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing notifications.
 * 
 * @see Notification
 */
@RestController
@CrossOrigin
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Gets notification status for the active user.
     * 
     * @param principal
     *            Active user.
     * 
     * @return Notification status.
     */
    @GetMapping
    public ResponseEntity<Object> getStatus(Principal principal) {
        List<NotificationDTO> notification = notificationService.getNotificationsByEmail(principal.getName()).stream()
                .map(NotificationDTO::new).collect(Collectors.toList());

        return ResponseEntity.ok(new UnreadDTO(notification.stream().anyMatch(it -> !it.isRead())));
    }
}
