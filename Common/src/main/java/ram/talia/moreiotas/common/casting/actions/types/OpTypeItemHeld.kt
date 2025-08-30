package ram.talia.moreiotas.common.casting.actions.types

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import net.minecraft.world.item.Items
import ram.talia.moreiotas.api.asActionResult

object OpTypeItemHeld : ConstMediaAction {
    override val argc = 0

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        return env.getHeldItemToOperateOn { it.item != Items.AIR }?.stack?.item?.takeUnless { it == Items.AIR }?.asActionResult ?: null.asActionResult
    }
}