package ram.talia.moreiotas.common.casting.actions.strings

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import net.minecraft.world.entity.player.Player
import ram.talia.moreiotas.api.getStringOrNull
import ram.talia.moreiotas.xplat.IXplatAbstractions

object OpSetChatPrefix : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        (env.castingEntity as? Player)?.let { IXplatAbstractions.INSTANCE.setChatPrefix(it, args.getStringOrNull(0, argc)) }
        return listOf()
    }
}