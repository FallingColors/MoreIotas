package ram.talia.moreiotas.common.casting.actions.matrices

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import ram.talia.moreiotas.api.asActionResult
import ram.talia.moreiotas.api.asSimpleMatrix
import ram.talia.moreiotas.api.getNumOrVecOrSimpleMatrix
import ram.talia.moreiotas.api.matrixWrongSize

object OpInverseMatrix : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val mat = args.getNumOrVecOrSimpleMatrix(0, argc).asSimpleMatrix
        // pseudo-inverse works even on rectangular matrices
        return mat.pseudoInverse().asActionResult;
    }
}