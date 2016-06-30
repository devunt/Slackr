package com.github.glowstone.io.SlackSponge.Configs;

import com.github.glowstone.io.SlackSponge.SlackSponge;

import java.io.File;
import java.util.ArrayList;

public class DefaultConfig extends Config {

    public static final String GENERAL_SETTINGS = "General Settings";
    public static final String WEBHOOK_SETTINGS = "Webhook Settings";
    public static final String COMMAND_SETTINGS = "Command Settings";

    /**
     * DefaultConfig constructor
     *
     * @param configDir File
     */
    public DefaultConfig(File configDir) {
        super(configDir);
        setConfigFile(new File(configDir, SlackSponge.NAME + ".conf"));
    }

    /**
     * Set this config's default values
     */
    protected void setDefaults() {
        get().getNode(COMMAND_SETTINGS, "token").setValue("");
        get().getNode(COMMAND_SETTINGS, "command").setValue("mc");

        get().getNode(GENERAL_SETTINGS, "port").setValue(8765);
        get().getNode(GENERAL_SETTINGS, "showHelmet").setValue(true);

        get().getNode(WEBHOOK_SETTINGS, "token").setValue("");
        get().getNode(WEBHOOK_SETTINGS, "webhookUrl").setValue("");
    }
}
