package ram.talia.moreiotas.common.casting.actions.matrices

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import ram.talia.moreiotas.api.*

object OpMulMatrix : ConstMediaAction {
    override val argc = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val arg0 = args.getNumOrVecOrMatrix(0, OpAddMatrix.argc)
        val arg1 = args.getNumOrVecOrMatrix(1, OpAddMatrix.argc)

        // Nine possible cases, since the first iota can be double, vec, or matrix, and so can the second iota.
        // The outer flatMap distinguishes between possible values of arg0, while each of the inner distinguish
        // between possible values of arg1. If either is a double it's simple elementwise multiplication, but if both
        // are non-double then it checks that the number of columns of the first arg is equal to the number of rows of
        // the second. If they are equal, it does matrix multiplication in the order arg0 on the left, arg1 on the right.
        return arg0.flatMap({d0 ->
            arg1.flatMap({d1 -> (d0*d1).asActionResult}, {vec1 -> (d0*vec1).asActionResult}, {mat1 -> (d0*mat1).asActionResult})
        }, {vec0 ->
            arg1.flatMap({d1 -> (vec0*d1).asActionResult},
                { throw MishapInvalidIota.matrixWrongSize(args[1], 0, 1, null) },
                { mat1 ->
                    if (mat1.rows != 1) throw MishapInvalidIota.matrixWrongSize(args[1], 0, 1, null)
                    (vec0*mat1).asActionResult
                })
        }, {mat0 ->
            arg1.flatMap({d1 -> (mat0*d1).asActionResult},
                { vec1 ->
                    if (mat0.columns != 3) throw MishapInvalidIota.matrixWrongSize(args[0], 1, null, 3)
                    val out = mat0*vec1
                    if (out.columns == 1 && out.rows == 3)
                        out.asVec3.asActionResult
                    else out.asActionResult
                },
                { mat1 ->
                    if (mat0.columns != mat1.rows)
                        throw MishapInvalidIota.matrixWrongSize(args[1], 0, mat0.columns, null)
                    (mat0 * mat1).asActionResult
                })
        })
    }
}