package ram.talia.moreiotas.common.casting.actions.types

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPositiveDouble
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.common.casting.actions.selectors.OpGetEntitiesBy
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import ram.talia.moreiotas.api.getEntityType

class OpGetEntitiesByDyn(val negate: Boolean) : ConstMediaAction {
    override val argc = 3
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val type = args.getEntityType(0, argc)
        val pos = args.getVec3(1, argc)
        val radius = args.getPositiveDouble(2, argc)
        env.assertVecInRange(pos)

        val aabb = AABB(pos.add(Vec3(-radius, -radius, -radius)), pos.add(Vec3(radius, radius, radius)))
        val entitiesGot = env.world.getEntities(null, aabb) {
            OpGetEntitiesBy.isReasonablySelectable(env, it)
                    && it.distanceToSqr(pos) <= radius * radius
                    && ((it.type == type) != negate) //
        }.sortedBy { it.distanceToSqr(pos) }
        return entitiesGot.map(::EntityIota).asActionResult
    }
}