package com.github.glowstone.io.SlackSponge;

import com.github.glowstone.io.SlackSponge.Configs.DefaultConfig;
import com.github.glowstone.io.SlackSponge.Listeners.ChatEventListener;
import com.github.glowstone.io.SlackSponge.Listeners.SlackMessageListener;
import com.github.glowstone.io.SlackSponge.Server.SlackIncomingServer;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;

@Plugin(id = "slacksponge", name = SlackSponge.NAME, version = SlackSponge.VERSION,
        description = "Send messages to and from Slack")
public class SlackSponge {

    private static SlackSponge instance;
    public static final String NAME = "SlackSponge";
    public static final String VERSION = "1.0";

    @Inject
    private Game game;

    @Inject
    private static Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private File configDir;

    private static DefaultConfig defaultConfig;

    /**
     * @return SlackSponge
     */
    public static SlackSponge getInstance() {
        return instance;
    }

    /**
     * @return Game
     */
    public Game getGame() {
        return this.game;
    }

    /**
     * @return Logger
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * @return DefaultConfig
     */
    public static DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {

        SlackSponge.instance = this;
        SlackSponge.logger = LoggerFactory.getLogger(SlackSponge.NAME);
        getLogger().info(String.format("Starting up %s v%s", SlackSponge.NAME, SlackSponge.VERSION));

        if (!this.configDir.isDirectory()) {
            if (this.configDir.mkdir()) {
                getLogger().info("SlackSponge config directory successfully created!");
            }
        }

        // Load default config
        defaultConfig = new DefaultConfig(this, this.configDir);
        defaultConfig.load();
    }

    @Listener
    public void onGameInit(GameInitializationEvent event) {
        new SlackIncomingServer();

        // Listen for incoming slack messages
        Sponge.getEventManager().registerListeners(this, new SlackMessageListener());

        // Send outgoing message to slack
        Sponge.getEventManager().registerListeners(this, new ChatEventListener());
    }

}
