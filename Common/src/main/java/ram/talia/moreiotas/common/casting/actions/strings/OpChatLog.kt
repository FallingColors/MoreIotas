package ram.talia.moreiotas.common.casting.actions.strings

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPositiveInt
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import ram.talia.moreiotas.xplat.IXplatAbstractions
import ram.talia.moreiotas.api.casting.iota.StringIota as ApiStringIota

class OpChatLog() : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val count = args.getPositiveInt(0, argc)
        val logs = IXplatAbstractions.INSTANCE.chatLog(count)
        val messages = mutableListOf<Iota>()
        val timestamps = mutableListOf<Iota>()
        val names = mutableListOf<Iota>()

        for (entry in logs) {
            messages.add(ApiStringIota.make(entry.message))
            timestamps.add(DoubleIota(entry.worldTime.toDouble()))
            names.add(ApiStringIota.make(entry.username))
        }

        return listOf(ListIota(messages), ListIota(timestamps), ListIota(names))
    }
}