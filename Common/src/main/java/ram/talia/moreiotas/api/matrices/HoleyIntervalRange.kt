package ram.talia.moreiotas.api.matrices

import org.jblas.ranges.Range

@Deprecated("Use of JBLAS is deprecated")
class HoleyIntervalRange(private val start: Int, private val end: Int, private val hole: Int) : Range {
    private var value: Int = start

    init {
        require(hole in start .. end) { "$hole must be between $start and $end." }
    }

    override fun init(lower: Int, upper: Int) {
        value = start
        if (value == hole)
            value++
        require(!(start < lower || end > upper + 1))
            { "Bounds $lower to $upper are beyond range interval $start to $end." }
    }

    override fun length() = end - start - 1

    override fun next() {
        value++
        if (value == hole)
            value++
    }

    override fun index() = if (value > hole) value - start - 1 else value - start

    override fun value(): Int = value

    override fun hasMore(): Boolean = (value < end - 1) || (hole != end && value < end)
}