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
import ram.talia.moreiotas.api.asActionResult
import ram.talia.moreiotas.api.matrixWrongSize
import ram.talia.moreiotas.common.casting.arithmetic.operator.nextNumOrVecOrSimpleMatrix
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes.MATRIX
import kotlin.math.*
import ram.talia.moreiotas.api.matrices.MatrixExponentials.matrixExponential;
import org.ejml.simple.SimpleMatrix

object OperatorMatrixPow : OperatorBasic(2, either(pair(ofType(MATRIX), ofType(DOUBLE)), pair(ofType(DOUBLE), ofType(MATRIX)))) {
    override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
        val it = iotas.iterator().withIndex()
        val arg0 = it.nextNumOrVecOrSimpleMatrix(arity)
        val arg1 = it.nextNumOrVecOrSimpleMatrix(arity)

        arg0.a?.let {
            val mat = arg1.asMatrix
            if (mat.numRows != mat.numCols)
                throw MishapInvalidIota.matrixWrongSize(iotas.last(), 0, mat.numRows, mat.numRows)
            return matrixExponential(ln(it) * mat).asActionResult
        }

        val mat = arg0.asMatrix
        if (mat.numRows != mat.numCols)
            throw MishapInvalidIota.matrixWrongSize(iotas.last(), 0, mat.numRows, mat.numRows)
        val power = arg1.a ?: throw MishapInvalidIota.of(iotas.last(), 0, "double")

        return fractionalPowerMatrix(mat, power).asActionResult
    }

    /**
     * Takes a matrix A and a power p and returns A^p.
     * https://github.com/scipy/scipy/blob/cfe80116aaa145061246b8aec0e98248fbefb835/scipy/linalg/_matfuncs_inv_ssq.py#L671
     */
    internal fun fractionalPowerMatrix(matrix: SimpleMatrix, power: Double): SimpleMatrix {
        if (power < 0)
            return fractionalPowerMatrix(matrix.pseudoInverse(), power.absoluteValue)

        val rounded = power.roundToInt()
        if (abs(power - rounded) <= DoubleIota.TOLERANCE)
            return intMatrixPower(matrix, rounded)

        throw MishapInvalidIota.of(DoubleIota(power), 0, "int") // TODO, Figure out Shur decomposition.

        /*val svdsMat = Singular.SVDValues(matrix)
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
//        return Q.mmul(R)*/
    }

    /*internal fun remainderMatrixPower(matrix: DoubleMatrix, power: Double): DoubleMatrix {
        TODO()
    }*/

    internal fun intMatrixPower(matrix: SimpleMatrix, power: Int): SimpleMatrix {
        if (power == 0)
            return SimpleMatrix.identity(matrix.numRows)

        if (power == 1)
            return matrix
        if (power == 2)
            return matrix.mult(matrix)
        if (power == 3)
            return matrix.mult(matrix.mult(matrix))

        var z: SimpleMatrix? = null
        var result: SimpleMatrix? = null
        var currPower = power
        var bit: Int

        while (currPower > 0) {
            z = if (z == null) matrix else z.mult(z)
            bit = currPower.mod(2)
            currPower = currPower shr 1;
            if (bit != 0)
                result = if (result == null) z else result.mult(z)
        }

        return result!!
    }
}