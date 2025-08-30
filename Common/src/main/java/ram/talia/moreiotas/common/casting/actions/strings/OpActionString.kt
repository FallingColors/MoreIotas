package ram.talia.moreiotas.common.casting.actions.strings

import at.petrak.hexcasting.api.HexAPI
import at.petrak.hexcasting.api.casting.PatternShapeMatch
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.SpecialPatterns.*
import at.petrak.hexcasting.api.casting.getPattern
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.mod.HexTags
import at.petrak.hexcasting.api.utils.isOfTag
import at.petrak.hexcasting.common.casting.PatternRegistryManifest
import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.network.chat.Component
import ram.talia.moreiotas.api.asActionResult

/**
 * Takes a pattern iota, returns the name of that pattern, or "Unknown" if that pattern doesn't exist (or if it's a great pattern).
 */
object OpActionString : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val pattern = args.getPattern(0, argc)

        if (pattern.sigsEqual(INTROSPECTION))
            return Component.translatable("hexcasting.action.rawhook.hexcasting:open_paren").string.asActionResult
        if (pattern.sigsEqual(RETROSPECTION))
            return Component.translatable("hexcasting.action.rawhook.hexcasting:close_paren").string.asActionResult
        if (pattern.sigsEqual(CONSIDERATION))
            return Component.translatable("hexcasting.action.rawhook.hexcasting:escape").string.asActionResult
        if (pattern.sigsEqual(EVANITION))
            return Component.translatable("hexcasting.action.rawhook.hexcasting:undo").string.asActionResult

        val action = PatternRegistryManifest.matchPattern(pattern, env, true) ?: return null.asActionResult

        return when (action) {
            is PatternShapeMatch.Normal ->
                HexAPI.instance().getActionI18n(action.key, isOfTag(IXplatAbstractions.INSTANCE.actionRegistry, action.key, HexTags.Actions.REQUIRES_ENLIGHTENMENT)).string.asActionResult
            is PatternShapeMatch.PerWorld ->
                HexAPI.instance().getActionI18n(action.key, isOfTag(IXplatAbstractions.INSTANCE.actionRegistry, action.key, HexTags.Actions.REQUIRES_ENLIGHTENMENT)).string.asActionResult
            is PatternShapeMatch.Special ->
                action.handler.name.string.asActionResult
            else -> null.asActionResult
        }
    }
}