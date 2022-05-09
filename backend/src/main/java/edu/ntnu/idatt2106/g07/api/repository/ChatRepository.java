package edu.ntnu.idatt2106.g07.api.repository;

import edu.ntnu.idatt2106.g07.api.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {

}
