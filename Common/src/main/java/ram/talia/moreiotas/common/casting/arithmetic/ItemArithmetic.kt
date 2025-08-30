package ram.talia.moreiotas.common.casting.arithmetic

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic.ABS
import at.petrak.hexcasting.api.casting.arithmetic.engine.InvalidOperatorException
import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorUnary
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate.ofType
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import net.minecraft.world.item.ItemStack
import ram.talia.moreiotas.common.casting.arithmetic.operator.type.OperatorExtractItem
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes.ITEM_STACK
import java.util.function.Function

object ItemArithmetic : Arithmetic {
    @JvmField
    val EXTRACT_ITEM: HexPattern = HexPattern.fromAngles("qaqqaea", HexDir.EAST)

    private val OPS = listOf(
        ABS,
        EXTRACT_ITEM
    )

    override fun arithName() = "item_ops"

    override fun opTypes(): Iterable<HexPattern> = OPS

    override fun getOperator(pattern: HexPattern): Operator = when (pattern) {
        ABS -> make1Double { if (it.isEmpty) 0.0 else it.count.toDouble() }
        EXTRACT_ITEM -> OperatorExtractItem
        else -> throw InvalidOperatorException("$pattern is not a valid operator in Arithmetic $this.")
    }

    private fun make1Double(op: Function<ItemStack, Double>): OperatorUnary = OperatorUnary(IotaMultiPredicate.all(ofType(ITEM_STACK)))
    { i: Iota -> DoubleIota(
            op.apply(Operator.downcast(i, ITEM_STACK).itemStack)
    ) }
}