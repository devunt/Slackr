package com.github.glowstone.io.SlackSponge.Server;

import net.gpedro.integrations.slack.SlackMessage;

public class SlackSendService implements Runnable {

    private SlackSender sender;
    private SlackMessage message;

    /**
     * SlackSendService constructor
     *
     * @param message SlackMessage
     */
    public SlackSendService(SlackSender sender, SlackMessage message) {
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
