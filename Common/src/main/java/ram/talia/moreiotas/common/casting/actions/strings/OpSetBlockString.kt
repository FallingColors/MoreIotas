package ram.talia.moreiotas.common.casting.actions.strings

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.utils.putList
import com.mojang.datafixers.util.Either
import net.minecraft.core.BlockPos
import net.minecraft.nbt.StringTag
import net.minecraft.network.chat.Component
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.entity.LecternBlockEntity
import net.minecraft.world.level.block.entity.SignBlockEntity
import net.minecraft.world.level.block.entity.SignText
import net.minecraft.world.phys.Vec3
import ram.talia.moreiotas.api.getStringOrList
import ram.talia.moreiotas.api.mod.MoreIotasConfig
import ram.talia.moreiotas.api.nbt.toNbtList

object OpSetBlockString : SpellAction {
    override val argc = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val pos = args.getBlockPos(0, argc)
        val stringOrList = args.getStringOrList(1, argc)

        env.assertVecInRange(pos.center)

        return SpellAction.Result(
                Spell(pos, stringOrList),
                MoreIotasConfig.server.setBlockStringCost,
                listOf(ParticleSpray.burst(Vec3.atCenterOf(pos), 1.0))
        )
    }

    private data class Spell(val pos: BlockPos, val stringOrList: Either<String, List<String>>) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val blockEntity = env.world.getBlockEntity(pos)

            when (blockEntity) {
                is SignBlockEntity -> {
                    if (blockEntity.playerWhoMayEdit != null && blockEntity.playerWhoMayEdit != env.castingEntity?.uuid)
                        return

                    val lines = stringOrList.map(
                        { string -> string.split("\n") },
                        { list -> list.flatMap { it.split("\n") } },
                    )

                    blockEntity.setText(makeSignText(lines), true)
                }

                is LecternBlockEntity -> {
                    if (!blockEntity.book.`is`(Items.WRITABLE_BOOK))
                        return

                    val book = blockEntity.book.copy()

                    val pages = stringOrList.map(
                        { string -> listOf(StringTag.valueOf(string)) },
                        { list -> list.map(StringTag::valueOf) },
                    )

                    book.orCreateTag.putList("pages", pages.toNbtList())

                    blockEntity.book = book
                }

                else -> return
            }

            blockEntity.setChanged()
            val blockState = env.world.getBlockState(pos)
            env.world.sendBlockUpdated(pos, blockState, blockState, 3)
        }

        private fun makeSignText(lines: List<String>): SignText = SignText(
            arrayOf(
                Component.literal(lines[0]),
                lines.getOrNull(1)?.let { Component.literal(it) } ?: Component.empty(),
                lines.getOrNull(2)?.let { Component.literal(it) } ?: Component.empty(),
                lines.getOrNull(3)?.let { Component.literal(it) } ?: Component.empty()),
            arrayOf(
                Component.literal(lines[0]),
                lines.getOrNull(1)?.let { Component.literal(it) } ?: Component.empty(),
                lines.getOrNull(2)?.let { Component.literal(it) } ?: Component.empty(),
                lines.getOrNull(3)?.let { Component.literal(it) } ?: Component.empty()),
            DyeColor.BLACK, false
        )
    }
}