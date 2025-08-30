package ram.talia.moreiotas.common.casting.actions.matrices

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import org.jblas.DoubleMatrix
import ram.talia.moreiotas.api.asActionResult
import ram.talia.moreiotas.api.asMatrix
import ram.talia.moreiotas.api.mod.MoreIotasConfig

object OpMakeMatrix : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        when (val arg = args[0]) {
            is DoubleIota -> return DoubleMatrix.scalar(arg.double).asActionResult
            is Vec3Iota -> return arg.vec3.asMatrix.asActionResult
            is ListIota -> {
                val list = arg.list
                if (!list.nonEmpty)
                    return DoubleMatrix.EMPTY.asActionResult

                val numRows = when (val car = list.car) {
                    is DoubleIota -> 1
                    is Vec3Iota -> 3
                    is ListIota -> car.list.size()
                    else -> throw MishapInvalidIota.ofType(arg, 0, "possible_matrix")
                }
                val numCols = list.size()

                if (numRows > MoreIotasConfig.server.maxMatrixSize || numCols > MoreIotasConfig.server.maxMatrixSize)
                    throw MishapInvalidIota.of(arg, 0, "matrix.max_size", MoreIotasConfig.server.maxMatrixSize, numRows, numCols)

                val matrix = DoubleMatrix(numRows, numCols)

                list.forEachIndexed { col, iota ->
                    when (iota) {
                        is DoubleIota -> {
                            if (numRows != 1) throw MishapInvalidIota.ofType(arg, 0, "possible_matrix")
                            matrix.put(0, col, iota.double)
                        }
                        is Vec3Iota -> {
                            if (numRows != 3) throw MishapInvalidIota.ofType(arg, 0, "possible_matrix")
                            val vec = iota.vec3
                            matrix.put(0, col, vec.x)
                            matrix.put(1, col, vec.y)
                            matrix.put(2, col, vec.z)
                        }
                        is ListIota -> {
                            if (numRows != iota.list.size()) throw MishapInvalidIota.ofType(arg, 0, "possible_matrix")
                            iota.list.forEachIndexed { row, innerIota ->
                                if (innerIota !is DoubleIota) throw MishapInvalidIota.ofType(arg, 0, "possible_matrix")
                                matrix.put(row, col, innerIota.double)
                            }
                        }
                    }
                }

                return matrix.asActionResult
            }
            else -> throw MishapInvalidIota.ofType(arg, 0, "possible_matrix")
        }
    }
}