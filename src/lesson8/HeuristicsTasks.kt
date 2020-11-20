@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson8

import lesson6.Graph
import lesson6.Path
import lesson7.knapsack.Fill
import lesson7.knapsack.Item
import lesson7.knapsack.fillKnapsackGreedy

// Примечание: в этом уроке достаточно решить одну задачу

/**
 * Решить задачу о ранце (см. урок 6) любым эвристическим методом
 *
 * Очень сложная
 *
 * load - общая вместимость ранца, items - список предметов
 *
 * Используйте parameters для передачи дополнительных параметров алгоритма
 * (не забудьте изменить тесты так, чтобы они передавали эти параметры)
 */
fun fillKnapsackHeuristics(load: Int, items: List<Item>, vararg parameters: Any): Fill {
    var fillHeuristic = Fill(0, Item(0, 0))
    val fillGreedy = fillKnapsackGreedy(load, items)

    val inOneGeneration = parameters[0].toString().toInt()
    var anStartPop = genGeneration(inOneGeneration, load, items)

    while (fillHeuristic.cost < fillGreedy.cost) {
        val selPop = selection(anStartPop)
        val crPop = cross(selPop, load, items)
        anStartPop = if ((0..9).random() == 1) { mutation(inOneGeneration, crPop, load, items) } else { crPop }
        fillHeuristic = findTBestFill(anStartPop, load, items)
    }

    return fillHeuristic
}

fun minItemW(items: List<Item>): Int {
    var min = Int.MAX_VALUE

    for (i in items) {
        if (min > i.weight) {
            min = i.weight
        }
    }

    return min
}

fun genGeneration(inOneGeneration: Int, load: Int, items: List<Item>): List<Chroma> {
    val generation = mutableListOf<Chroma>()
    val minItemW = minItemW(items)

    for (i in 0 until inOneGeneration) {
        val chroma = Chroma(minItemW, load, items)
        chroma.calcAttributes(load, items)
        generation.add(chroma)
    }

    return generation
}

fun selection(population: List<Chroma>): List<Chroma> {
    val selPop = population.toMutableList().sortedBy { it.rating }.toMutableList()

    for (i in 0 until population.size / 2) {
        selPop.removeAt(0)
    }

    return selPop
}

fun cross(population: List<Chroma>, load: Int, items: List<Item>): List<Chroma> {
    val crossPop = population.toMutableList()

    for (i in population.indices) {
        val par2 = population[(population.indices).random()]
        val par1 = population[(population.indices).random()]
        val chNew = par1.cross(par2, load, items)

        crossPop.add(chNew)
    }

    return crossPop
}

fun mutation(inOneGeneration: Int, population: List<Chroma>, load: Int, items: List<Item>): List<Chroma> {
    val mutPop = population.toMutableList()
    val population20 = genGeneration(inOneGeneration / 100 * 20, load, items)

    var i = 0
    while (i != inOneGeneration / 100 * 20) {
        mutPop.removeAt((0 until inOneGeneration - i).random())
        i++
    }

    mutPop.addAll(population20)

    return mutPop
}

fun findTBestFill(population: List<Chroma>, load: Int, items: List<Item>): Fill {
    var indOfB = 0
    for (ch in population.indices) {
        if (population[ch].rating > population[indOfB].rating && population[ch].weight <= load) {
            indOfB = ch
        }
    }

    val bestItems = mutableSetOf<Item>()
    for (ch in population[indOfB].genes) {
        bestItems.add(items[ch])
    }

    return Fill(population[indOfB].cost, bestItems)
}

/**
 * Решить задачу коммивояжёра (см. урок 5) методом колонии муравьёв
 * или любым другим эвристическим методом, кроме генетического и имитации отжига
 * (этими двумя методами задача уже решена в под-пакетах annealing & genetic).
 *
 * Очень сложная
 *
 * Граф передаётся через получатель метода
 *
 * Используйте parameters для передачи дополнительных параметров алгоритма
 * (не забудьте изменить тесты так, чтобы они передавали эти параметры)
 */
fun Graph.findVoyagingPathHeuristics(vararg parameters: Any): Path {
    TODO()
}

