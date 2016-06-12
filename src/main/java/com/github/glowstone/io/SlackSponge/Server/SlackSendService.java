package com.github.glowstone.io.SlackSponge.Server;

import net.gpedro.integrations.slack.SlackMessage;

public class SlackSendService implements Runnable {

    private SlackMessage message;

    public SlackSendService(SlackMessage message) {
        this.message = message;
    }

    @Override
    public void run() {
        SlackSender.getInstance().getSlackApi().call(this.message);
    }

}
