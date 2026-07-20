package com.msteams.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Matches the "messages" table. authorId identifies who posted it (used to
 * decide whether the current user is allowed to delete it); authorName is
 * denormalized (joined in from users) purely for display.
 */
public class Message {
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("MMM d, h:mm a");

    private int messageId;
    private final int channelId;
    private final int authorId;
    private final String authorName;
    private final String content;
    private final LocalDateTime timestamp;

    public Message(int messageId, int channelId, int authorId, String authorName, String content, LocalDateTime timestamp) {
        this.messageId = messageId;
        this.channelId = channelId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.content = content;
        this.timestamp = timestamp;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getChannelId() {
        return channelId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "[" + timestamp.format(FORMAT) + "] " + authorName + ": " + content;
    }
}
