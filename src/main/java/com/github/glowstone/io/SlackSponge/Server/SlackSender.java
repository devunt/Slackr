package com.github.glowstone.io.SlackSponge.Server;

import com.github.glowstone.io.SlackSponge.Configs.DefaultConfig;
import com.github.glowstone.io.SlackSponge.SlackSponge;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;

public class SlackSender {

    private static SlackSender instance;
    private SlackApi api;

    public SlackSender() {
        String url = SlackSponge.getDefaultConfig().get().getNode(DefaultConfig.SLACK_SETTINGS, "webHookUrl")
                .getString("");
        this.api = new SlackApi(url);
    }

    /**
     * @return SlackApi
     */
    public SlackApi getSlackApi() {
        return this.api;
    }

    /**
     * @return SlackSender
     */
    public static SlackSender getInstance() {
        if (instance == null) {
            instance = new SlackSender();
        }
        return instance;
    }

    /**
     * Send a message to slack
     *
     * @param message  String
     * @param username String
     */
    public void send(String message, String username) {

        if (!message.isEmpty() && !username.isEmpty()) {

            boolean showHelmet = SlackSponge.getDefaultConfig().get().getNode(DefaultConfig.SLACK_SETTINGS,
                    "showHelmet").getBoolean(true);

            SlackMessage slackMessage = new SlackMessage();
            slackMessage.setText(message);
            slackMessage.setUsername(username);
            slackMessage.setIcon("https://minotar.net/" + (showHelmet ? "helm" : "avatar") + "/" + username + ".png");

            Thread thread = new Thread(new SlackSendService(slackMessage));
            thread.start();
        }

    }
}
