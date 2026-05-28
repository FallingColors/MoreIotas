package ram.talia.moreiotas.common.casting.actions.matrices

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import net.minecraft.world.phys.Vec3
import ram.talia.moreiotas.api.asVec3
import ram.talia.moreiotas.api.getMatrix

class OpUnmakeMatrix(val skipBackConversion: Boolean) : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val mat = args.getMatrix(0, argc)

        val list = mutableListOf<Iota>()

        if (!skipBackConversion) {
            if (mat.numRows == 1 && mat.numCols == 1)
                return mat[0,0].asActionResult
            if ((mat.numRows == 1 && mat.numCols == 3) || mat.numRows == 3 && mat.numCols == 1)
                return mat.asVec3.asActionResult
            if (mat.numCols == 3) {
                for (i in 0 until mat.numRows) {
                    list.add(Vec3Iota(Vec3(mat[i, 0], mat[i, 1], mat[i, 2])))
                }
                return list.asActionResult
            }
            if (mat.numRows == 3) {
                for (i in 0 until mat.numCols) {
                    list.add(Vec3Iota(Vec3(mat[0, i], mat[1, i], mat[2, i])))
                }
                return list.asActionResult
            }
        }

        for (c in 0 until mat.numCols) {
            val toAdd = mutableListOf<Iota>()
            for (r in 0 until mat.numRows) {
                toAdd.add(DoubleIota(mat[r,c]))
            }
            list.add(ListIota(toAdd))
        }

        return list.asActionResult
    }
}