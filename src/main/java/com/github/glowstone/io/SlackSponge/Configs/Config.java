package com.github.glowstone.io.SlackSponge.Configs;

import com.github.glowstone.io.SlackSponge.SlackSponge;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;

abstract public class Config {

    private SlackSponge plugin;
    private File configDir;
    private File configFile;
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;
    private CommentedConfigurationNode config;

    /**
     * @return SlackSponge
     */
    public SlackSponge getPlugin() {
        return this.plugin;
    }

    /**
     * @return File
     */
    public File getConfigDir() {
        return this.configDir;
    }

    /**
     * @return File
     */
    public File getConfigFile() {
        return this.configFile;
    }

    /**
     * @param configFile File
     */
    public void setConfigFile(File configFile) {
        this.configFile = configFile;
    }

    /**
     * @return ConfigurationLoader<CommentedConfigurationNode>
     */
    public ConfigurationLoader<CommentedConfigurationNode> getConfigLoader() {
        return this.configLoader;
    }

    /**
     * @param configLoader ConfigurationLoader<CommentedConfigurationNode>
     */
    public void setConfigLoader(ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        this.configLoader = configLoader;
    }

    /**
     * @return CommendedConfigurationNode
     */
    public CommentedConfigurationNode get() {
        return this.config;
    }

    /**
     * @param config CommentedConfigurationNode
     */
    public void setConfig(CommentedConfigurationNode config) {
        this.config = config;
    }

    /**
     * @param plugin SlackSponge
     * @param configDir File
     */
    public Config(SlackSponge plugin, File configDir) {
        this.plugin = plugin;
        this.configDir = configDir;
    }

    /**
     * @param plugin SlackSponge
     * @param configDir File
     * @param configFile File
     */
    public Config(SlackSponge plugin, File configDir, File configFile) {
        this.plugin = plugin;
        this.configDir = configDir;
        this.configFile = configFile;
    }

    /**
     * Load Config
     */
    public void load() {

        setConfigLoader(HoconConfigurationLoader.builder().setFile(getConfigFile()).build());

        try {

            boolean isNew = false;
            if (!getConfigFile().isFile()) {
                if (getConfigFile().createNewFile()) {
                    isNew = true;
                }
            }
            setConfig(getConfigLoader().load());

            if (isNew) {
                setDefaults();
                save();
            }

        } catch (IOException e) {
            SlackSponge.getLogger().error("There was an error loading the config: " + e.getMessage());
        }

    }

    /**
     * Set default values the first time the config file is generated
     */
    abstract protected void setDefaults();

    /**
     * Save Config
     */
    public void save() {

        try {
            getConfigLoader().save(get());

        } catch (IOException e) {
            SlackSponge.getLogger().error("There was an error saving the config: " + e.getMessage());
        }

    }

}
