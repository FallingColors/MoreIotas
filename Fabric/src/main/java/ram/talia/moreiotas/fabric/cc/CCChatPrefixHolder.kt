package ram.talia.moreiotas.fabric.cc

import at.petrak.hexcasting.api.utils.hasString
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.player.Player

class CCChatPrefixHolder(private val player: Player, var prefix: String? = null) : AutoSyncedComponent {

    override fun readFromNbt(tag: CompoundTag) {
        if (tag.hasString(TAG_CHAT_PREFIX))
            prefix = tag.getString(TAG_CHAT_PREFIX)
    }

    override fun writeToNbt(tag: CompoundTag) {
        prefix?.let { tag.putString(TAG_CHAT_PREFIX, it) }
    }

    companion object {
        const val TAG_CHAT_PREFIX = "moreiotas:prefix"
    }
}