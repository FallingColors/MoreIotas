package ram.talia.moreiotas.common.casting.actions.matrices

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import ram.talia.moreiotas.api.*

object OpAddMatrix : ConstMediaAction {
    override val argc = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val mat0 = args.getNumOrVecOrMatrix(0, argc).asMatrix
        val mat1 = args.getNumOrVecOrMatrix(1, argc).asMatrix
        if (mat0.rows != mat1.rows || mat0.columns != mat1.columns)
            throw MishapInvalidIota.matrixWrongSize(args[1], 0, mat0.rows, mat0.columns)

        return (mat0 + mat1).asActionResult
    }
}