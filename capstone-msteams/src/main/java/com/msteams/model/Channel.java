package com.msteams.model;

/**
 * Matches the "channels" table. Messages are fetched separately by MessageDAO.
 */
public class Channel {
    private final int channelId;
    private final String name;

    public Channel(int channelId, String name) {
        this.channelId = channelId;
        this.name = name;
    }

    public int getChannelId() {
        return channelId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
