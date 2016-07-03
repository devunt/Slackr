package com.github.glowstone.io.slackr.Runnables;

import com.github.glowstone.io.slackr.Server.SlackSender;
import net.gpedro.integrations.slack.SlackMessage;

public class SlackSendRunnable implements Runnable {

    private SlackSender sender;
    private SlackMessage message;

    /**
     * SlackSendRunnable constructor
     *
     * @param message SlackMessage
     */
    public SlackSendRunnable(SlackSender sender, SlackMessage message) {
        this.sender = sender;
        this.message = message;
    }

    /**
     * Send the SlackMessage
     */
    @Override
    public void run() {
        sender.send(this.message.prepare());
    }

}
