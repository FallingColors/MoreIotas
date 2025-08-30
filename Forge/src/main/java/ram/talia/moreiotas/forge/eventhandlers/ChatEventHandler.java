package ram.talia.moreiotas.forge.eventhandlers;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatEventHandler {
    private static final String TAG_CHAT_PREFIX = "moreiotas:prefix";

    private static final Map<UUID, @Nullable String> lastMessages = new HashMap<>();

    private static @Nullable String lastMessage = null;

    public static void setPrefix(Player player, @Nullable String prefix) {
        if (prefix == null)
            player.getPersistentData().remove(TAG_CHAT_PREFIX);
        else
            player.getPersistentData().putString(TAG_CHAT_PREFIX, prefix);
    }

    public static @Nullable String getPrefix(Player player) {
        if (!player.getPersistentData().contains(TAG_CHAT_PREFIX))
            return null;
        return player.getPersistentData().getString(TAG_CHAT_PREFIX);
    }

    public static @Nullable String getLastMessage(@Nullable Player player) {
        if (player == null)
            return lastMessage;
        return lastMessages.get(player.getUUID());
    }

    @SubscribeEvent
    public static void chatMessageSent(ServerChatEvent event) {
        var player = event.getPlayer();
        var uuid = player.getUUID();
        var text = event.getRawText();

        if (event.isCanceled())
            return;

        if (!player.getPersistentData().contains(TAG_CHAT_PREFIX)) {
            lastMessages.put(uuid, text);
            lastMessage = text;
            return;
        }

        var prefix = player.getPersistentData().getString(TAG_CHAT_PREFIX);

        if (text.startsWith(prefix)) {
            event.setCanceled(true);
            lastMessages.put(uuid, text.substring(prefix.length()));
            return;
        }

        lastMessage = text;
    }
}
