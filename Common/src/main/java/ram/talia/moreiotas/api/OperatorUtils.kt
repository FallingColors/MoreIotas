package ram.talia.moreiotas.api

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.iota.*
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import com.mojang.datafixers.util.Either
import net.minecraft.core.BlockPos
import net.minecraft.core.Position
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.phys.Vec3
import org.jblas.DoubleMatrix
import ram.talia.moreiotas.api.casting.iota.*
import ram.talia.moreiotas.api.util.Anyone
import ram.talia.moreiotas.api.util.ChatEntry

operator fun Double.times(vec: Vec3): Vec3 = vec.scale(this)
operator fun Vec3.times(d: Double): Vec3 = this.scale(d)
operator fun Vec3.div(d: Double): Vec3 = this.scale(1/d)
operator fun Vec3.plus(vec3: Vec3): Vec3 = this.add(vec3)
operator fun Vec3.minus(vec3: Vec3): Vec3 = this.subtract(vec3)
operator fun Vec3.unaryMinus(): Vec3 = this.scale(-1.0)

operator fun Position.component1(): Double = this.x()
operator fun Position.component2(): Double = this.y()
operator fun Position.component3(): Double = this.z()


operator fun Double.times(mat: DoubleMatrix): DoubleMatrix = mat.mul(this)
operator fun DoubleMatrix.times(d: Double): DoubleMatrix = this.mul(d)

operator fun Vec3.times(mat: DoubleMatrix): DoubleMatrix = (this.asMatrix).mmul(mat)
operator fun DoubleMatrix.times(vec: Vec3): DoubleMatrix = this.mmul(vec.asMatrix)
operator fun DoubleMatrix.times(mat: DoubleMatrix): DoubleMatrix = this.mmul(mat)
operator fun DoubleMatrix.plus(mat: DoubleMatrix): DoubleMatrix = this.add(mat)
operator fun DoubleMatrix.minus(mat: DoubleMatrix): DoubleMatrix = this.sub(mat)
operator fun DoubleMatrix.unaryMinus(): DoubleMatrix = this.mul(-1.0)

fun List<Iota>.getBoolOrNull(idx: Int, argc: Int = 0): Boolean? {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    return when (x) {
        is BooleanIota -> x.bool
        is NullIota -> null
        else -> throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "booleannull")
    }
}

fun List<Iota>.getString(idx: Int, argc: Int = 0): String {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    if (x is StringIota) {
        return x.string
    } else {
        throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "string")
    }
}

fun List<Iota>.getStringOrNull(idx: Int, argc: Int = 0): String? {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    return when (x) {
        is StringIota -> x.string
        is NullIota -> null
        else -> throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "string")
    }
}

fun List<Iota>.getStringOrList(idx: Int, argc: Int = 0): Either<String, List<String>> {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    return when (x) {
        is StringIota -> Either.left(x.string)
        is ListIota -> {
            val list = mutableListOf<String>()

            for (iota in x.list) {
                if (iota !is StringIota)
                    throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "stringstringlist")
                list.add(iota.string)
            }

            Either.right(list)
        }
        else -> throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "stringstringlist")
    }
}

fun List<Iota>.getMatrix(idx: Int, argc: Int = 0): DoubleMatrix {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    if (x is MatrixIota) {
        return x.matrix
    } else {
        throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "matrix")
    }
}

fun List<Iota>.getNumOrVecOrMatrix(idx: Int, argc: Int = 0): Anyone<Double, Vec3, DoubleMatrix> {
    val datum = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    return when (datum) {
        is DoubleIota -> Anyone.first(datum.double)
        is Vec3Iota -> Anyone.second(datum.vec3)
        is MatrixIota -> Anyone.third(datum.matrix)
        else -> throw MishapInvalidIota.of(
                datum,
                if (argc == 0) idx else argc - (idx + 1),
                "numvecmat"
        )
    }
}

fun List<Iota>.getEntityType(idx: Int, argc: Int = 0): EntityType<*> {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    if (x is EntityTypeIota)
        return x.entityType
    if (x is EntityIota)
        return x.entity.type
    throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "type.entity")
}

fun List<Iota>.getItemStack(index: Int, argc: Int = 0): ItemStack {
    val x = this.getOrElse(index) { throw MishapNotEnoughArgs(index + 1, this.size) }

    return (x as? ItemStackIota)?.itemStack
        ?: throw MishapInvalidIota.of(x, if (argc == 0) index else argc - (index + 1), "item_stack")
}

inline val String.asActionResult get() = listOf(StringIota.make(this))
inline val DoubleMatrix.asActionResult get() = listOf(MatrixIota(this))
inline val IotaType<*>.asActionResult get() = listOf(IotaTypeIota(this))
inline val Block.asActionResult get() = listOf(ItemTypeIota(this))
inline val EntityType<*>.asActionResult get() = listOf(EntityTypeIota(this))
inline val Item.asActionResult get() = listOf(ItemTypeIota(this))
inline val List<Item>.asActionResult get() = this.map { ItemTypeIota(it) }.asActionResult
inline val ItemStack.asActionResult get() = listOf(ItemStackIota.createFiltered(this))

inline val Vec3.asMatrix get() = DoubleMatrix(3, 1, this.x, this.y, this.z)
inline val BlockPos.asMatrix get() = DoubleMatrix(1, 3, this.x.toDouble(), this.y.toDouble(), this.z.toDouble())
inline val List<Vec3>.asMatrix get(): DoubleMatrix {
    val matrix = DoubleMatrix(this.size, 3)
    this.forEachIndexed { i, vec ->
        matrix.put(i, 0, vec.x)
        matrix.put(i, 1, vec.y)
        matrix.put(i, 2, vec.z)
    }
    return matrix
}

inline val Anyone<Double, Vec3, DoubleMatrix>.asMatrix get() = this.flatMap(
        {d -> DoubleMatrix(1,1,d)},
        {v -> v.asMatrix},
        {mat -> mat})

inline val DoubleMatrix.asVec3 get() = Vec3(this[0], this[1], this[2])

fun MishapInvalidIota.Companion.matrixWrongSize(perpetrator: Iota,
                                                reverseIdx: Int,
                                                expectedRows: Int?,
                                                expectedColumns: Int?): MishapInvalidIota {
    if (expectedRows == null && expectedColumns == null)
        throw Exception("Need at least one of expectedRows and expectedColumns non-null.")

    return if (expectedRows == null)
        of(perpetrator, reverseIdx, "matrix.wrong_size", "n", expectedColumns!!)
    else if (expectedColumns == null)
        of(perpetrator, reverseIdx, "matrix.wrong_size", expectedRows, "n")
    else
        of(perpetrator, reverseIdx, "matrix.wrong_size", expectedRows, expectedColumns)
}