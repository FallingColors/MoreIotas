package ram.talia.moreiotas.common.casting.arithmetic.operator.string

import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes.DOUBLE
import ram.talia.moreiotas.api.asActionResult
import ram.talia.moreiotas.api.casting.iota.StringIota
import ram.talia.moreiotas.common.casting.arithmetic.operator.nextString
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes.STRING
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.roundToInt

object OperatorStringReplace : OperatorBasic(3,
        IotaMultiPredicate.triple(IotaPredicate.ofType(STRING),
        IotaPredicate.or(IotaPredicate.ofType(DOUBLE), IotaPredicate.ofType(STRING)),
        IotaPredicate.ofType(STRING))) {
    override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
        val it = iotas.iterator().withIndex()
        val replaceIn = it.nextString(arity)
        val (_, x) = it.next()
        val replaceWith = it.nextString()

        return if (x is StringIota) {
            val toReplace = x.string
            replaceIn.replaceFirst(toReplace, replaceWith)
        } else {
            val double = (x as DoubleIota).double
            val rounded = double.roundToInt()
            if (abs(double - rounded) > DoubleIota.TOLERANCE || rounded !in 0..replaceIn.length) {
                throw MishapInvalidIota.of(x, 0, "int.positive.less.equal", replaceIn.length)
            }

            val replWithLength = min(replaceIn.length - rounded, replaceWith.length)
            replaceIn.substring(0, rounded) + replaceWith.substring(0, replWithLength) + replaceIn.substring(rounded + replWithLength)
        }.asActionResult
    }
}