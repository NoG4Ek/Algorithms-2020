package lesson8

import org.junit.jupiter.api.Tag
import kotlin.test.Test

class HeuristicsTestsKotlin : AbstractHeuristicsTests() {

    @Test
    @Tag("12")
    fun testFillKnapsackCompareWithGreedyTest() {
        fillKnapsackCompareWithGreedyTest { load, items, inOneGen -> fillKnapsackHeuristics(load, items, inOneGen) }
    }

    @Test
    @Tag("12")
    fun testFindVoyagingPathHeuristics() {
        findVoyagingPathHeuristics { findVoyagingPathHeuristics() }
    }

    @Test
    @Tag("12")
    fun testCutRodHeuristics() {
        cutRodCompareWith90 { length, storage, inOneGen -> cutRodHeuristics(length, storage, inOneGen) }
    }
}