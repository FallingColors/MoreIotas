package ram.talia.moreiotas.common.casting.arithmetic

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic.*
import at.petrak.hexcasting.api.casting.arithmetic.engine.InvalidOperatorException
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBinary
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorUnary
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import org.jblas.DoubleMatrix
import org.jblas.MatrixFunctions
import ram.talia.moreiotas.api.casting.iota.MatrixIota
import ram.talia.moreiotas.api.matrixWrongSize
import ram.talia.moreiotas.common.casting.arithmetic.operator.matrix.OperatorMatrixAdd
import ram.talia.moreiotas.common.casting.arithmetic.operator.matrix.OperatorMatrixDiv
import ram.talia.moreiotas.common.casting.arithmetic.operator.matrix.OperatorMatrixMul
import ram.talia.moreiotas.common.casting.arithmetic.operator.matrix.OperatorMatrixPow
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes.MATRIX
import java.util.function.BinaryOperator
import java.util.function.Function
import java.util.function.UnaryOperator

object MatrixArithmetic : Arithmetic {
    @JvmField val ALTADD = HexPattern.fromAngles("waawawaeawwaea", HexDir.EAST)
    @JvmField val ALTMUL = HexPattern.fromAngles("waqawawwaeaww", HexDir.SOUTH_EAST)
    @JvmField val ALTDIV = HexPattern.fromAngles("wdedwdwwdqdww", HexDir.NORTH_EAST)
    @JvmField val ALTPOW = HexPattern.fromAngles("wedewqawwawqwa", HexDir.NORTH_EAST)

    private val OPS = listOf(
        ADD,
        SUB,
        MUL,
        ALTMUL,
        DIV,
        ALTDIV,
        ABS,
        POW,
        ALTPOW,
        FLOOR,
        CEIL,
//        SIN,
//        COS,
//        TAN,
//        ARCSIN,
//        ARCCOS,
//        ARCTAN,
//        ARCTAN2,
//        LOG,
        REV
    )

    override fun arithName(): String = "matrix_maths"

    override fun opTypes(): Iterable<HexPattern> = OPS

    override fun getOperator(pattern: HexPattern): Operator = when (pattern) {
        ADD -> OperatorMatrixAdd(false)
        SUB -> OperatorMatrixAdd(true)
        MUL -> OperatorMatrixMul
        ALTMUL -> make2SameSize { i, j -> i.mul(j) }
        DIV -> OperatorMatrixDiv
        ALTDIV -> make2SameSize { i, j -> i.div(j) }
        ABS -> make1Double { it.norm2() }
        POW -> OperatorMatrixPow
        ALTPOW -> make2SameSize { i, j -> MatrixFunctions.pow(i, j) }
        FLOOR -> make1(MatrixFunctions::floor)
        CEIL -> make1(MatrixFunctions::ceil)
//        SIN -> make1(MatrixFunctions::floor) // TODO
//        COS -> make1(MatrixFunctions::floor) // TODO
//        TAN -> make1(MatrixFunctions::floor) // TODO
//        ARCSIN -> make1(MatrixFunctions::floor) // TODO
//        ARCCOS -> make1(MatrixFunctions::floor) // TODO
//        ARCTAN -> make1(MatrixFunctions::floor) // TODO
//        ARCTAN2 -> OperatorMatrixMul // TODO
//        LOG -> OperatorMatrixMul // TODO
        REV -> make1(DoubleMatrix::transpose)
        else -> throw InvalidOperatorException("$pattern is not a valid operator in Arithmetic $this.")
    }

    private fun make1Double(op: Function<DoubleMatrix, Double>): OperatorUnary = OperatorUnary(IotaMultiPredicate.all(IotaPredicate.ofType(MATRIX)))
    { i: Iota -> DoubleIota(
        op.apply(Operator.downcast(i, MATRIX).matrix)
    ) }

    private fun make1(op: UnaryOperator<DoubleMatrix>): OperatorUnary = OperatorUnary(IotaMultiPredicate.all(IotaPredicate.ofType(MATRIX)))
    { i: Iota -> MatrixIota(
        op.apply(Operator.downcast(i, MATRIX).matrix)
    ) }

    private fun make2SameSize(op: BinaryOperator<DoubleMatrix>): OperatorBinary = OperatorBinary(IotaMultiPredicate.all(IotaPredicate.ofType(MATRIX)))
    { i, j ->
        val mat0 = Operator.downcast(i, MATRIX).matrix
        val mat1 = Operator.downcast(j, MATRIX).matrix

        if (mat0.rows != mat1.rows || mat0.columns != mat1.columns)
            throw MishapInvalidIota.matrixWrongSize(MatrixIota(mat1), 0, mat0.rows, mat1.columns)

        MatrixIota(
            op.apply(mat0, mat1)
        )
    }
}