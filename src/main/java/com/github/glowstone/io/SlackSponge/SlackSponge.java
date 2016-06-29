package com.github.glowstone.io.SlackSponge;

import com.github.glowstone.io.SlackSponge.Commands.AdminCommand;
import com.github.glowstone.io.SlackSponge.Commands.RegisterCommand;
import com.github.glowstone.io.SlackSponge.Configs.DefaultConfig;
import com.github.glowstone.io.SlackSponge.Configs.PlayerConfig;
import com.github.glowstone.io.SlackSponge.Listeners.ChatEventListener;
import com.github.glowstone.io.SlackSponge.Listeners.SlackRequestListener;
import com.github.glowstone.io.SlackSponge.Server.SlackIncomingServer;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Plugin(id = "slacksponge", name = SlackSponge.NAME, version = SlackSponge.VERSION, description = "Send messages to and from Slack")
public class SlackSponge {

    private static SlackSponge instance;
    public static final String NAME = "SlackSponge";
    public static final String VERSION = "1.0.0";

    @Inject
    private Game game;

    @Inject
    private static Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private File configDir;

    private static DefaultConfig defaultConfig;
    private static PlayerConfig playerConfig;

    /**
     * Get the plugin instance
     *
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

    /**
     * @return PlayerConfig
     */
    public static PlayerConfig getPlayerConfig() {
        return playerConfig;
    }

    /**
     * Setup SlackSponge instance, logger and configs
     *
     * @param event GamePreInitializationEvent
     */
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
        defaultConfig = new DefaultConfig(this.configDir);
        defaultConfig.load();

        playerConfig = new PlayerConfig(this.configDir);
        playerConfig.load();
    }

    /**
     * Setup SlackSponge's event listeners
     *
     * @param event GameInitializationEvent
     */
    @Listener
    public void onGameInit(GameInitializationEvent event) {
        new SlackIncomingServer();

        // Listen for incoming slack commands
        Sponge.getEventManager().registerListeners(this, new SlackRequestListener());

        // Send outgoing message to slack
        Sponge.getEventManager().registerListeners(this, new ChatEventListener());

        // Register commands
        HashMap<List<String>, CommandSpec> subcommands = new HashMap<>();

        /**
         * /slack register <token>
         */
        subcommands.put(Collections.singletonList("register"), CommandSpec.builder()
                .permission("slack.register")
                .description(Text.of("Register you Slack user."))
                .executor(new RegisterCommand())
                .arguments(GenericArguments.string(Text.of("token")))
                .build());

        /**
         * /slack admin [-a] [-r] [-d] [player]
         */
        subcommands.put(Collections.singletonList("allow"), CommandSpec.builder()
                .permission("slack.admin")
                .description(Text.of("Administrate Slack user privileges."))
                .executor(new AdminCommand())
                .arguments(
                        GenericArguments.optional(
                                GenericArguments.flags().flag("a").flag("r").buildWith(
                                        GenericArguments.player(Text.of("player"))
                                )
                        )
                )
                .build());

        CommandSpec slackCommand = CommandSpec.builder()
                .children(subcommands)
                .build();
        game.getCommandManager().register(this, slackCommand, "slack");
    }

}
