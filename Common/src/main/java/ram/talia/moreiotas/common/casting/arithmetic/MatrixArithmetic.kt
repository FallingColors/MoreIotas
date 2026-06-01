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
import org.ejml.simple.SimpleMatrix
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
import kotlin.math.ceil
import kotlin.math.floor

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
        ALTMUL -> make2SameSize { mat1, mat2 -> mat1.elementMult(mat2) }
        DIV -> OperatorMatrixDiv
        ALTDIV -> make2SameSize { mat1, mat2 -> mat1.elementDiv(mat2) }
        ABS -> make1Double { it.normF() }
        POW -> OperatorMatrixPow
        ALTPOW -> make2SameSize { mat1, mat2 -> mat1.elementPower(mat2) }
        FLOOR -> make1 {mat -> mat.elementOp { i, j, d: Double -> floor(d) }}
        CEIL -> make1 {mat -> mat.elementOp { i, j, d: Double -> ceil(d) }}
//        SIN -> make1(MatrixFunctions::floor) // TODO
//        COS -> make1(MatrixFunctions::floor) // TODO
//        TAN -> make1(MatrixFunctions::floor) // TODO
//        ARCSIN -> make1(MatrixFunctions::floor) // TODO
//        ARCCOS -> make1(MatrixFunctions::floor) // TODO
//        ARCTAN -> make1(MatrixFunctions::floor) // TODO
//        ARCTAN2 -> OperatorMatrixMul // TODO
//        LOG -> OperatorMatrixMul // TODO
        REV -> make1(SimpleMatrix::transpose)
        else -> throw InvalidOperatorException("$pattern is not a valid operator in Arithmetic $this.")
    }


    private fun make1Double(op: Function<SimpleMatrix, Double>): OperatorUnary = OperatorUnary(IotaMultiPredicate.all(IotaPredicate.ofType(MATRIX)))
    { i: Iota -> DoubleIota(
        op.apply(Operator.downcast(i, MATRIX).simpleMatrix)
    ) }

    private fun make1(op: UnaryOperator<SimpleMatrix>): OperatorUnary = OperatorUnary(IotaMultiPredicate.all(IotaPredicate.ofType(MATRIX)))
    { i: Iota -> MatrixIota(
        op.apply(Operator.downcast(i, MATRIX).simpleMatrix)
    ) }

    private fun make2SameSize(op: BinaryOperator<SimpleMatrix>): OperatorBinary = OperatorBinary(IotaMultiPredicate.all(IotaPredicate.ofType(MATRIX)))
    { i, j ->
        val mat0 = Operator.downcast(i, MATRIX).simpleMatrix
        val mat1 = Operator.downcast(j, MATRIX).simpleMatrix

        if (mat0.numRows != mat1.numRows || mat0.numCols != mat1.numCols)
            throw MishapInvalidIota.matrixWrongSize(MatrixIota(mat1), 0, mat0.numRows, mat1.numCols)

        MatrixIota(
            op.apply(mat0, mat1)
        )
    }
}