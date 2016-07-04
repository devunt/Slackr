package com.github.glowstone.io.slackr.Configs;

import com.github.glowstone.io.slackr.Slackr;

import java.io.File;

public class DefaultConfig extends Config {

    public static final String COMMAND_SETTINGS = "Command Settings";
    public static final String GENERAL_SETTINGS = "General Settings";
    public static final String WEBHOOK_SETTINGS = "Webhook Settings";

    /**
     * DefaultConfig constructor
     *
     * @param configDir File
     */
    public DefaultConfig(File configDir) {
        super(configDir);
        setConfigFile(new File(configDir, Slackr.NAME + ".conf"));
    }

    /**
     * Set this config's default values
     */
    protected void setDefaults() {
        get().getNode(COMMAND_SETTINGS, "token").setValue("");
        get().getNode(COMMAND_SETTINGS, "command").setValue("mc");

        get().getNode(GENERAL_SETTINGS, "allCallMod").setValue(true);
        get().getNode(GENERAL_SETTINGS, "callModChannel").setValue("#admins");
        get().getNode(GENERAL_SETTINGS, "notificationSound").setValue(true);
        get().getNode(GENERAL_SETTINGS, "port").setValue(8765);
        get().getNode(GENERAL_SETTINGS, "showHelmet").setValue(true);
        get().getNode(GENERAL_SETTINGS, "slackTeamDomain").setValue("");

        get().getNode(WEBHOOK_SETTINGS, "playerDeath").setValue(true);
        get().getNode(WEBHOOK_SETTINGS, "playerJoin").setValue(true);
        get().getNode(WEBHOOK_SETTINGS, "playerLeave").setValue(true);
        get().getNode(WEBHOOK_SETTINGS, "token").setValue("");
        get().getNode(WEBHOOK_SETTINGS, "webhookUrl").setValue("");
    }

    /**
     * Format the slackTeamDomain
     */
    public void formatSlackTeamDomain() {
        String domain = get().getNode(GENERAL_SETTINGS, "slackTeamDomain").getString("");
        if (domain.isEmpty()) {
            return;
        }
        if (domain.contains(".slack.com")) {
            return;
        }
        get().getNode(GENERAL_SETTINGS, "slackTeamDomain").setValue(domain + ".slack.com");
        save();
    }

    /**
     * Are outgoing messages webhook settings setup?
     *
     * @return boolean
     */
    public boolean isOutgoingWebhook() {
        return !get().getNode(WEBHOOK_SETTINGS, "webhookUrl").getString("").isEmpty();
    }

    /**
     * Are Slack death messages turned on?
     *
     * @return boolean
     */
    public boolean allowDeathMessages() {
        return get().getNode(GENERAL_SETTINGS, "playerDeath").getBoolean(false);
    }

    /**
     * Are Slack join messages turned on?
     *
     * @return boolean
     */
    public boolean allowJoinMessages() {
        return get().getNode(GENERAL_SETTINGS, "playerJoin").getBoolean(false);
    }

    /**
     * Are Slack leave messages turned on?
     *
     * @return boolean
     */
    public boolean allowLeaveMessages() {
        return get().getNode(GENERAL_SETTINGS, "playerLeave").getBoolean(false);
    }

}
