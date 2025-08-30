package ram.talia.moreiotas.common.casting.actions.strings

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.utils.getList
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.entity.LecternBlockEntity
import net.minecraft.world.level.block.entity.SignBlockEntity
import net.minecraft.world.level.block.entity.SignText
import ram.talia.moreiotas.api.asActionResult
import ram.talia.moreiotas.api.casting.iota.StringIota

object OpGetBlockString : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val pos = args.getBlockPos(0, argc)

        env.assertVecInRange(pos.center)

        val blockEntity = env.world.getBlockEntity(pos)

        if (blockEntity is SignBlockEntity) {
            var string = blockEntity.getText(true).getMessage(0, false).string
            for (i in 1 until SignText.LINES) {
                string += "\n" + blockEntity.getText(true).getMessage(i, false).string
            }

            return string.asActionResult
        }
        if (blockEntity is LecternBlockEntity) {
            val book = blockEntity.book

            return if (book.`is`(Items.WRITABLE_BOOK))
                    book.tag.getList("pages", Tag.TAG_STRING)?.map { StringIota.make(it.asString) }?.let { listOf(ListIota(it)) } ?: null.asActionResult
                else if (book.`is`(Items.WRITTEN_BOOK))
                    book.tag.getList("pages", Tag.TAG_STRING)?.map {
                        StringIota.make(Component.Serializer.fromJson(it.asString)?.string ?: "")
                    }?.let { listOf(ListIota(it)) } ?: null.asActionResult
                else null.asActionResult
        }

        return null.asActionResult
    }
}