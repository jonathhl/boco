package edu.ntnu.idatt2106.g07.api.service;

import edu.ntnu.idatt2106.g07.api.entity.Chat;
import edu.ntnu.idatt2106.g07.api.entity.ChatMessage;
import edu.ntnu.idatt2106.g07.api.entity.User;
import edu.ntnu.idatt2106.g07.api.repository.ChatMessageRepository;
import edu.ntnu.idatt2106.g07.api.repository.ChatRepository;
import edu.ntnu.idatt2106.g07.api.repository.UserRepository;
import edu.ntnu.idatt2106.g07.api.websocket.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for managing chats and chat messages.
 * 
 * @see Chat
 * @see ChatMessage
 */
@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final WebSocketService webSocketService;
    private final NotificationService notificationService;

    @Autowired
    public ChatService(ChatRepository chatRepository, ChatMessageRepository chatMessageRepository,
            UserRepository userRepository, WebSocketService webSocketService, NotificationService notificationService) {
        this.chatRepository = chatRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
        this.webSocketService = webSocketService;
        this.notificationService = notificationService;
    }

    /**
     * Creates a new chat with the given users.
     *
     * @param users
     *            Users in chat.
     *
     * @return New chat.
     */
    public Optional<Chat> createChat(List<String> users) {
        if (users.stream().anyMatch((it) -> !userRepository.existsById(it))) {
            return Optional.empty();
        }

        Chat chat = new Chat(users.stream().map(userRepository::getById).collect(Collectors.toList()));

        return Optional.of(chatRepository.save(chat));
    }

    /**
     * Gets all chats for a given user.
     *
     * @param username
     *            User.
     *
     * @return Users chats.
     */
    public List<Chat> getChatsByUsername(String username) {
        if (!userRepository.existsById(username)) {
            return List.of();
        }

        return userRepository.getById(username).getChats();
    }

    /**
     * Gets a single chat by id.
     *
     * @param id
     *            Chat to retrieve.
     *
     * @return Chat.
     */
    public Optional<Chat> getChatById(Long id) {
        if (!chatRepository.existsById(id)) {
            return Optional.empty();
        }

        return Optional.of(chatRepository.getById(id));
    }

    /**
     * Posts a single message.
     *
     * @param to
     *            User to send message to.
     *
     * @param message
     *            Message to post.
     * @param from
     *            User who sent the message.
     *
     * @return Chat with new message.
     */
    public Chat postMessage(String to, String from, String message) {
        Optional<Chat> optionalChat = getChatsByUsername(from).stream()
                .filter(c -> c.getUsers().stream().anyMatch(u -> u.getEmail().equals(to))).findFirst();
        Chat chat = optionalChat.orElseGet(
                () -> new Chat(List.of(userRepository.findById(to).get(), userRepository.findById(from).get())));

        ChatMessage chatMessage = new ChatMessage(chat, message, from);

        return postMessage(chatMessage, chat, from);
    }

    /**
     * Posts a single message.
     * 
     * @param chat
     *            Chat to send message in.
     * @param message
     *            Message to send.
     * @param from
     *            Message sender.
     * 
     * @return Chat with new message.
     */
    public Chat postMessage(Chat chat, String message, String from) {
        ChatMessage chatMessage = new ChatMessage(chat, message, from);

        return postMessage(chatMessage, chat, from);
    }

    /**
     * Posts a single message.
     * 
     * @param message
     *            Message to send.
     * @param chat
     *            Chat to send message in.
     * @param from
     *            Message sender.
     * 
     * @return Chat with new message.
     */
    private Chat postMessage(ChatMessage message, Chat chat, String from) {
        chat = chatRepository.save(chat);
        message = chatMessageRepository.save(message);

        User recipient = chat.getUsers().stream().filter(it -> !Objects.equals(it.getEmail(), from)).findFirst().get();

        notificationService.createNotification(recipient, chat);

        chat.getMessages().add(message);
        return chatRepository.save(chat);
    }

    /**
     * Gets the chat with the two users, or creates one if it does not yet exist.
     *
     * @param userA
     *            First user.
     * @param userB
     *            Second user.
     *
     * @return Chat.
     */
    public Chat getChatByUsers(User userA, User userB) {
        return chatRepository.findAll().stream()
                .filter(chat -> chat.getUsers().contains(userA) && chat.getUsers().contains(userB)).findFirst()
                .orElseGet(() -> chatRepository.save(new Chat(List.of(userA, userB))));
    }
}
