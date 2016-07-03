package com.github.glowstone.io.slackr.Server;

import com.github.glowstone.io.slackr.Configs.DefaultConfig;
import com.github.glowstone.io.slackr.Runnables.SlackSendRunnable;
import com.github.glowstone.io.slackr.Slackr;
import com.google.gson.JsonObject;
import net.gpedro.integrations.slack.SlackMessage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SlackSender {

    private static SlackSender instance;
    private String url;
    private int timeout = 5000;

    /**
     * @param url String
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @param timeout int
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Gets the SlackSender for Slack's incoming webhooks
     *
     * @return SlackSender
     */
    public static SlackSender getWebhookInstance() {
        String url = Slackr.getDefaultConfig().get().getNode(DefaultConfig.WEBHOOK_SETTINGS, "webhookUrl").getString("");
        return getInstance(url);
    }

    /**
     * Gets the SlackSender instance for a url, generally a Slack command's response url.
     *
     * @param url String
     * @return SlackSender
     */
    public static SlackSender getInstance(String url) {
        if (instance != null) {
            return instance;
        }
        SlackSender instance = new SlackSender();
        instance.setUrl(url);
        return instance;
    }

    /**
     * Send a player message to slackr
     *
     * @param message  String
     * @param username String
     */
    public void sendMessage(String message, String username) {

        if (!message.isEmpty() && !username.isEmpty()) {

            boolean showHelmet = Slackr.getDefaultConfig().get().getNode(DefaultConfig.GENERAL_SETTINGS, "showHelmet").getBoolean(true);

            SlackMessage slackMessage = new SlackMessage();
            slackMessage.setText(message);
            slackMessage.setUsername(username);
            slackMessage.setIcon("https://minotar.net/" + (showHelmet ? "helm" : "avatar") + "/" + username + ".png");

            if (Slackr.getDefaultConfig().isOutgoingWebhook()) {
                Thread thread = new Thread(new SlackSendRunnable(this, slackMessage));
                thread.start();
            }
        }

    }

    /**
     * Send a message to slackr
     *
     * @param message String
     */
    public void sendMessage(String message) {

        if (!message.isEmpty()) {
            SlackMessage slackMessage = new SlackMessage();
            slackMessage.setText(message);

            if (Slackr.getDefaultConfig().isOutgoingWebhook()) {
                Thread thread = new Thread(new SlackSendRunnable(this, slackMessage));
                thread.start();
            }
        }

    }

    /**
     * Send a message to a Slack channel
     *
     * @param channel  String
     * @param username String
     * @param message  message
     */
    public void sendChannelMessage(String channel, String username, String message) {

        if (!channel.isEmpty() && !username.isEmpty() && !message.isEmpty()) {

            boolean showHelmet = Slackr.getDefaultConfig().get().getNode(DefaultConfig.GENERAL_SETTINGS, "showHelmet").getBoolean(true);

            SlackMessage slackMessage = new SlackMessage();
            slackMessage.setChannel(channel);
            slackMessage.setText(message);
            slackMessage.setUsername(username);
            slackMessage.setIcon("https://minotar.net/" + (showHelmet ? "helm" : "avatar") + "/" + username + ".png");

            if (Slackr.getDefaultConfig().isOutgoingWebhook()) {
                Thread thread = new Thread(new SlackSendRunnable(this, slackMessage));
                thread.start();
            }
        }

    }

    /**
     * Send a command response to slackr
     *
     * @param message String
     */
    public void sendCommandResponse(String message) {

        if (!message.isEmpty()) {

            SlackMessage slackMessage = new SlackMessage();
            slackMessage.setText(message);

            Thread thread = new Thread(new SlackSendRunnable(this, slackMessage));
            thread.start();
        }

    }

    /**
     * Send json message to this url
     *
     * @param message JsonObject
     * @return String
     */
    public String send(JsonObject message) {

        HttpURLConnection connection = null;

        try {
            // Create connection
            final URL url = new URL(this.url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(timeout);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            final String payload = "payload=" + URLEncoder.encode(message.toString(), "UTF-8");

            // Send request
            final DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(payload);
            wr.flush();
            wr.close();

            // Get Response
            final InputStream is = connection.getInputStream();
            final BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\n');
            }

            rd.close();
            return response.toString();

        } catch (Exception e) {

            throw new RuntimeException(e);

        } finally {

            if (connection != null) {
                connection.disconnect();
            }

        }
    }

}
