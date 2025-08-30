package ram.talia.moreiotas.common.casting.actions.strings

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import net.minecraft.world.entity.item.ItemEntity
import ram.talia.moreiotas.api.asActionResult

object OpNameGet : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val entity = args.getEntity(0, argc)
        env.assertEntityInRange(entity)
        val name = when (entity) {
            is ItemEntity -> entity.item.displayName.string
            else -> entity.displayName.string
        }
        return name.asActionResult
    }
}