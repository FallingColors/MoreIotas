package ram.talia.moreiotas.common.casting.arithmetic.operator.matrix

import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate.any
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate.ofType
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate.or
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes.*
import ram.talia.moreiotas.api.*
import ram.talia.moreiotas.common.casting.arithmetic.operator.nextNumOrVecOrSimpleMatrix
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes.MATRIX

class OperatorMatrixAdd(private val subtract: Boolean)
    : OperatorBasic(2, any(ofType(MATRIX), or(ofType(DOUBLE), ofType(VEC3)))) {
    override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
        val it = iotas.iterator().withIndex()
        val mat0 = it.nextNumOrVecOrSimpleMatrix(arity).asSimpleMatrix
        val mat1 = it.nextNumOrVecOrSimpleMatrix(arity).asSimpleMatrix

        if (mat0.numRows != mat1.numRows || mat0.numCols != mat1.numCols)
            throw MishapInvalidIota.matrixWrongSize(iotas.last(), 0, mat0.numRows, mat0.numCols)

        return if (subtract) { mat0 - mat1 } else { mat0 + mat1 }.asActionResult
    }
}