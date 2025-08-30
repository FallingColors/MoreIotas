package ram.talia.moreiotas.common.casting.actions.matrices

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import org.jblas.DoubleMatrix
import org.jblas.ranges.RangeUtils
import ram.talia.moreiotas.api.asMatrix
import ram.talia.moreiotas.api.getNumOrVecOrMatrix
import ram.talia.moreiotas.api.matrices.HoleyIntervalRange
import kotlin.math.pow

object OpDeterminantMatrix : ConstMediaAction {
    override val argc = 1

    private fun determinant(mat: DoubleMatrix): Double {
        if (mat.isScalar)
            return mat.scalar()

        var sum = 0.0

        val matWithoutTop = mat.getRows(RangeUtils.interval(1, mat.rows))

        for (i in 0 until mat.rows) {
            sum += (-1.0).pow(i) * mat[0, i] *
                    determinant(matWithoutTop.getColumns(HoleyIntervalRange(0, mat.columns, i)))
        }

        return sum
    }

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val mat = args.getNumOrVecOrMatrix(0, argc).asMatrix

        if (!mat.isSquare)
            throw MishapInvalidIota.ofType(args[0], 0, "matrix.square")
        if (mat.columns > 4)
            throw MishapInvalidIota.of(args[0], 0, "matrix.max_size", 4, 4, mat.columns, mat.rows)

        return determinant(mat).asActionResult
    }
}