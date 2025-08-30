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
import at.petrak.hexcasting.api.casting.math.HexPattern
import ram.talia.moreiotas.api.casting.iota.StringIota
import ram.talia.moreiotas.common.casting.arithmetic.operator.string.*
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes.STRING
import java.util.function.BinaryOperator
import java.util.function.Function
import java.util.function.UnaryOperator

object StringArithmetic : Arithmetic {
    private val OPS = listOf(
        ADD,
        MUL,
        ABS,
        INDEX,
        SLICE,
        REV,
        INDEX_OF,
        REMOVE,
        REPLACE,
        UNIQUE
    )

    override fun arithName(): String = "string_ops"

    override fun opTypes(): Iterable<HexPattern> = OPS

    override fun getOperator(pattern: HexPattern): Operator = when (pattern) {
        ADD -> make2 { a, b -> a + b }
        MUL -> OperatorStringMul
        ABS -> make1Double { it.length.toDouble() }
        INDEX -> OperatorStringIndex
        SLICE -> OperatorStringSlice
        REV -> make1 { it.reversed() }
        INDEX_OF -> OperatorStringIndexOf
        REMOVE -> OperatorStringRemove
        REPLACE -> OperatorStringReplace
        UNIQUE -> make1 { str -> str.fold(StringBuilder()) { b, c -> if (!b.contains(c)) b.append(c); b }.toString() }
        else -> throw InvalidOperatorException("$pattern is not a valid operator in Arithmetic $this.")
    }

    private fun make1Double(op: Function<String, Double>): OperatorUnary = OperatorUnary(IotaMultiPredicate.all(IotaPredicate.ofType(STRING)))
    { i: Iota -> DoubleIota(
            op.apply(Operator.downcast(i, STRING).string)
    ) }

    private fun make1(op: UnaryOperator<String>): OperatorUnary = OperatorUnary(IotaMultiPredicate.all(IotaPredicate.ofType(STRING)))
    { i: Iota ->
        StringIota.make(
            op.apply(Operator.downcast(i, STRING).string)
        )
    }

    private fun make2(op: BinaryOperator<String>): OperatorBinary = OperatorBinary(IotaMultiPredicate.all(IotaPredicate.ofType(STRING)))
    { i: Iota, j: Iota ->
        StringIota.make(
            op.apply(Operator.downcast(i, STRING).string, Operator.downcast(j, STRING).string)
        )
    }
}