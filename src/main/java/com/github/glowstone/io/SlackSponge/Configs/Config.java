package com.github.glowstone.io.SlackSponge.Configs;

import com.github.glowstone.io.SlackSponge.SlackSponge;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;

abstract public class Config {

    private File configDir;
    private File configFile;
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;
    private CommentedConfigurationNode config;

    /**
     * Get this config's directory
     *
     * @return File
     */
    public File getConfigDir() {
        return this.configDir;
    }

    /**
     * Get this config's file
     *
     * @return File
     */
    public File getConfigFile() {
        return this.configFile;
    }

    /**
     * Set this config's file
     *
     * @param configFile File
     */
    public void setConfigFile(File configFile) {
        this.configFile = configFile;
    }

    /**
     * Get the configuration loader
     *
     * @return ConfigurationLoader<CommentedConfigurationNode>
     */
    public ConfigurationLoader<CommentedConfigurationNode> getConfigLoader() {
        return this.configLoader;
    }

    /**
     * Set the configuration loader
     *
     * @param configLoader ConfigurationLoader<CommentedConfigurationNode>
     */
    public void setConfigLoader(ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        this.configLoader = configLoader;
    }

    /**
     * Get this config
     *
     * @return CommendedConfigurationNode
     */
    public CommentedConfigurationNode get() {
        return this.config;
    }

    /**
     * Set this config
     *
     * @param config CommentedConfigurationNode
     */
    public void setConfig(CommentedConfigurationNode config) {
        this.config = config;
    }

    /**
     * Config constructor
     *
     * @param configDir File
     */
    public Config(File configDir) {
        this.configDir = configDir;
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
     * Save this config to disk
     */
    public void save() {

        try {
            getConfigLoader().save(get());

        } catch (IOException e) {
            SlackSponge.getLogger().error("There was an error saving the config: " + e.getMessage());
        }

    }

}
