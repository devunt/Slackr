package com.github.glowstone.io.SlackSponge.Events;

import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;

public class SlackCommandEvent implements Event, Cancellable {

    private final String username;
    private final String command;
    private final String text;

    private final Cause cause;
    private boolean cancelled = false;

    /**
     * SlackCommandEvent constructor
     *
     * @param username String
     * @param command  String
     * @param text     String
     */
    public SlackCommandEvent(String username, String command, String text) {
        this.username = username;
        this.command = command;
        this.text = text;

        this.cause = null;
    }

    /**
     * Get the Slack username of the command sender
     *
     * @return String
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Get the command
     *
     * @return String
     */
    public String getCommand() {
        return this.command;
    }

    /**
     * Get the text of the command
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
