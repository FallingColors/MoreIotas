package ram.talia.moreiotas.common.casting.actions.items

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPositiveInt
import at.petrak.hexcasting.api.casting.iota.Iota
import ram.talia.moreiotas.api.asActionResult
import ram.talia.moreiotas.api.getItemStack

object OpSetItemCount : ConstMediaAction {
    override val argc = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val stack = args.getItemStack(0, argc)
        val newCount = args.getPositiveInt(1, argc)

        return stack.copyWithCount(newCount).asActionResult
    }
}