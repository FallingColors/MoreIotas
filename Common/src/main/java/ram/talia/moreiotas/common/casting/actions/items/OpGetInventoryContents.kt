package ram.talia.moreiotas.common.casting.actions.items

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import net.minecraft.core.Direction
import net.minecraft.world.Container
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.phys.Vec3
import ram.talia.moreiotas.api.casting.iota.ItemStackIota
import ram.talia.moreiotas.api.casting.iota.ItemTypeIota

class OpGetInventoryContents(private val returnStacks: Boolean) : ConstMediaAction {
    override val argc = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val pos = args.getBlockPos(0, argc)
        env.assertVecInRange(pos.center)

        val inventory = env.world.getBlockEntity(pos)

        if (inventory == null || inventory !is Container) {
            return listOf(NullIota())
        }

        val sideVec = args.getVec3(1, argc)

        val slots = if (sideVec.distanceToSqr(Vec3.ZERO) > DoubleIota.TOLERANCE * DoubleIota.TOLERANCE && inventory is WorldlyContainer) {
            inventory.getSlotsForFace(Direction.getNearest(sideVec.x, sideVec.y, sideVec.z)).asIterable()
        } else {
            (0 until inventory.containerSize)
        }

        val items = slots.map {
            val stack = inventory.getItem(it)
            if (returnStacks) {
                ItemStackIota.createFiltered(stack)
            } else {
                ItemTypeIota(stack.item)
            }
        }

        return items.asActionResult
    }
}