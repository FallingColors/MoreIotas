package ram.talia.moreiotas.fabric.eventhandlers

import net.minecraft.network.chat.ChatType
import net.minecraft.network.chat.PlayerChatMessage
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import ram.talia.moreiotas.fabric.cc.MoreIotasCardinalComponents
import java.util.*

object ChatEventHandler {
    private val lastMessages: MutableMap<UUID, String?> = mutableMapOf()
    private var lastMessage: String? = null

    private val lastMessageTimestamps: MutableMap<UUID, Long?> = mutableMapOf()
    private var lastMessageTimestamp: Long? = null

    fun receiveChat(message: PlayerChatMessage, player: ServerPlayer, params: ChatType.Bound): Boolean {
        val text = message.signedBody.content + (message.unsignedContent?.string ?: "")

        val prefix = MoreIotasCardinalComponents.CHAT_PREFIX_HOLDER.get(player).prefix

        if (prefix == null) {
            lastMessages[player.uuid] = text
            lastMessage = text
            lastMessageTimestamp = player.serverLevel().gameTime
            lastMessageTimestamps[player.uuid] = lastMessageTimestamp
            return true
        }

        if (text.startsWith(prefix)) {
            lastMessages[player.uuid] = text.substring(prefix.length)
            lastMessageTimestamps[player.uuid] = player.serverLevel().gameTime
            return false
        }

        lastMessage = text
        lastMessageTimestamp = player.serverLevel().gameTime
        return true
    }

    @JvmStatic
    fun lastMessage(player: Player?): String? = if (player != null) lastMessages[player.uuid] else lastMessage

    @JvmStatic
    fun lastMessageTimestamp(player: Player?): Long? = if (player != null) lastMessageTimestamps[player.uuid] else lastMessageTimestamp

    @JvmStatic
    fun resetMessage(player: Player): Unit {
        lastMessages[player.uuid] = null
        lastMessageTimestamps[player.uuid] = null
    }
}