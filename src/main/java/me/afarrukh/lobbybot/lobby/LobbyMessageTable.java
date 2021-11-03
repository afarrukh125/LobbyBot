package me.afarrukh.lobbybot.lobby;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.util.Optional;

/**
 * For a given lobby, this class aims to map the "acceptance" messages for a given member. For instance,
 * if member A has a lobby, then that lobby has a lobby table that maps, for any potential members that join
 * the voice chat, a message that the owner of the lobby needs to accept or reject.
 * <p>
 * Once the request (invoked by simply joining the voice chat) has been accepted or rejected, the message
 * can be safely deleted and cleared from this table.
 * <p>
 * Note we use Guava's BiMap class so this table is inherently a 1-1 invertible mapping.
 */
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
        return Optional.ofNullable(messageTable.inverse().get(message));
    }

    public void removeMessageForMember(Member member) {
        Message message = messageTable.get(member);
        if (message != null) {
            messageTable.remove(member);
            message.delete().queue();
        }
    }
}
