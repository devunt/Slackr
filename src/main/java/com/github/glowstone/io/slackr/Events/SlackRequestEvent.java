package com.github.glowstone.io.slackr.Events;

import com.github.glowstone.io.slackr.Models.SlackRequest;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;

public class SlackRequestEvent implements Event, Cancellable {

    private final SlackRequest slackRequest;

    private final Cause cause;
    private boolean cancelled = false;

    /**
     * SlackRequestEvent constructor
     *
     * @param slackRequest SlackRequest
     */
    public SlackRequestEvent(SlackRequest slackRequest) {
        this.slackRequest = slackRequest;
        this.cause = null;
    }

    /**
     * Get the SlackRequest from this command
     *
     * @return String
     */
    public SlackRequest getSlackRequest() {
        return this.slackRequest;
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
