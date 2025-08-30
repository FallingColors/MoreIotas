package ram.talia.moreiotas.common.casting.actions.matrices

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.asActionResult
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
            if (mat.rows == 1 && mat.columns == 1)
                return mat[0,0].asActionResult
            if ((mat.rows == 1 && mat.columns == 3) || mat.rows == 3 && mat.columns == 1)
                return mat.asVec3.asActionResult
            if (mat.columns == 3) {
                for (i in 0 until mat.rows) {
                    list.add(Vec3Iota(Vec3(mat[i, 0], mat[i, 1], mat[i, 2])))
                }
                return list.asActionResult
            }
            if (mat.rows == 3) {
                for (i in 0 until mat.columns) {
                    list.add(Vec3Iota(Vec3(mat[0, i], mat[1, i], mat[2, i])))
                }
                return list.asActionResult
            }
        }

        for (c in 0 until mat.columns) {
            val toAdd = mutableListOf<Iota>()
            for (r in 0 until mat.rows) {
                toAdd.add(DoubleIota(mat[r,c]))
            }
            list.add(ListIota(toAdd))
        }

        return list.asActionResult
    }
}