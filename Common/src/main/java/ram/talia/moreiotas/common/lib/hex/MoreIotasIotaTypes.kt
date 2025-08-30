package ram.talia.moreiotas.common.lib.hex

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import net.minecraft.resources.ResourceLocation
import org.jetbrains.annotations.ApiStatus
import ram.talia.moreiotas.api.MoreIotasAPI.modLoc
import ram.talia.moreiotas.api.casting.iota.*
import java.util.function.BiConsumer

object MoreIotasIotaTypes {
    @JvmStatic
    @ApiStatus.Internal
    fun registerTypes(r: BiConsumer<IotaType<*>, ResourceLocation>) {
        for ((key, value) in TYPES) {
            r.accept(value, key)
        }
    }

    private val TYPES: MutableMap<ResourceLocation, IotaType<*>> = LinkedHashMap()

    @JvmField
    val STRING: IotaType<StringIota> = type("string", StringIota.TYPE)
    @JvmField
    val MATRIX: IotaType<MatrixIota> = type("matrix", MatrixIota.TYPE)
    @JvmField
    val IOTA_TYPE: IotaType<IotaTypeIota> = type("iota_type", IotaTypeIota.TYPE)
    @JvmField
    val ENTITY_TYPE: IotaType<EntityTypeIota> = type("entity_type", EntityTypeIota.TYPE)
    @JvmField
    val ITEM_TYPE: IotaType<ItemTypeIota> = type("item_type", ItemTypeIota.TYPE)
    @JvmField
    val ITEM_STACK: IotaType<ItemStackIota> = type("item_stack", ItemStackIota.TYPE)

    private fun <U : Iota, T : IotaType<U>> type(name: String, type: T): T {
        val old = TYPES.put(modLoc(name), type)
        require(old == null) { "Typo? Duplicate id $name" }
        return type
    }
}