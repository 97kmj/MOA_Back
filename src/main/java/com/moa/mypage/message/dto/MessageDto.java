package com.moa.mypage.message.dto;

import java.sql.Timestamp;

import com.moa.entity.Message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {
    private Long messageId;
    private String senderId;
    private String senderName;
    private String title;
    private String content;
    private Timestamp createAt;
    private Boolean readStatus;
    private String reply;
    private Boolean replyReadState;
    private Timestamp replyAt;
    
    
    public static MessageDto fromEntity(Message message) {
    	return MessageDto.builder()
    			.messageId(message.getMessageId())
    			.senderId(message.getSender().getUsername())
    			.senderName(message.getSender().getName())
    			.title(message.getTitle())
    			.content(message.getContent())
    			.createAt(message.getCreateAt())
    			.readStatus(message.getReadStatus())
    			.reply(message.getReply())
    			.replyReadState(message.getReplyReadState())
    			.replyAt(message.getReplyAt())
    			.build();
    }
}
