package com.github.glowstone.io.SlackSponge.Models;

import com.github.glowstone.io.SlackSponge.SlackSponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.channel.MutableMessageChannel;
import org.spongepowered.api.text.chat.ChatType;

import javax.annotation.Nullable;
import java.util.*;

public class SlackMessageChannel implements MutableMessageChannel {

    private Collection<MessageReceiver> members;
    private StringBuilder commandResult;

    public SlackMessageChannel() {
        this.members = new HashSet<>();
        this.commandResult = new StringBuilder();
    }

    @Override
    public Collection<MessageReceiver> getMembers() {
        return Collections.unmodifiableCollection(this.members);
    }

    @Override
    public boolean addMember(MessageReceiver member) {
        return this.members.add(member);
    }

    @Override
    public boolean removeMember(MessageReceiver member) {
        return this.members.remove(member);
    }

    @Override
    public void clearMembers() {
        this.members.clear();
    }

    @Override
    public Optional<Text> transformMessage(@Nullable Object sender, MessageReceiver recipient, Text original, ChatType type) {
        this.commandResult.append(original.toPlain());
        return Optional.empty();
    }

    public String getCommandResult() {
        return commandResult.toString();
    }
}
