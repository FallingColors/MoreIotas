package ram.talia.moreiotas.common.casting.actions.matrices

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPositiveIntUnderInclusive
import at.petrak.hexcasting.api.casting.iota.Iota
import org.jblas.DoubleMatrix
import ram.talia.moreiotas.api.asActionResult
import ram.talia.moreiotas.api.mod.MoreIotasConfig

object OpIdentityMatrix : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        return DoubleMatrix.eye(args.getPositiveIntUnderInclusive(0, MoreIotasConfig.server.maxMatrixSize, argc)).asActionResult
    }
}