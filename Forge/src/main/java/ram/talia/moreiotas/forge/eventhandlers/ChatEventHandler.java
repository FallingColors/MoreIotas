package ram.talia.moreiotas.forge.eventhandlers;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;
import ram.talia.moreiotas.api.mod.MoreIotasConfig;
import ram.talia.moreiotas.api.util.ChatEntry;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChatEventHandler {
    private static final String TAG_CHAT_PREFIX = "moreiotas:prefix";

    private static final Map<UUID, @Nullable String> lastMessages = new HashMap<>();
    private static final Map<UUID, @Nullable String> lastMessageTimestamps = new HashMap<>();

    private static final ArrayDeque<ChatEntry> messageLog = new ArrayDeque<>();

    private static Long lastMessageTimestamp = 0;
    private static Int messagesHandled = 0;

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


	public static long lastMessageTimestamp(@Nullable Player player) {
        if (player != null)
            return lastMessageTimestamps.get(player.getUUID());
        return lastMessageTimestamp;
	}

	public static int lastMessageCount() {
        return messagesHandled;
	}

	public static List<ChatEntry> chatLog(int count) {
        var list = new ArrayList<ChatEntry>();
        var iter = messageLog.descendingIterator();
        for (int i = 1; i < count; i++) {
            if (!iter.hasNext())
                break;
            list.add(iter.next());
        }
        return list;
    }

    @SubscribeEvent
    public static void chatMessageSent(ServerChatEvent event) {
        if (event.isCanceled())
            return;

        var uuid = event.getPlayer().getUUID();
        var text = event.getRawText();
        var timestamp = event.getPlayer().serverLevel().gameTime;

        var prefix = ChatEventHandler.getPrefix(player);

        if (prefix != null && text.startsWith(prefix)) {
            lastMessages.put(uuid, text.substring(prefix.length()));
            lastMessageTimestamps.put(uuid, timestamp);
            return;
        }

        if (prefix == null) {
            lastMessages.put(uuid, text);
            lastMessageTimestamps.put(uuid, timestamp);
        }

        if (MoreIotasConfig.server.maxChatLog == 0)
            return true;

        while (!messageLog.isEmpty() && messageLog.size() > MoreIotasConfig.server.maxChatLog) {
            messageLog.removeFirst();
        }

        messageLog.addLast(new ChatEntry(text, timestamp, event.getPlayer().name.string));

        if (lastMessageTimestamp == timestamp) {
            messagesHandled++;
        } else {
            messagesHandled = 1;
            lastMessageTimestamp = timestamp;
        }

        lastMessage = text;
    }
}
