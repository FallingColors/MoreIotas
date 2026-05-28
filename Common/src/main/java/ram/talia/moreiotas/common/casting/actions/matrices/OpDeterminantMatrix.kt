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
        // I don't believe the ejml determinant requires that the matrix be below size four.
        //if (mat.numCols > 4)
            //throw MishapInvalidIota.of(args[0], 0, "matrix.max_size", 4, 4, mat.numCols, mat.numRows)

        return mat.determinant().asActionResult;
    }
}