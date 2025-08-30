package ram.talia.moreiotas.common.casting.arithmetic.operator

import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import net.minecraft.world.phys.Vec3
import org.jblas.DoubleMatrix
import ram.talia.moreiotas.api.casting.iota.MatrixIota
import ram.talia.moreiotas.api.casting.iota.StringIota
import ram.talia.moreiotas.api.util.Anyone

fun Iterator<IndexedValue<Iota>>.nextString(argc: Int = 0): String {
    val (idx, x) = this.next()
    if (x is StringIota)
        return x.string
    throw MishapInvalidIota.of(x, if (argc == 0) idx else argc - (idx + 1), "string")
}

fun Iterator<IndexedValue<Iota>>.nextNumOrVecOrMatrix(argc: Int = 0): Anyone<Double, Vec3, DoubleMatrix> {
    val (idx, x) = this.next()
    return when (x) {
        is DoubleIota -> Anyone.first(x.double)
        is Vec3Iota -> Anyone.second(x.vec3)
        is MatrixIota -> Anyone.third(x.matrix)
        else -> throw MishapInvalidIota.of(
            x,
            if (argc == 0) idx else argc - (idx + 1),
            "numvecmat"
        )
    }
}