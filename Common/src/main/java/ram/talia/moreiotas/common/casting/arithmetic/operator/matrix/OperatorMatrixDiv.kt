package ram.talia.moreiotas.common.casting.arithmetic.operator.matrix

import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate.any
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate.ofType
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate.or
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes.DOUBLE
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes.VEC3
import org.jblas.Solve
import ram.talia.moreiotas.api.asActionResult
import ram.talia.moreiotas.api.asMatrix
import ram.talia.moreiotas.api.matrixWrongSize
import ram.talia.moreiotas.common.casting.arithmetic.operator.nextNumOrVecOrMatrix
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes.MATRIX

object OperatorMatrixDiv : OperatorBasic(2, any(ofType(MATRIX), or(ofType(DOUBLE), ofType(VEC3)))) {
    override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
        val it = iotas.iterator().withIndex()
        val arg0 = it.nextNumOrVecOrMatrix(arity)
        val arg1 = it.nextNumOrVecOrMatrix(arity)

        arg0.a?.let { return (arg1.asMatrix.divi(it)).asActionResult }
        arg1.a?.let { return (arg0.asMatrix.rdivi(it)).asActionResult }

        val mat0 = arg0.asMatrix
        val mat1 = arg1.asMatrix

        if (mat0.columns != mat1.rows)
            throw MishapInvalidIota.matrixWrongSize(iotas.last(), 0, mat0.columns, null)
        if (mat1.columns != mat1.rows)
            throw MishapInvalidIota.matrixWrongSize(iotas.last(), 0, mat1.rows, mat0.rows)
        return (mat0.mmul(Solve.pinv(mat1))).asActionResult
    }
}