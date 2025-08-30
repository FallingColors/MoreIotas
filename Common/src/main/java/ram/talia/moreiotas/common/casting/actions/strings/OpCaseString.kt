package ram.talia.moreiotas.common.casting.actions.strings

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import ram.talia.moreiotas.api.asActionResult
import ram.talia.moreiotas.api.getBoolOrNull
import ram.talia.moreiotas.api.getString

/**
 * Takes a string and a boolean | null. On true, converts to uppercase, on false, converts to lowercase. On null, toggles case.
 */
object OpCaseString : ConstMediaAction {
    override val argc = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val string = args.getString(0, argc)

        val stringOut = when (args.getBoolOrNull(1, argc)) {
            true -> string.uppercase()
            false -> string.lowercase()
            else -> string.map { if (it.isLowerCase()) it.uppercase() else it.lowercase() }.joinToString("")
        }

        return stringOut.asActionResult
    }
}