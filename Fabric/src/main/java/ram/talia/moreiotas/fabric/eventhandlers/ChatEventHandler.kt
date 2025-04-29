package ram.talia.moreiotas.fabric.eventhandlers

import net.minecraft.network.chat.ChatType
import net.minecraft.network.chat.PlayerChatMessage
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import ram.talia.moreiotas.api.util.ChatEntry
import ram.talia.moreiotas.fabric.cc.MoreIotasCardinalComponents
import java.util.UUID
import kotlin.collections.ArrayDeque

object ChatEventHandler {
    private val lastMessages: MutableMap<UUID, String?> = mutableMapOf()
    private val messageLog: ArrayDeque<ChatEntry> = ArrayDeque<ChatEntry>()
    private var lastMessageTimestamp: Long? = null;
    private var messagesHandled: Int = 0;

    private val lastMessageTimestamps: MutableMap<UUID, Long?> = mutableMapOf()

    fun receiveChat(message: PlayerChatMessage, player: ServerPlayer, params: ChatType.Bound): Boolean {
        val text = message.signedBody.content + (message.unsignedContent?.string ?: "")

        val prefix = MoreIotasCardinalComponents.CHAT_PREFIX_HOLDER.get(player).prefix

        var timestamp = player.serverLevel().gameTime

        if (prefix != null && text.startsWith(prefix)) {
            lastMessages[player.uuid] = text.substring(prefix.length)
            lastMessageTimestamps[player.uuid] = timestamp
            return false
        }

        if (prefix == null) {
            lastMessages[player.uuid] = text
            lastMessageTimestamps[player.uuid] = timestamp
        }

        while (messageLog.size > 1023) {
            // Just in case somehow we end up putting two "at once" even though this is only place this is touched
            messageLog.removeFirst()
        }

        messageLog.addLast(ChatEntry(text, timestamp, player.getName().getString()))
        if (lastMessageTimestamp == timestamp) {
            messagesHandled++
        } else {
            messagesHandled = 1
            lastMessageTimestamp = timestamp
        }
        return true
    }

    @JvmStatic
    fun lastMessage(player: Player?): String? = if (player != null) lastMessages[player.uuid] else messageLog.last().message

    @JvmStatic
    fun lastMessageTimestamp(player: Player?): Long? = if (player != null) lastMessageTimestamps[player.uuid] else lastMessageTimestamp

    @JvmStatic
    fun chatLog(count: Int): List<ChatEntry> {
        return messageLog.subList(messageLog.size - count, messageLog.size)
    }
}