package me.afarrukh.lobbybot.lobby;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.util.Optional;

class LobbyMessageTable {
    private final BiMap<Member, Message> messageTable;

    LobbyMessageTable() {
        messageTable = HashBiMap.create();
    }

    public boolean hasPendingMessage(Member member) {
        return messageTable.containsKey(member);
    }

    public Optional<Message> findMessageById(String messageId) {
        for (Message message : messageTable.values())
            if (message.getId().equals(messageId))
                return Optional.of(message);
        return Optional.empty();
    }

    public void addPendingMessage(Member member, Message message) {
        messageTable.put(member, message);
    }

    public Optional<Message> getMessageForMember(Member member) {
        Message message = messageTable.get(member);
        if (message != null)
            return Optional.of(message);
        return Optional.empty();
    }

    public Optional<Member> getMemberForMessage(Message message) {
        Member member = messageTable.inverse().get(message);
        if (member != null)
            return Optional.of(member);
        return Optional.empty();
    }

    public void removeMessageForMember(Member member) {
        Message message = messageTable.get(member);
        if (message != null) {
            messageTable.remove(member);
            message.delete().queue();
        }
    }
}
