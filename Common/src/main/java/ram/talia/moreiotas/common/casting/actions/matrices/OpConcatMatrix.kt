package ram.talia.moreiotas.common.casting.actions.matrices

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import org.jblas.DoubleMatrix
import ram.talia.moreiotas.api.asActionResult
import ram.talia.moreiotas.api.asMatrix
import ram.talia.moreiotas.api.getNumOrVecOrMatrix
import ram.talia.moreiotas.api.matrixWrongSize
import ram.talia.moreiotas.api.mod.MoreIotasConfig

class OpConcatMatrix(private val concatVertically: Boolean) : ConstMediaAction {
    override val argc = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val mat0 = args.getNumOrVecOrMatrix(0, argc).asMatrix
        val mat1 = args.getNumOrVecOrMatrix(1, argc).asMatrix

        if (concatVertically) {
            if (mat0.columns != mat1.columns)
                throw MishapInvalidIota.matrixWrongSize(args[1], 0, null, mat0.columns)
            if (mat0.rows + mat1.rows > MoreIotasConfig.server.maxMatrixSize)
                throw MishapInvalidIota.of(args[1],
                        0,
                        "matrix.max_size",
                        MoreIotasConfig.server.maxMatrixSize,
                        mat0.rows + mat1.rows,
                        mat0.columns)
        } else {
            if (mat0.rows != mat1.rows)
                throw MishapInvalidIota.matrixWrongSize(args[1], 0, mat0.rows, null)
            if (mat0.columns + mat1.columns > MoreIotasConfig.server.maxMatrixSize)
                throw MishapInvalidIota.of(args[1],
                        0,
                        "matrix.max_size",
                        MoreIotasConfig.server.maxMatrixSize,
                        mat0.rows,
                        mat0.columns + mat1.columns)
        }

        return if (concatVertically)
            DoubleMatrix.concatVertically(mat0, mat1).asActionResult
        else DoubleMatrix.concatHorizontally(mat0, mat1).asActionResult
    }
}