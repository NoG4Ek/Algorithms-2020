package lesson8

import lesson7.knapsack.Item
import java.util.*

//data class Chroma(var genes: List<Int>, var rating: Int, var weight: Int, var cost: Int)
class Chroma(val genes: List<Int>) {
    var rating: Int = 0
    var weight: Int = 0
    var cost: Int = 0

    constructor(minItemW: Int, load: Int, items: List<Item>) : this(
        mutableListOf<Int>().apply {
            var j = (items.indices).random()
            var w = 0
            while (load - w >= minItemW) {
                if (load - w >= items[j].weight) {
                    this.add(j)
                    w += items[j].weight
                }
                j = (items.indices).random()
            }
        }
    )

    fun calcAttributes(load: Int, items: List<Item>) {
        for (i in genes) {
            weight += items[i].weight
        }
        for (i in genes) {
            cost += items[i].cost
        }

        var permCost = 0
        var permLoad = 0

        for (i in genes) {
            if (permLoad < load) {
                permCost += items[i].cost
                permLoad += items[i].weight
            }
        }

        rating = permCost - weight
    }

    fun cross(other: Chroma, load: Int, items: List<Item>): Chroma {
        val chNew = mutableListOf<Int>()
        var w = 0

        for (j in genes) {
            for (k in other.genes) {
                if (j == k) {
                    chNew.add(j)
                    w += items[j].weight
                }
            }
        }

        for (j in genes) {
            if (load - w >= items[j].weight) {
                if ((0..1).random() == 1) {
                    chNew.add(j)
                    w += items[j].weight
                }
            }
        }

        for (j in other.genes) {
            if (load - w >= items[j].weight) {
                if ((0..1).random() == 1) {
                    chNew.add(j)
                    w += items[j].weight
                }
            }
        }

        val crChroma = Chroma(chNew)
        crChroma.calcAttributes(load, items)

        return crChroma
    }
}