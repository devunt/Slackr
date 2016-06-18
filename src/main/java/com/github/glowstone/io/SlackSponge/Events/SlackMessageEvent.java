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
     * SlackMessageEvent constructor
     *
     * @param channel  String
     * @param username String
     * @param text     String
     */
    public SlackMessageEvent(String channel, String username, String text) {
        this.channel = channel;
        this.username = username;
        this.text = text;

        this.cause = null;
    }

    /**
     * Get the formatted message text
     * <username> message text
     *
     * @return String
     */
    public String getMessage() {
        return String.format("<%s> %s", this.username, this.text);
    }

    /**
     * Get the Slack channel of this message
     *
     * @return String
     */
    public String getChannel() {
        return this.channel;
    }

    /**
     * Get the Slack username of this message
     *
     * @return String
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Get the text of this message
     *
     * @return String
     */
    public String getText() {
        return this.text;
    }

    /**
     * Is this event cancelled?
     *
     * @return boolean
     */
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Should this event be cancelled?
     *
     * @param cancel boolean
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * Get the cause of this event
     *
     * @return Cause
     */
    @Override
    public Cause getCause() {
        return this.cause;
    }
}
