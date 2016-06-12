package com.github.glowstone.io.SlackSponge.Events;

import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;

public class SlackMessageEvent implements Event, Cancellable {

    private final String channel;
    private final String username;
    private final String text;

    private final Cause cause;
    private boolean cancelled = false;

    /**
     * @param channel String
     * @param username String
     * @param text String
     */
    public SlackMessageEvent(String channel, String username, String text) {
        this.channel = channel;
        this.username = username;
        this.text = text;

        this.cause = null;
    }

    /**
     * @return String
     */
    public String getMessage() {
        return String.format("<%s> %s", this.username, this.text);
    }

    /**
     * @return String
     */
    public String getChannel() {
        return this.channel;
    }

    /**
     * @return String
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * @return String
     */
    public String getText() {
        return this.text;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }
}
