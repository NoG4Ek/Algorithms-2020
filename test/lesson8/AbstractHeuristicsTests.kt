package lesson8

import lesson6.Graph
import lesson6.Path
import lesson6.impl.GraphBuilder
import lesson7.knapsack.Fill
import lesson7.knapsack.Item
import lesson7.knapsack.fillKnapsackGreedy
import lesson7.rod.Cut
import lesson7.rod.cutRod
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class AbstractHeuristicsTests {

    fun fillKnapsackCompareWithGreedyTest(fillKnapsackHeuristics: (Int, List<Item>, Int) -> Fill) {
        for (i in 0..9) {
            val items = mutableListOf<Item>()
            val random = Random()
            for (j in 0 until 10000) {
                items += Item(1 + random.nextInt(10000), 300 + random.nextInt(600))
            }
            try {
                var fillHeuristics = fillKnapsackHeuristics(1000, items, 1000)
                println("1000 of 1000")
                println("Heuristics score = " + fillHeuristics.cost)
                var fillGreedy = fillKnapsackGreedy(1000, items)
                println("Greedy score = " + fillGreedy.cost)
                assertTrue(fillHeuristics.cost >= fillGreedy.cost)

                fillHeuristics = fillKnapsackHeuristics(1000, items, 10000)
                println("1000 of 10000")
                println("Heuristics score = " + fillHeuristics.cost)
                fillGreedy = fillKnapsackGreedy(1000, items)
                println("Greedy score = " + fillGreedy.cost)
                assertTrue(fillHeuristics.cost >= fillGreedy.cost)

                fillHeuristics = fillKnapsackHeuristics(700, items, 700)
                println("700 of 700")
                println("Heuristics score = " + fillHeuristics.cost)
                fillGreedy = fillKnapsackGreedy(700, items)
                println("Greedy score = " + fillGreedy.cost)
                assertTrue(fillHeuristics.cost >= fillGreedy.cost)

            } catch (e: StackOverflowError) {
                println("Greedy failed with Stack Overflow")
            }
        }

        val items = mutableListOf<Item>()

        val emptyI = fillKnapsackHeuristics(1000, items, 1000)
        assertEquals(Fill(0, Item(0, 0)), emptyI)

        val emptyIL = fillKnapsackHeuristics(0, items, 1000)
        assertEquals(Fill(0, Item(0, 0)), emptyIL)

        for (j in 0 until 10000) {
            items += Item(1000 + (0..10000).random(), 1 + (0..5).random())
        }
        try {
            val fillHeuristics = fillKnapsackHeuristics(1000, items, 1000)
            println("Heuristics score = " + fillHeuristics.cost)
            val fillGreedy = fillKnapsackGreedy(1000, items)
            println("Greedy score = " + fillGreedy.cost)
            assertTrue(fillHeuristics.cost >= fillGreedy.cost)
        } catch (e: StackOverflowError) {
            println("Greedy failed with Stack Overflow")
        }
    }

    fun cutRodCompareWith90(cutRodHeuristics: (Int, Map<Int, Int>, Int) -> Cut) {
        for (i in 0..9) {
            val cuts = mutableMapOf<Int, Int>()
            val random = Random()
            for (j in 0 until 10000) {
                cuts += Pair(300 + random.nextInt(600), 1 + random.nextInt(10000))
            }
            try {
                var fillHeuristics = cutRodHeuristics(1000, cuts, 1000)
                println("1000 of 1000")
                println("Heuristics score = " + fillHeuristics.cost)
                var fillBest90 = cutRod(1000) { cuts[it] ?: 0 }.cost / 100 * 90
                println("90% score = $fillBest90")
                assertTrue(fillHeuristics.cost >= fillBest90)

                fillHeuristics = cutRodHeuristics(1000, cuts, 10000)
                println("1000 of 10000")
                println("Heuristics score = " + fillHeuristics.cost)
                fillBest90 = cutRod(1000) { cuts[it] ?: 0 }.cost / 100 * 90
                println("90% score = $fillBest90")
                assertTrue(fillHeuristics.cost >= fillBest90)

                fillHeuristics = cutRodHeuristics(700, cuts, 700)
                println("700 of 700")
                println("Heuristics score = " + fillHeuristics.cost)
                fillBest90 = cutRod(700) { cuts[it] ?: 0 }.cost / 100 * 90
                println("90% score = $fillBest90")
                assertTrue(fillHeuristics.cost >= fillBest90)

                fillHeuristics = cutRodHeuristics(500, cuts, 700)
                println("500 of 700")
                println("Heuristics score = " + fillHeuristics.cost)
                fillBest90 = cutRod(500) { cuts[it] ?: 0 }.cost / 100 * 90
                println("90% score = $fillBest90")
                assertTrue(fillHeuristics.cost >= fillBest90)

            } catch (e: StackOverflowError) {
                println("Best failed with Stack Overflow")
            }
        }

        var cuts = mapOf<Int, Int>()

        val emptyI = cutRodHeuristics(1000, cuts, 1000)
        assertEquals(Cut(0, 0), emptyI)

        val emptyIL = cutRodHeuristics(0, cuts, 1000)
        assertEquals(Cut(0, 0), emptyIL)

        cuts = mapOf(1 to 100, 2 to 400, 3 to 200, 4 to 2, 5 to 10)
        try {
            val fillHeuristics = cutRodHeuristics(1000, cuts, 1000)
            println("Heuristics score = " + fillHeuristics.cost)
            val fillBest90 = cutRod(1000) { cuts[it] ?: 0 }.cost / 100 * 90
            println("90% score = $fillBest90")
            assertTrue(fillHeuristics.cost >= fillBest90)
        } catch (e: StackOverflowError) {
            println("Greedy failed with Stack Overflow")
        }
    }

    fun findVoyagingPathHeuristics(findVoyagingPathHeuristics: Graph.() -> Path) {
        val graph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            addConnection(a, b, 10)
            addConnection(b, c, 15)
            addConnection(c, f, 30)
            addConnection(a, d, 20)
            addConnection(d, e, 25)
            addConnection(e, f, 15)
            addConnection(a, f, 40)
            addConnection(b, d, 10)
            addConnection(c, e, 5)
        }.build()
        val path = graph.findVoyagingPathHeuristics()
        assertEquals(105, path.length)
        val vertices = path.vertices
        assertEquals(vertices.first(), vertices.last(), "Voyaging path $vertices must be loop!")
        val withoutLast = vertices.dropLast(1)
        val expected = listOf(graph["A"], graph["D"], graph["B"], graph["C"], graph["E"], graph["F"])
        assertEquals(expected.size, withoutLast.size, "Voyaging path $vertices must travel through all vertices!")
        expected.forEach {
            assertTrue(it in vertices, "Voyaging path $vertices must travel through all vertices!")
        }
    }

}