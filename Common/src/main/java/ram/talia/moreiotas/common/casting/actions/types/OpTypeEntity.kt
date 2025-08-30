package ram.talia.moreiotas.common.casting.actions.types

import at.petrak.hexcasting.api.casting.*
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import ram.talia.moreiotas.api.asActionResult

object OpTypeEntity : ConstMediaAction {
	override val argc = 1

	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		return args.getEntity(0, argc).type.asActionResult
	}
}