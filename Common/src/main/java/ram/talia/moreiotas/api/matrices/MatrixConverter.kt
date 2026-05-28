package ram.talia.moreiotas.api.matrices

import org.ejml.simple.SimpleMatrix
import org.jblas.DoubleMatrix

object MatrixConverter {
    @JvmStatic
    fun jblasToEjml(jMatrix: DoubleMatrix): SimpleMatrix {
        val eMatrix = SimpleMatrix(jMatrix.rows, jMatrix.columns)
        for (i in 0 until jMatrix.rows) {
            for (j in 0 until jMatrix.columns) {
                eMatrix.set(i, j, jMatrix.get(i, j))
            }
        }
        return eMatrix
    }

    @JvmStatic
    fun ejmlToJblas(eMatrix: SimpleMatrix): DoubleMatrix {
        val jMatrix = DoubleMatrix(eMatrix.numRows, eMatrix.numCols)
        for (i in 0 until eMatrix.numRows) {
            for (j in 0 until eMatrix.numCols) {
                jMatrix.put(i, j, eMatrix.get(i, j))
            }
        }
        return jMatrix
    }
}