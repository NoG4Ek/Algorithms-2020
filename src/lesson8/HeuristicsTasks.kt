@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson8

import lesson6.Graph
import lesson6.Path
import lesson7.knapsack.Fill
import lesson7.knapsack.Item
import lesson7.knapsack.fillKnapsackGreedy
import lesson7.rod.Cut
import lesson7.rod.cutRod
import lesson8.chromes.ChromeKnapsack
import lesson8.chromes.ChromeRod
import kotlin.math.min

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
// inOneGeneration = iog = parameters[0]
// (iog - 1) * (load / minItemW) ++ iog / 2 ++ (iog - 1) * (load / minItemW) ^ 2 ++ 20%iog * ((load / minItemW) + 1) ++
// ++ (iog - 1) + (load / minItemW) ~~ O(iog * (load/minItemW)^2)
// O(iog * (load/minItemW)^2), O(iog)
fun fillKnapsackHeuristics(load: Int, items: List<Item>, vararg parameters: Any): Fill {
    var fillHeuristic = Fill(0, Item(0, 0))
    if (load == 0 || items.isEmpty()) {
        return fillHeuristic
    }
    val fillGreedy = fillKnapsackGreedy(load, items)

    val inOneGeneration = parameters[0].toString().toInt()
    var anStartPop = genGeneration(inOneGeneration, load, items)

    while (fillHeuristic.cost < fillGreedy.cost) {
        val selPop = selection(anStartPop)
        val crPop = cross(selPop, load, items)
        anStartPop = if ((0..9).random() == 1) {
            mutation(inOneGeneration, crPop, load, items)
        } else {
            crPop
        }
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

// (iog - 1) * (load / minItemW)
fun genGeneration(inOneGeneration: Int, load: Int, items: List<Item>): List<ChromeKnapsack> {
    val generation = mutableListOf<ChromeKnapsack>()
    val minItemW = minItemW(items)

    for (i in 0 until inOneGeneration) { // iog - 1
        val chroma = ChromeKnapsack(minItemW, load, items) // load / minItemW - худший
        chroma.calcAttributes(load, items)
        generation.add(chroma)
    }

    return generation
}

// iog / 2
fun selection(population: List<ChromeKnapsack>): List<ChromeKnapsack> {
    val selPop = population.toMutableList().sortedBy { it.rating }.toMutableList()

    for (i in 0 until population.size / 2) { // iog / 2
        selPop.removeAt(0)
    }

    return selPop
}

// (iog - 1) * (load / minItemW) ^ 2
fun cross(population: List<ChromeKnapsack>, load: Int, items: List<Item>): List<ChromeKnapsack> {
    val crossPop = population.toMutableList()

    for (i in population.indices) { // iog - 1
        val par2 = population[(population.indices).random()]
        val par1 = population[(population.indices).random()]
        val chNew = par1.cross(par2, load, items) // (load / minItemW) ^ 2

        crossPop.add(chNew)
    }

    return crossPop
}

// 20%iog * ((load / minItemW) + 1)
fun mutation(
    inOneGeneration: Int,
    population: List<ChromeKnapsack>,
    load: Int,
    items: List<Item>
): List<ChromeKnapsack> {
    val mutPop = population.toMutableList()
    val population20 = genGeneration(inOneGeneration / 100 * 20, load, items) // 20%iog * (load / minItemW)

    var i = 0
    while (i != inOneGeneration / 100 * 20) { // 20%iog
        mutPop.removeAt((0 until inOneGeneration - i).random())
        i++
    }

    mutPop.addAll(population20)

    return mutPop

}

// (iog - 1) + (load / minItemW)
fun findTBestFill(population: List<ChromeKnapsack>, load: Int, items: List<Item>): Fill {
    var indOfB = 0
    for (ch in population.indices) { // iog - 1
        if (population[ch].rating > population[indOfB].rating && population[ch].weight <= load) {
            indOfB = ch
        }
    }

    val bestItems = mutableSetOf<Item>()
    for (ch in population[indOfB].genes) { // (load / minItemW)
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


// inOneGeneration = iog = parameters[0]
// (iog - 1) * 3*storage
// O(iog * storage), O(iog)
fun cutRodHeuristics(length: Int, storage: Map<Int, Int>, vararg parameters: Any): Cut {

    var cutHeuristic = Cut(0, 0)
    if (length == 0 || storage.isEmpty()) {
        return cutHeuristic
    }

    val bestCut90 = cutRod(length) { storage[it] ?: 0 }.cost / 100 * 90

    val same = tryTheSame(length, storage)

    if (bestCut90 <= same.cost) {
        return same
    }

    val inOneGeneration = parameters[0].toString().toInt()
    val minLength = minLength(storage)
    var anStartPop = genGenerationCut(inOneGeneration, minLength, length, storage)

    while (cutHeuristic.cost < bestCut90) {
        val selPop = selectionCut(anStartPop)
        val crPop = crossCut(selPop, length, storage)
        anStartPop = if ((0..9).random() == 1) {
            mutationCut(inOneGeneration, minLength, crPop, length, storage)
        } else {
            crPop
        }
        cutHeuristic = findTBestCut(anStartPop, length, storage)
        println("" + cutHeuristic.cost + "  vs  " + bestCut90)
    }

    return cutHeuristic
}

fun tryTheSame(length: Int, storage: Map<Int, Int>): Cut {
    var keyOfB = 0
    var v = 0
    for (i in storage) {
        if (i.value / i.key > v) {
            v = i.value / i.key
            keyOfB = i.key
        }
    }

    return Cut(storage[keyOfB]!! * (length / keyOfB), List(length / keyOfB) { keyOfB })
}

fun minLength(storage: Map<Int, Int>): Int {
    var min = Int.MAX_VALUE

    for (i in storage) {
        if (min > i.key) {
            min = i.key
        }
    }

    return min
}

// (iog - 1) * ((length / minLength) + storage)
fun genGenerationCut(inOneGeneration: Int, minLength: Int, length: Int, storage: Map<Int, Int>): List<ChromeRod> {
    val generation = mutableListOf<ChromeRod>()

    for (i in 0 until inOneGeneration) {
        val chroma = ChromeRod(minLength, length, storage)
        chroma.calcAttributes(storage)
        generation.add(chroma)
    }

    return generation
}

// iog / 2
fun selectionCut(population: List<ChromeRod>): List<ChromeRod> {
    val selPop = population.toMutableList().sortedBy { it.cost }.toMutableList()

    for (i in 0 until population.size / 2) {
        selPop.removeAt(0)
    }

    return selPop
}

// (iog - 1) * 3*storage
fun crossCut(population: List<ChromeRod>, length: Int, storage: Map<Int, Int>): List<ChromeRod> {
    val crossPop = population.toMutableList()

    for (i in population.indices) { // iog - 1
        val par2 = population[(population.indices).random()]
        val par1 = population[(population.indices).random()]
        val chNew = par1.cross(par2, length, storage) // 3 * storage

        crossPop.add(chNew)
    }

    return crossPop
}

// 20%iog * ((length / minLength) + 1)
fun mutationCut(
    inOneGeneration: Int,
    minLength: Int,
    population: List<ChromeRod>,
    length: Int,
    storage: Map<Int, Int>
): List<ChromeRod> {
    val mutPop = population.toMutableList()
    val population20 = genGenerationCut(inOneGeneration / 100 * 20, minLength, length, storage)

    var i = 0
    while (i != inOneGeneration / 100 * 20) {
        mutPop.removeAt((0 until inOneGeneration - i).random())
        i++
    }

    mutPop.addAll(population20)

    return mutPop

}

fun findTBestCut(population: List<ChromeRod>, length: Int, storage: Map<Int, Int>): Cut {
    var indOfB = 0
    for (ch in population.indices) { // iog - 1
        if (population[ch].cost > population[indOfB].cost) {
            indOfB = ch
        }
    }

    val bestCuts = mutableListOf<Int>()
    for (i in population[indOfB].genes) {
        for (j in 0 until i.value) {
            bestCuts.add(i.key)
        }
    }

    return Cut(population[indOfB].cost, bestCuts)
}
