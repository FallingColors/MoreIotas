package ram.talia.moreiotas.api.matrices

import org.ejml.simple.SimpleMatrix
import org.jblas.DoubleMatrix

object MatrixConverter {
    @JvmStatic
    fun jblasToEjml(jMatrix: DoubleMatrix): SimpleMatrix {
        var eMatrix = SimpleMatrix.filled(jMatrix.rows, jMatrix.columns, 1.0)
        eMatrix = eMatrix.elementOp { row, col, value: Double -> jMatrix.get(row, col) }
        return eMatrix
    }

    @JvmStatic
    fun ejmlToJblas(eMatrix: SimpleMatrix): DoubleMatrix {
        val jMatrix = DoubleMatrix(eMatrix.numRows, eMatrix.numCols)
        eMatrix.elementOp { row, col, value: Double ->
            jMatrix.put(row, col, value)
            value // the lambda needs to return a "new" value for each element
        }
        return jMatrix
    }
}