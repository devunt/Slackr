package com.github.glowstone.io.SlackSponge.Models;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.util.Tristate;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SlackCommandSource implements CommandSource {

    private User user;
    private MessageChannel messageChannel;

    /**
     * SlackCommandSource constructor
     *
     * @param user User
     */
    public SlackCommandSource(User user) {
        this.user = user;
    }

    @Override
    public String getName() {
        return this.user.getName();
    }

    @Override
    public Optional<CommandSource> getCommandSource() {
        return this.user.getCommandSource();
    }

    @Override
    public SubjectCollection getContainingCollection() {
        return this.user.getContainingCollection();
    }

    @Override
    public SubjectData getSubjectData() {
        return this.user.getSubjectData();
    }

    @Override
    public SubjectData getTransientSubjectData() {
        return this.user.getTransientSubjectData();
    }

    @Override
    public boolean hasPermission(Set<Context> contexts, String permission) {
        return this.user.hasPermission(contexts, permission);
    }

    @Override
    public Tristate getPermissionValue(Set<Context> contexts, String permission) {
        return this.user.getPermissionValue(contexts, permission);
    }

    @Override
    public boolean isChildOf(Set<Context> contexts, Subject parent) {
        return this.user.isChildOf(contexts, parent);
    }

    @Override
    public List<Subject> getParents(Set<Context> contexts) {
        return this.user.getParents(contexts);
    }

    @Override
    public String getIdentifier() {
        return this.user.getIdentifier();
    }

    @Override
    public Set<Context> getActiveContexts() {
        return this.user.getActiveContexts();
    }

    @Override
    public void sendMessage(Text message) {
        this.messageChannel.send(message);
    }

    @Override
    public MessageChannel getMessageChannel() {
        return this.messageChannel;
    }

    @Override
    public void setMessageChannel(MessageChannel channel) {
        this.messageChannel = channel;
    }
}
