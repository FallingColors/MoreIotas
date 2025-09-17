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
import ram.talia.moreiotas.common.casting.arithmetic.operator.nextNumOrVecOrMatrix
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes.MATRIX

class OperatorMatrixAdd(private val subtract: Boolean)
    : OperatorBasic(2, any(ofType(MATRIX), or(ofType(DOUBLE), ofType(VEC3)))) {
    override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
        val it = iotas.iterator().withIndex()
        val mat0 = it.nextNumOrVecOrMatrix(arity).asMatrix
        val mat1 = it.nextNumOrVecOrMatrix(arity).asMatrix

        if (mat0.rows != mat1.rows || mat0.columns != mat1.columns)
            throw MishapInvalidIota.matrixWrongSize(iotas.last(), 0, mat0.rows, mat0.columns)

        return if (subtract) { mat0 - mat1 } else { mat0 + mat1 }.asActionResult
    }
}