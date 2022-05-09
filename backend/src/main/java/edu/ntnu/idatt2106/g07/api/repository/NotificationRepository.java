package edu.ntnu.idatt2106.g07.api.repository;

import edu.ntnu.idatt2106.g07.api.entity.Chat;
import edu.ntnu.idatt2106.g07.api.entity.Notification;
import edu.ntnu.idatt2106.g07.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    public List<Notification> findAllByUser_Email(String email);

    public List<Notification> findAllByChatAndUser(Chat chat, User user);

}
