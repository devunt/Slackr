package com.github.glowstone.io.SlackSponge.Server;

import net.gpedro.integrations.slack.SlackMessage;

public class SlackSendService implements Runnable {

    private SlackMessage message;

    /**
     * SlackSendService constructor
     *
     * @param message SlackMessage
     */
    public SlackSendService(SlackMessage message) {
        this.message = message;
    }

    /**
     * Send the SlackMessage
     */
    @Override
    public void run() {
        SlackSender.getInstance().getSlackApi().call(this.message);
    }

}
