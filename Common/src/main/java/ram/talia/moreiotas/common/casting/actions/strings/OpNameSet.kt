package ram.talia.moreiotas.common.casting.actions.strings

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.item.ItemEntity
import ram.talia.moreiotas.api.getString
import ram.talia.moreiotas.api.mod.MoreIotasConfig

object OpNameSet : SpellAction {
    override val argc = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val newName = args.getString(0, argc)
        val entityToRename = args.getEntity(1, argc)

        env.assertEntityInRange(entityToRename)

        return SpellAction.Result(
            Spell(newName, entityToRename),
            MoreIotasConfig.server.setBlockStringCost,
            listOf(ParticleSpray.burst(entityToRename.position(), 0.5))
        )
    }

    private data class Spell(val name: String, val entity: Entity) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val nameComp = Component.literal(name)
            entity.customName = nameComp
            if (entity is Mob)
                entity.setPersistenceRequired()
            else if (entity is ItemEntity)
                entity.item.setHoverName(nameComp)
        }
    }
}