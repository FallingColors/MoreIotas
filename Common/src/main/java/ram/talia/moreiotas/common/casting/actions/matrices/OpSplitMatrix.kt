package ram.talia.moreiotas.common.casting.actions.matrices

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPositiveIntUnderInclusive
import at.petrak.hexcasting.api.casting.iota.Iota
import org.jblas.ranges.IntervalRange
import ram.talia.moreiotas.api.asMatrix
import ram.talia.moreiotas.api.getNumOrVecOrMatrix
import ram.talia.moreiotas.api.casting.iota.MatrixIota

class OpSplitMatrix(private val splitVertically: Boolean) : ConstMediaAction {
    override val argc = 2
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val mat = args.getNumOrVecOrMatrix(0, argc).asMatrix
        val split = args.getPositiveIntUnderInclusive(1, if (splitVertically) mat.rows else mat.columns, argc)

        val int0 = IntervalRange(0, split)
        val int1 = IntervalRange(split, if (splitVertically) mat.rows else mat.columns)
        val out0 = if (splitVertically) mat.getRows(int0) else mat.getColumns(int0)
        val out1 = if (splitVertically) mat.getRows(int1) else mat.getColumns(int1)

        return listOf(MatrixIota(out0), MatrixIota(out1))
    }
}