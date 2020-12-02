package lesson8.chromes

import java.lang.Integer.min
import java.lang.Math.max

class ChromeRod(val genes: Map<Int, Int>) {
    var cost: Int = 0

    constructor(minLength: Int, length: Int, storage: Map<Int, Int>) : this(
        mutableMapOf<Int, Int>().apply {
            for (i in storage)
                this[i.key] = 0
            var l = 0
            var j = (this.keys).random()
            while (length - l >= minLength) {
                if (length - l >= j) {
                    this[j]?.plus(1)?.let { this.put(j, it) }
                    l += j
                }
                j = (this.keys).random()
            }
        }
    )

    fun calcAttributes(storage: Map<Int, Int>) {
        for (i in genes) {
            cost += storage[i.key]!! * i.value
        }
    }

    fun cross(other: ChromeRod, length: Int, storage: Map<Int, Int>): ChromeRod {
        val chNew = mutableMapOf<Int, Int>()
        var l = 0

        for (i in storage) // storage - 1
            chNew[i.key] = 0

        for (j in genes) { // storage - 1
            if (other.genes[j.key] != 0 && j.value != 0) {
                chNew[j.key] = other.genes[j.key]?.let { kotlin.math.min(j.value, it) }!!
                l += j.key * chNew[j.key]!!
            }
        }
        for (j in genes) { // storage - 1

            if (other.genes[j.key] != 0) {
                if (l + j.key * other.genes[j.key]!! <= length) {
                    if ((0..1).random() == 1) {
                        chNew[j.key] = chNew[j.key]!! + other.genes[j.key]!!
                        l += j.key * other.genes[j.key]!!
                    }
                }
            }

            if (j.value != 0) {
                if (l + j.key * j.value <= length) {
                    if ((0..1).random() == 1) {
                        chNew[j.key] = chNew[j.key]!! + j.value
                        l += j.key * j.value
                    }
                }
            }

        }

        val crChroma = ChromeRod(chNew)
        crChroma.calcAttributes(storage)

        return crChroma
    }
}