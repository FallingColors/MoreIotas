package ram.talia.moreiotas.common.casting.arithmetic.operator.type

import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate.any
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate.ofType
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes.ENTITY
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes.VEC3
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.decoration.ItemFrame
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.Items
import ram.talia.moreiotas.api.asActionResult
import ram.talia.moreiotas.api.casting.iota.ItemStackIota
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes.ITEM_STACK

object OperatorExtractItem : OperatorBasic(1, IotaMultiPredicate.all(any(ofType(VEC3), ofType(ENTITY), ofType(ITEM_STACK)))) {
    override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
        val iota = iotas.first()

        return when (iota) {
            is Vec3Iota -> {
                if (!env.isVecInRange(iota.vec3))
                    null.asActionResult
                else {
                    val blockState = env.world.getBlockState(BlockPos.containing(iota.vec3))
                    blockState.block.asActionResult
                }
            }
            is EntityIota -> {
                when (val entity = iota.entity) {
                    is ItemEntity -> entity.item.item.asActionResult
                    is ItemFrame -> entity.item.item.asActionResult
                    else -> throw MishapInvalidIota.of(iota, 0, "blockitementityitemframeitem")
                }
            }
            is ItemStackIota -> {
                if (iota.itemStack.isEmpty)
                    Items.AIR.asActionResult
                else
                    iota.itemStack.item.asActionResult
            }
            else -> throw IllegalStateException("iota argument to OperatorExtractItem must be one of Vec3, Entity, or ItemStack.")
        }
    }
}