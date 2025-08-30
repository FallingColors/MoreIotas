package ram.talia.moreiotas.common.casting.actions.matrices

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import org.jblas.Solve
import ram.talia.moreiotas.api.*

object OpInverseMatrix : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val mat = args.getNumOrVecOrMatrix(0, argc).asMatrix
        if (mat.rows != mat.columns)
            throw MishapInvalidIota.matrixWrongSize(args[0], 0, mat.rows, mat.rows)
        // pseudo-inverse, will see how it works.
        return Solve.pinv(mat).asActionResult
    }
}