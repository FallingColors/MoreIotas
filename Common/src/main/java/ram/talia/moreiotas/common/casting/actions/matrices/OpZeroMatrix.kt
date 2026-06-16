package ram.talia.moreiotas.common.casting.actions.matrices

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPositiveIntUnderInclusive
import at.petrak.hexcasting.api.casting.iota.Iota
import org.ejml.simple.SimpleMatrix
import ram.talia.moreiotas.api.asActionResult
import ram.talia.moreiotas.api.mod.MoreIotasConfig

object OpZeroMatrix : ConstMediaAction {
    override val argc = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        return SimpleMatrix.filled(
                args.getPositiveIntUnderInclusive(0, MoreIotasConfig.server.maxMatrixSize, argc),
                args.getPositiveIntUnderInclusive(1, MoreIotasConfig.server.maxMatrixSize, argc), 0.0).asActionResult
    }
}