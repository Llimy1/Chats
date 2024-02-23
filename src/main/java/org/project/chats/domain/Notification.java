package org.project.chats.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String url;

    private Boolean isRead;

    private String type;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Builder
    public Notification(String content, String url, Boolean isRead, String type, User sender, User receiver) {
        this.content = content;
        this.url = url;
        this.isRead = isRead;
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
    }

    public static Notification createNotification(String content, String url, Boolean isRead, String type, User sender, User receiver) {
        return Notification.builder()
                .content(content)
                .url(url)
                .isRead(isRead)
                .type(type)
                .sender(sender)
                .receiver(receiver)
                .build();
    }
}
