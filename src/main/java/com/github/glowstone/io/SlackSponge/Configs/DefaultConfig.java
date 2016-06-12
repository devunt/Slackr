package com.github.glowstone.io.SlackSponge.Configs;

import com.github.glowstone.io.SlackSponge.SlackSponge;

import java.io.File;

public class DefaultConfig extends Config {

    public static final String SLACK_SETTINGS = "slackSettings";
    public static final String SLACK_PLAYERS = "slackPlayers";

    /**
     * @param plugin SlackSponge
     * @param configDir File
     */
    public DefaultConfig(SlackSponge plugin, File configDir) {
        super(plugin, configDir);
        setConfigFile(new File(configDir, SlackSponge.NAME + ".conf"));
    }

    /**
     * Set this config's default values
     */
    protected void setDefaults() {
        get().getNode(SLACK_SETTINGS, "port").setValue(8765);
        get().getNode(SLACK_SETTINGS, "showHelmet").setValue(true);
        get().getNode(SLACK_SETTINGS, "token").setValue("");
        get().getNode(SLACK_SETTINGS, "webHookUrl").setValue("");
    }
}
