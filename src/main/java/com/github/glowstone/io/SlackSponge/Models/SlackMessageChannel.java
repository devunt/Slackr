package com.github.glowstone.io.SlackSponge.Models;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.chat.ChatType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class SlackMessageChannel implements MessageChannel {

    private Collection<MessageReceiver> members;
    private StringBuilder commandResult;

    public SlackMessageChannel() {
        this.members = new ArrayList<>();
        this.commandResult = new StringBuilder();
    }

    public void addMember(MessageReceiver member) {
        this.members.add(member);
    }

    @Override
    public Optional<Text> transformMessage(@Nullable Object sender, MessageReceiver recipient, Text original, ChatType type) {
        this.commandResult.append(original.toPlain());
        return Optional.empty();
    }

    @Override
    public Collection<MessageReceiver> getMembers() {
        return this.members;
    }

    public String getCommandResult() {
        return commandResult.toString();
    }
}
