package com.github.glowstone.io.SlackSponge.Models;

import javax.servlet.http.HttpServletRequest;

public class SlackRequest {

    public static final int REQUEST_TYPE_UNKNOWN = 0;
    public static final int REQUEST_TYPE_WEBHOOK = 1;
    public static final int REQUEST_TYPE_COMMAND = 2;

    private int requestType = REQUEST_TYPE_UNKNOWN;
    private String token;
    private String teamId;
    private String teamDomain;
    private String channelId;
    private String channelName;
    private String userId;
    private String username;
    private String text;
    private String timestamp;
    private String triggerWord;
    private String command;
    private String responseUrl;

    /**
     * SlackRequest constructor
     *
     * @param request HttpServletRequest
     */
    public SlackRequest(HttpServletRequest request) {
        this.requestType = REQUEST_TYPE_UNKNOWN;
        this.token = request.getParameter("token") != null ? request.getParameter("token") : "";
        this.teamId = request.getParameter("team_id") != null ? request.getParameter("team_id") : "";
        this.teamDomain = request.getParameter("team_domain") != null ? request.getParameter("team_domain") : "";
        this.channelId = request.getParameter("channel_id") != null ? request.getParameter("channel_id") : "";
        this.channelName = request.getParameter("channel_name") != null ? request.getParameter("channel_name") : "";
        this.userId = request.getParameter("user_id") != null ? request.getParameter("user_id") : "";
        this.username = request.getParameter("user_name") != null ? request.getParameter("user_name") : "";
        this.text = request.getParameter("text") != null ? request.getParameter("text") : "";
        this.timestamp = request.getParameter("timestamp") != null ? request.getParameter("timestamp") : "";
        this.triggerWord = request.getParameter("trigger_word") != null ? request.getParameter("trigger_word") : "";
        this.command = request.getParameter("command") != null ? request.getParameter("command") : "";
        this.responseUrl = request.getParameter("response_url") != null ? request.getParameter("response_url") : "";
    }

    /**
     * SlackRequest constructor
     *
     * @param request     HttpServletRequest
     * @param requestType int
     */
    public SlackRequest(HttpServletRequest request, int requestType) {
        this.requestType = requestType;
        this.token = request.getParameter("token") != null ? request.getParameter("token") : "";
        this.teamId = request.getParameter("team_id") != null ? request.getParameter("team_id") : "";
        this.teamDomain = request.getParameter("team_domain") != null ? request.getParameter("team_domain") : "";
        this.channelId = request.getParameter("channel_id") != null ? request.getParameter("channel_id") : "";
        this.channelName = request.getParameter("channel_name") != null ? request.getParameter("channel_name") : "";
        this.userId = request.getParameter("user_id") != null ? request.getParameter("user_id") : "";
        this.username = request.getParameter("user_name") != null ? request.getParameter("user_name") : "";
        this.text = request.getParameter("text") != null ? request.getParameter("text") : "";
        this.timestamp = request.getParameter("timestamp") != null ? request.getParameter("timestamp") : "";
        this.triggerWord = request.getParameter("trigger_word") != null ? request.getParameter("trigger_word") : "";
        this.command = request.getParameter("command") != null ? request.getParameter("command") : "";
        this.responseUrl = request.getParameter("response_url") != null ? request.getParameter("response_url") : "";
    }

    /**
     * @return int
     */
    public int getRequestType() {
        return requestType;
    }

    /**
     * @param requestType int
     */
    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    /**
     * @return String
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token String
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return String
     */
    public String getTeamId() {
        return teamId;
    }

    /**
     * @param teamId String
     */
    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    /**
     * @return String
     */
    public String getTeamDomain() {
        return teamDomain;
    }

    /**
     * @param teamDomain String
     */
    public void setTeamDomain(String teamDomain) {
        this.teamDomain = teamDomain;
    }

    /**
     * @return String
     */
    public String getChannelId() {
        return channelId;
    }

    /**
     * @param channelId String
     */
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    /**
     * @return String
     */
    public String getChannelName() {
        return channelName;
    }

    /**
     * @param channelName String
     */
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    /**
     * @return String
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId String
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return String
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username String
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return String
     */
    public String getText() {
        return text;
    }

    /**
     * @param text String
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return String
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp String
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return String
     */
    public String getTriggerWord() {
        return triggerWord;
    }

    /**
     * @param triggerWord String
     */
    public void setTriggerWord(String triggerWord) {
        this.triggerWord = triggerWord;
    }

    /**
     * @return String
     */
    public String getCommand() {
        return command;
    }

    /**
     * @param command String
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * @return String
     */
    public String getResponseUrl() {
        return responseUrl;
    }

    /**
     * @param responseUrl String
     */
    public void setResponseUrl(String responseUrl) {
        this.responseUrl = responseUrl;
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("token: ").append(this.token)
                .append(", team_id: ").append(this.teamId)
                .append(", team_domain: ").append(this.teamDomain)
                .append(", channel_id: ").append(this.channelId)
                .append(", channel_name: ").append(this.channelName)
                .append(", user_id: ").append(this.userId)
                .append(", user_name: ").append(this.username)
                .append(", text: ").append(this.text);

        if (this.requestType == REQUEST_TYPE_WEBHOOK || this.requestType == REQUEST_TYPE_UNKNOWN) {
            builder.append(", timestamp: ").append(this.timestamp)
                    .append(", trigger_word: ").append(this.triggerWord);
        }

        if (this.requestType == REQUEST_TYPE_COMMAND || this.requestType == REQUEST_TYPE_UNKNOWN) {
            builder.append(", command: ").append(this.command)
                    .append(", response_url: ").append(this.responseUrl);
        }

        return builder.toString();
    }
}
