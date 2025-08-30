package ram.talia.moreiotas.common.casting.actions.items

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.decoration.ItemFrame
import net.minecraft.world.entity.item.ItemEntity
import ram.talia.moreiotas.api.asActionResult

class OpGetHeldItem(private val hand: InteractionHand) : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val stack = when (val holder = args.getEntity(0)) {
            is LivingEntity -> holder.getItemInHand(hand)
            is ItemFrame ->
                if (hand == InteractionHand.MAIN_HAND) {
                    holder.item
                } else {
                    throw MishapBadEntity.of(holder, "item.read.offhand")
                }
            is ItemEntity ->
                if (hand == InteractionHand.MAIN_HAND) {
                    holder.item
                } else {
                    throw MishapBadEntity.of(holder, "item.read.offhand")
                }
            else -> throw MishapBadEntity.of(holder, "item.read.any")
        }

        return stack.asActionResult
    }
}