package ram.talia.moreiotas.common.casting.arithmetic.operator.string

import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.common.casting.arithmetic.operator.nextPositiveIntUnderInclusive
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes.DOUBLE
import ram.talia.moreiotas.api.asActionResult
import ram.talia.moreiotas.common.casting.arithmetic.operator.nextString
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes.STRING
import kotlin.math.max
import kotlin.math.min

object OperatorStringSlice : OperatorBasic(3, IotaMultiPredicate.triple(IotaPredicate.ofType(STRING), IotaPredicate.ofType(DOUBLE), IotaPredicate.ofType(DOUBLE))) {
    override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
        val it = iotas.iterator().withIndex()
        val string = it.nextString(arity)
        val index0 = it.nextPositiveIntUnderInclusive(string.length, arity)
        val index1 = it.nextPositiveIntUnderInclusive(string.length, arity)

        if (index0 == index1)
            return "".asActionResult
        return string.substring(min(index0, index1), max(index0, index1)).asActionResult
    }
}