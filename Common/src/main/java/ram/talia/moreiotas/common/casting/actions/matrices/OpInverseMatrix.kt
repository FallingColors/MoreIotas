package ram.talia.moreiotas.common.casting.actions.matrices

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import ram.talia.moreiotas.api.asActionResult
import ram.talia.moreiotas.api.asMatrix
import ram.talia.moreiotas.api.getNumOrVecOrMatrix
import ram.talia.moreiotas.api.matrixWrongSize

object OpInverseMatrix : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val mat = args.getNumOrVecOrMatrix(0, argc).asMatrix
        if (mat.numRows != mat.numCols)
            throw MishapInvalidIota.matrixWrongSize(args[0], 0, mat.numRows, mat.numCols)
        // pseudo-inverse, will see how it works.
        return mat.pseudoInverse().asActionResult;
    }
}