package ram.talia.moreiotas.fabric.cc

import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.utils.getList
import at.petrak.hexcasting.fabric.cc.HexCardinalComponents.IOTA_HOLDER
import at.petrak.hexcasting.fabric.cc.adimpl.CCItemIotaHolder
import dev.onyxstudios.cca.api.v3.component.ComponentKey
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Items
import ram.talia.moreiotas.api.MoreIotasAPI.modLoc
import ram.talia.moreiotas.api.casting.iota.StringIota

class MoreIotasCardinalComponents : EntityComponentInitializer, ItemComponentInitializer {
	override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
		registry.registerForPlayers(CHAT_PREFIX_HOLDER, ::CCChatPrefixHolder, RespawnCopyStrategy.ALWAYS_COPY)
	}

	override fun registerItemComponentFactories(registry: ItemComponentFactoryRegistry) {
		registry.register(Items.WRITABLE_BOOK, IOTA_HOLDER) { stack ->
			CCItemIotaHolder.Static(stack) {
				s -> s.tag.getList("pages", Tag.TAG_STRING)?.map { StringIota.make(it.asString) }?.let { ListIota(it) }
			}
		}
		registry.register(Items.WRITTEN_BOOK, IOTA_HOLDER) { stack ->
			CCItemIotaHolder.Static(stack) {
				s -> s.tag.getList("pages", Tag.TAG_STRING)?.map {
                StringIota.make(Component.Serializer.fromJson(it.asString)?.string ?: "")
				}?.let { ListIota(it) }
			}
		}
	}

	companion object {
		@JvmField
		val CHAT_PREFIX_HOLDER: ComponentKey<CCChatPrefixHolder> = ComponentRegistry.getOrCreate(
			modLoc("chat_prefix_holder"),
			CCChatPrefixHolder::class.java
		)
	}
}