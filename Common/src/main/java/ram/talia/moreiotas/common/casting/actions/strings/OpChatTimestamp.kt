package ram.talia.moreiotas.common.casting.actions.strings

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import net.minecraft.world.entity.player.Player
import ram.talia.moreiotas.api.asActionResult
import ram.talia.moreiotas.xplat.IXplatAbstractions

class OpChatTimestamp(private val allChat: Boolean) : ConstMediaAction {
    override val argc = 0

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        if (!allChat)
            return IXplatAbstractions.INSTANCE.lastMessageTimestamp(env.castingEntity as? Player).asActionResult
        return listOf<Iota>(
            DoubleIota(IXplatAbstractions.INSTANCE.lastMessageCount().toDouble()),
            DoubleIota(IXplatAbstractions.INSTANCE.lastMessageTimestamp(null).toDouble())
        )
    }
}