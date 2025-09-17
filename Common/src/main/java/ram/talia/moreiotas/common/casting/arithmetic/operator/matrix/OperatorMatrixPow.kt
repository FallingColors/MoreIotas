package ram.talia.moreiotas.common.casting.arithmetic.operator.matrix

import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate.either
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate.pair
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate.ofType
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes.DOUBLE
import org.jblas.DoubleMatrix
import org.jblas.MatrixFunctions
import org.jblas.Singular
import org.jblas.Solve
import ram.talia.moreiotas.api.asActionResult
import ram.talia.moreiotas.api.asMatrix
import ram.talia.moreiotas.api.matrixWrongSize
import ram.talia.moreiotas.api.times
import ram.talia.moreiotas.common.casting.arithmetic.operator.nextNumOrVecOrMatrix
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes.MATRIX
import kotlin.math.*

object OperatorMatrixPow : OperatorBasic(2, either(pair(ofType(MATRIX), ofType(DOUBLE)), pair(ofType(DOUBLE), ofType(MATRIX)))) {
    override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
        val it = iotas.iterator().withIndex()
        val arg0 = it.nextNumOrVecOrMatrix(arity)
        val arg1 = it.nextNumOrVecOrMatrix(arity)

        arg0.a?.let {
            val mat = arg1.asMatrix
            if (mat.rows != mat.columns)
                throw MishapInvalidIota.matrixWrongSize(iotas.last(), 0, mat.rows, mat.rows)

            return MatrixFunctions.expm(ln(it) * mat).asActionResult
        }

        val mat = arg0.asMatrix
        if (mat.rows != mat.columns)
            throw MishapInvalidIota.matrixWrongSize(iotas.last(), 0, mat.rows, mat.rows)
        val power = arg1.a ?: throw MishapInvalidIota.of(iotas.last(), 0, "double")

        return fractionalPowerMatrix(mat, power).asActionResult
    }

    /**
     * Takes a matrix A and a power p and returns A^p.
     * https://github.com/scipy/scipy/blob/cfe80116aaa145061246b8aec0e98248fbefb835/scipy/linalg/_matfuncs_inv_ssq.py#L671
     */
    internal fun fractionalPowerMatrix(matrix: DoubleMatrix, power: Double): DoubleMatrix {
        if (power < 0)
            return fractionalPowerMatrix(Solve.pinv(matrix), power.absoluteValue)

        val rounded = power.roundToInt()
        if (abs(power - rounded) <= DoubleIota.TOLERANCE)
            return intMatrixPower(matrix, rounded)

        throw MishapInvalidIota.of(DoubleIota(power), 0, "int") // TODO, Figure out Shur decomposition.

        val svdsMat = Singular.SVDValues(matrix)
        val svds = (0 until svdsMat.rows).map { svdsMat[it, it] }.sortedDescending()

        /*
        Only use this if all elements of the singular values are non-zero, meaning the matrix is non-singular.
         */
        if (svds.last() > 0.0) {
            val k2 = svds[0] / svds.last()
            val p1 = power - floor(power)
            val p2 = power - ceil(power)

            val (a, b) = if (p1 * k2.pow(1 - p1) <= -p2 * k2) floor(power).roundToInt() to p1 else ceil(power).roundToInt() to p2

            val R = remainderMatrixPower(matrix, b)
            val Q = intMatrixPower(matrix, a)
            return Q.mmul(R)
        }

//        val a = floor(power).roundToInt()
//        val b = power - a
//        val R = TODO()
//        val Q = intMatrixPower(matrix, a)
//        return Q.mmul(R)
    }

    internal fun remainderMatrixPower(matrix: DoubleMatrix, power: Double): DoubleMatrix {
        TODO()
    }

    internal fun intMatrixPower(matrix: DoubleMatrix, power: Int): DoubleMatrix {
        if (power == 0)
            return DoubleMatrix.eye(matrix.rows)

        if (power == 1)
            return matrix
        if (power == 2)
            return matrix.mmul(matrix)
        if (power == 3)
            return matrix.mmul(matrix.mmul(matrix))

        var z: DoubleMatrix? = null
        var result: DoubleMatrix? = null
        var currPower = power
        var bit: Int

        while (currPower > 0) {
            z = if (z == null) matrix else z.mmul(z)
            bit = currPower.mod(2)
            currPower /= 2
            if (bit != 0)
                result = if (result == null) z else result.mmul(z)
        }

        return result!!
    }
}