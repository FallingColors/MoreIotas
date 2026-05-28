package ram.talia.moreiotas.common.casting.arithmetic.operator.matrix

import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate.any
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate.ofType
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate.or
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes.DOUBLE
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes.VEC3
import ram.talia.moreiotas.api.asActionResult
import ram.talia.moreiotas.api.matrixWrongSize
import ram.talia.moreiotas.api.times
import ram.talia.moreiotas.common.casting.arithmetic.operator.nextNumOrVecOrSimpleMatrix
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes.MATRIX

object OperatorMatrixDiv : OperatorBasic(2, any(ofType(MATRIX), or(ofType(DOUBLE), ofType(VEC3)))) {
    override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
        val it = iotas.iterator().withIndex()
        val arg0 = it.nextNumOrVecOrSimpleMatrix(arity)
        val arg1 = it.nextNumOrVecOrSimpleMatrix(arity)

        // if the first argument is a number, return number / matrix (right division)
        arg0.a?.let { return ((arg1.asMatrix.invert() * it).asActionResult) }
        // if the second argument is a number, return matrix / number (left division)
        arg1.a?.let { return (arg0.asMatrix.scale(1 / it)).asActionResult }

        val mat0 = arg0.asMatrix
        val mat1 = arg1.asMatrix

        if (mat0.numCols != mat1.numRows)
            throw MishapInvalidIota.matrixWrongSize(iotas.last(), 0, mat0.numCols, null)
        if (mat1.numCols != mat1.numRows)
            throw MishapInvalidIota.matrixWrongSize(iotas.last(), 0, mat1.numRows, mat0.numRows)
        return (mat0.mult(mat1.invert())).asActionResult
    }
}