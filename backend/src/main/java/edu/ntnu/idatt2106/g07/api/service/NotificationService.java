package edu.ntnu.idatt2106.g07.api.service;

import edu.ntnu.idatt2106.g07.api.dto.NotificationDTO;
import edu.ntnu.idatt2106.g07.api.entity.Chat;
import edu.ntnu.idatt2106.g07.api.entity.Notification;
import edu.ntnu.idatt2106.g07.api.entity.User;
import edu.ntnu.idatt2106.g07.api.repository.NotificationRepository;
import edu.ntnu.idatt2106.g07.api.websocket.WebSocketService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing notifications.
 * 
 * @see Notification
 */
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final WebSocketService webSocketService;

    public NotificationService(NotificationRepository notificationRepository, WebSocketService webSocketService) {
        this.notificationRepository = notificationRepository;
        this.webSocketService = webSocketService;
    }

    /**
     * Creates a notification for the given user and chat.
     * 
     * @param user
     *            User receiving notification.
     * @param chat
     *            Chat causing notification.
     * 
     * @return New notification.
     */
    public Notification createNotification(User user, Chat chat) {
        Notification notification = new Notification(false, user, chat);

        // Create and send websocket notification
        webSocketService.notify(user.getEmail(), "new_chat");
        return notificationRepository.save(notification);
    }

    /**
     * Marks all notifications for the given chat as read/unread.
     * 
     * @param chat
     *            Chat to mark notifications for.
     * @param user
     *            User to mark notifications for.
     * @param read
     *            Mark as read or unread.
     * 
     * @return Updated notifications.
     */
    public List<Notification> markNotificationsOfChatAs(Chat chat, User user, boolean read) {
        List<Notification> notifications = notificationRepository.findAllByChatAndUser(chat, user);
        notifications.stream().filter(n -> n.isRead() != read).forEach(n -> n.setRead(read));
        return notificationRepository.saveAll(notifications);
    }

    /**
     * Gets all notifications for a given user.
     * 
     * @param email
     *            User email.
     * 
     * @return User notifications.
     */
    public List<Notification> getNotificationsByEmail(String email) {
        return notificationRepository.findAllByUser_Email(email);
    }

    /**
     * Gets a notification by the given id.
     * 
     * @param id
     *            Notification to retrieve.
     * 
     * @return Notification.
     */
    public Optional<Notification> getById(Long id) {
        return notificationRepository.findById(id);
    }
}
