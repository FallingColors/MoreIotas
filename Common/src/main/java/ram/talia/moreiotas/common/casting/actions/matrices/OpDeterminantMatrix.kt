package ram.talia.moreiotas.common.casting.actions.matrices

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import ram.talia.moreiotas.api.asSimpleMatrix
import ram.talia.moreiotas.api.getNumOrVecOrSimpleMatrix

object OpDeterminantMatrix : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val mat = args.getNumOrVecOrSimpleMatrix(0, argc).asSimpleMatrix

        if (mat.numCols != mat.numRows)
            throw MishapInvalidIota.ofType(args[0], 0, "matrix.square")

        return mat.determinant().asActionResult;
    }
}