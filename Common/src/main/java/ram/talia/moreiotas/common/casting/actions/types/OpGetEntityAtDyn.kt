package ram.talia.moreiotas.common.casting.actions.types

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.common.casting.actions.selectors.OpGetEntitiesBy
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import ram.talia.moreiotas.api.getEntityType

object OpGetEntityAtDyn : ConstMediaAction {
    override val argc = 2
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val type = args.getEntityType(0, argc)
        val pos = args.getVec3(1, argc)
        env.assertVecInRange(pos)
        val aabb = AABB(pos.add(Vec3(-0.5, -0.5, -0.5)), pos.add(Vec3(0.5, 0.5, 0.5)))
        val entitiesGot = env.world.getEntities(null, aabb) {
            OpGetEntitiesBy.isReasonablySelectable(env, it) && it.type == type
        }.sortedBy { it.distanceToSqr(pos) }

        val entity = entitiesGot.getOrNull(0)
        return entity.asActionResult
    }
}