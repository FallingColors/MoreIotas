package ram.talia.moreiotas.forge.cap

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.utils.getList
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import ram.talia.moreiotas.api.casting.iota.StringIota

object BookHelpers {
    @JvmStatic
    fun getWritableIota(s: ItemStack): Iota?
            = s.tag.getList("pages", Tag.TAG_STRING)?.map { StringIota.make(it.asString) }?.let { ListIota(it) }

    @JvmStatic
    fun getWrittenIota(s: ItemStack): Iota? = s.tag.getList("pages", Tag.TAG_STRING)?.map {
        StringIota.make(Component.Serializer.fromJson(it.asString)?.string ?: "")
        }?.let { ListIota(it) }
}