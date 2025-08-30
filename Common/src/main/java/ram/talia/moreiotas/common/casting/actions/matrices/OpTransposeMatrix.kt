package ram.talia.moreiotas.common.casting.actions.matrices

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import ram.talia.moreiotas.api.asActionResult
import ram.talia.moreiotas.api.asMatrix
import ram.talia.moreiotas.api.getNumOrVecOrMatrix

object OpTransposeMatrix : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        return args.getNumOrVecOrMatrix(0, argc).asMatrix.transpose().asActionResult
    }
}