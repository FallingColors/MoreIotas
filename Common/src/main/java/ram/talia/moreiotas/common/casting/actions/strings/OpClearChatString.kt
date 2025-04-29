package ram.talia.moreiotas.common.casting.actions.strings

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import net.minecraft.world.entity.player.Player
import ram.talia.moreiotas.api.asActionResult
import ram.talia.moreiotas.xplat.IXplatAbstractions

class OpClearChatString() : ConstMediaAction {
    override val argc = 0

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        IXplatAbstractions.INSTANCE.resetMessage(env.castingEntity as Player);
        return listOf()
    }
}