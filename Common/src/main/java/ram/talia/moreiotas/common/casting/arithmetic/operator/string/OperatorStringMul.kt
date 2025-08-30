package ram.talia.moreiotas.common.casting.arithmetic.operator.string

import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import ram.talia.moreiotas.api.asActionResult
import ram.talia.moreiotas.api.casting.iota.StringIota
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes
import kotlin.math.abs
import kotlin.math.roundToInt

object OperatorStringMul : OperatorBasic(2, IotaMultiPredicate.either(
        IotaMultiPredicate.pair(IotaPredicate.ofType(MoreIotasIotaTypes.STRING), IotaPredicate.ofType(HexIotaTypes.DOUBLE)),
        IotaMultiPredicate.pair(IotaPredicate.ofType(HexIotaTypes.DOUBLE), IotaPredicate.ofType(MoreIotasIotaTypes.STRING)))) {
    override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
        val it = iotas.iterator()
        val first = it.next()
        val second = it.next()
        val (num, str) = if (first is DoubleIota) {
            val double = first.double
            val rounded = double.roundToInt()
            if (abs(double - rounded) > DoubleIota.TOLERANCE)
                throw MishapInvalidIota.of(first, 1, "int")
            rounded to (second as StringIota).string
        } else {
            val double = (second as DoubleIota).double
            val rounded = double.roundToInt()
            if (abs(double - rounded) > DoubleIota.TOLERANCE)
                throw MishapInvalidIota.of(first, 0, "int")
            rounded to (first as StringIota).string
        }

        return str.repeat(num).asActionResult
    }
}