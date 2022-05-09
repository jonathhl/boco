package edu.ntnu.idatt2106.g07.api.controller;

import edu.ntnu.idatt2106.g07.api.dto.MessageDTO;
import edu.ntnu.idatt2106.g07.api.dto.NotificationDTO;
import edu.ntnu.idatt2106.g07.api.dto.UnreadDTO;
import edu.ntnu.idatt2106.g07.api.dto.chat.ChatDTO;
import edu.ntnu.idatt2106.g07.api.dto.chat.ChatsDTO;
import edu.ntnu.idatt2106.g07.api.dto.chat.RichChatMessageDTO;
import edu.ntnu.idatt2106.g07.api.entity.Chat;
import edu.ntnu.idatt2106.g07.api.entity.Notification;
import edu.ntnu.idatt2106.g07.api.entity.User;
import edu.ntnu.idatt2106.g07.api.service.ChatService;
import edu.ntnu.idatt2106.g07.api.service.NotificationService;
import edu.ntnu.idatt2106.g07.api.service.RatingService;
import edu.ntnu.idatt2106.g07.api.service.RequestService;
import edu.ntnu.idatt2106.g07.api.service.UserService;
import edu.ntnu.idatt2106.g07.api.websocket.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller for managing chats and chat messages.
 *
 * @see Chat
 * @see ChatMessage
 */
@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private final NotificationService notificationService;
    private final UserService userService;
    private final RequestService requestService;
    private final RatingService ratingService;
    private final WebSocketService webSocketService;

    @Autowired
    public ChatController(ChatService chatService, NotificationService notificationService, UserService userService,
            RequestService requestService, RatingService ratingService, WebSocketService webSocketService) {
        this.chatService = chatService;
        this.notificationService = notificationService;
        this.userService = userService;
        this.requestService = requestService;
        this.ratingService = ratingService;
        this.webSocketService = webSocketService;
    }

    /**
     * Creates a new chat from the active user to the given user.
     * 
     * @param data
     *            User to create a chat to.
     * @param principal
     *            Active user.
     * 
     * @return New chat.
     */
    @PostMapping
    public ResponseEntity<Object> postChat(@RequestBody HashMap<String, String> data, Principal principal) {
        if (!data.containsKey("username")) {
            return ResponseEntity.badRequest().body(new MessageDTO("Request must contain 'username'"));
        }

        List<String> users = List.of(principal.getName(), data.get("username"));
        Optional<Chat> chat = chatService.createChat(users);

        if (chat.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageDTO("Unable to create chat"));
        }

        return ResponseEntity.ok(new ChatsDTO(chat.get()));
    }

    /**
     * Gets all chats associated with the active user.
     * 
     * @param principal
     *            Active user.
     * 
     * @return List of chats.
     */
    @GetMapping
    public ResponseEntity<Object> getChats(Principal principal) {
        List<Chat> chats = chatService.getChatsByUsername(principal.getName());
        Optional<User> optionalUser = userService.getUserByEmail(principal.getName());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO("User not found"));
        }

        // Remove active user from chat user.
        chats.forEach(it -> it.getUsers().remove(optionalUser.get()));

        return ResponseEntity.ok(chats.stream().map(ChatsDTO::new));
    }

    /**
     * Gets the chat with the given id.
     * 
     * @param id
     *            Chat to retrieve.
     * 
     * @return Chat.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getChat(@PathVariable Long id, Principal principal) {
        Optional<Chat> optionalChat = chatService.getChatById(id);
        Optional<User> optionalUser = userService.getUserByEmail(principal.getName());

        if (optionalChat.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO("Chat not found"));
        }

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO("User not found"));
        }

        Chat chat = optionalChat.get();
        if (chat.getUsers().stream().map(User::getEmail).noneMatch((it) -> it.equals(principal.getName()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageDTO("No permission to view"));
        }

        // Remove active user from chat user.
        chat.getUsers().remove(optionalUser.get());

        List<RichChatMessageDTO> messages = new ArrayList<>();

        // Add all requests where sender is current user and recipient is recipient.
        requestService.getAllRequestsRegardingUsers(optionalUser.get(), chat.getUsers().get(0)).stream()
                .map(RichChatMessageDTO::new).forEach(messages::add);

        // Add all ratings where sender is current user and recipient is recipient.
        ratingService.getAllRatingsRegardingUsers(optionalUser.get(), chat.getUsers().get(0)).stream()
                .map(RichChatMessageDTO::new).forEach(messages::add);

        ChatDTO dto = new ChatDTO(chat);
        dto.getMessages().addAll(messages);

        return ResponseEntity.ok(dto);
    }

    /**
     * Creates a message in the chat with the given id.
     * 
     * @param data
     *            Message to send.
     * @param id
     *            Chat to send message in.
     * @param principal
     *            Active user.
     * 
     * @return Updated chat.
     */
    @PostMapping("/{id}")
    public ResponseEntity<Object> postMessage(@RequestBody MessageDTO data, @PathVariable Long id,
            Principal principal) {
        Optional<Chat> optionalChat = chatService.getChatById(id);

        if (optionalChat.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO("Chat not found"));
        }

        Chat chat = optionalChat.get();

        if (chat.getUsers().stream().map(User::getEmail).noneMatch((it) -> it.equals(principal.getName()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageDTO("No permission to send message"));
        }

        return ResponseEntity.ok(new ChatDTO(chatService.postMessage(chat, data.getMessage(), principal.getName())));
    }

    /**
     * Gets the notification status for the chat with the given id.
     *
     * @param id
     *            Chat to get notification for.
     *
     * @return Notification status.
     */
    @GetMapping("/{id}/notification")
    public ResponseEntity<Object> getNotificationById(@PathVariable Long id) {
        List<Notification> chatNotification = notificationService.getById(id).stream().map(Notification::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new UnreadDTO(chatNotification.stream().anyMatch(it -> !it.isRead())));
    }

    /**
     * Updates all notifications for the given chat to be read.
     *
     * @param id
     *            Chat to mark as read.
     * @param principal
     *            Active user.
     *
     * @return Updated notifications.
     */
    @PutMapping("/{id}/notification")
    public ResponseEntity<Object> updateNotificationById(@PathVariable Long id, Principal principal) {
        Optional<Chat> optionalChat = chatService.getChatById(id);
        Optional<User> optionalUser = userService.getUserByEmail(principal.getName());

        if (optionalChat.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO("Chat not found"));
        }

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDTO("User not found"));
        }

        Chat chat = optionalChat.get();

        if (chat.getUsers().stream().noneMatch(u -> u.getEmail().equals(principal.getName()))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageDTO("No permission to edit this notification"));
        }

        User user = optionalUser.get();

        List<Notification> updatedNotifications = notificationService.markNotificationsOfChatAs(chat, user, true);

        // Trigger notification-update call for user
        webSocketService.notify(user.getEmail(), "notification_read");

        return ResponseEntity.ok(updatedNotifications.stream().map(NotificationDTO::new));
    }

}
