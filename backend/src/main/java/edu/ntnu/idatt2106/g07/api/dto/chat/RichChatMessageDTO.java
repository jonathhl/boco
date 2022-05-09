package edu.ntnu.idatt2106.g07.api.dto.chat;

import edu.ntnu.idatt2106.g07.api.dto.RequestDTO;
import edu.ntnu.idatt2106.g07.api.dto.rating.RatingDTO;
import edu.ntnu.idatt2106.g07.api.entity.Rating;
import edu.ntnu.idatt2106.g07.api.entity.Request;
import edu.ntnu.idatt2106.g07.api.misc.Attachable;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RichChatMessageDTO extends ChatMessageDTO {

    private LocalDateTime time;
    private String from;
    private Attachable attachment;

    public RichChatMessageDTO(Attachable attachment) {
        this.time = attachment.getTime();
        this.from = attachment.getFrom();

        if (attachment instanceof Request) {
            type = ChatType.REQUEST;
            this.attachment = new RequestDTO((Request) attachment);
        } else if (attachment instanceof Rating) {
            type = ChatType.RATING;
            this.attachment = new RatingDTO((Rating) attachment);
        }
    }
}
