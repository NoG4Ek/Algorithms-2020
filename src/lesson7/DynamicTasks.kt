@file:Suppress("UNUSED_PARAMETER")

package lesson7

import kotlin.text.StringBuilder

/**
 * Наибольшая общая подпоследовательность.
 * Средняя
 *
 * Дано две строки, например "nematode knowledge" и "empty bottle".
 * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
 * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
 * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
 * Если общей подпоследовательности нет, вернуть пустую строку.
 * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
 * При сравнении подстрок, регистр символов *имеет* значение.
 */
fun longestCommonSubSequence(first: String, second: String): String { // O(n * m), O(n * m)
    val coll = mutableListOf<MutableList<Int>>()
    val prev = mutableListOf<MutableList<Pair<Int, Int>>>()

    for (i in 0..first.length) {
        coll.add(mutableListOf())
        prev.add(mutableListOf())
        for (j in 0..second.length) {
            coll[i].add(0)
            prev[i].add(Pair(-1, -1))
        }
    }

    for (i in 1..first.length)
        for (j in 1..second.length) {
            if (first[i - 1] == second[j - 1]) {
                coll[i][j] = coll[i - 1][j - 1] + 1
                prev[i][j] = Pair(i - 1, j - 1)
            } else {
                if (coll[i - 1][j] >= coll[i][j - 1]) {
                    coll[i][j] = coll[i - 1][j]
                    prev[i][j] = Pair(i - 1, j)
                } else {
                    coll[i][j] = coll[i][j - 1]
                    prev[i][j] = Pair(i, j - 1)
                }
            }
        }

    return find(first.length, second.length, StringBuilder(), first, prev)
}

fun find(i: Int, j: Int, str: StringBuilder, first: String, prev: List<MutableList<Pair<Int, Int>>>): String {
    if (i == 0 || j == 0)
        return str.toString()
    if (prev[i][j] == Pair(i - 1, j - 1)) {
        find(i - 1, j - 1, str, first, prev)
        str.append(first[i - 1])
    } else {
        if (prev[i][j] == Pair(i - 1, j)) {
            find(i - 1, j, str, first, prev)
        } else {
            find(i, j - 1, str, first, prev)
        }
    }
    return str.toString()
}

/**
 * Наибольшая возрастающая подпоследовательность
 * Сложная
 *
 * Дан список целых чисел, например, [2 8 5 9 12 6].
 * Найти в нём самую длинную возрастающую подпоследовательность.
 * Элементы подпоследовательности не обязаны идти подряд,
 * но должны быть расположены в исходном списке в том же порядке.
 * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
 * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
 * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
 */
fun longestIncreasingSubSequence(list: List<Int>): List<Int> { // O(n^2), O(n)
    if (list.isEmpty())
        return listOf()

    val d = IntArray(list.size) { 0 }
    val p = IntArray(list.size) { -1 }

    for (i in list.indices) {
        d[i] = 1;
        p[i] = -1;
        for (j in 0 until i) {
            if (list[j] < list[i])
                if (d[j] + 1 > d[i]) {
                    d[i] = 1 + d[j];
                    p[i] = j;
                }
        }
    }

    var ans = d[0]
    var pos = 0
    for (i in list.indices) {
        if (d[i] > ans) {
            ans = d[i]
            pos = i
        }
    }

    val path = mutableListOf<Int>()
    while (pos != -1) {
        path.add(list[pos])
        pos = p[pos];
    }
    return path.reversed()
}

/**
 * Самый короткий маршрут на прямоугольном поле.
 * Средняя
 *
 * В файле с именем inputName задано прямоугольное поле:
 *
 * 0 2 3 2 4 1
 * 1 5 3 4 6 2
 * 2 6 2 5 1 3
 * 1 4 3 2 6 2
 * 4 2 3 1 5 0
 *
 * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
 * В каждой клетке записано некоторое натуральное число или нуль.
 * Необходимо попасть из верхней левой клетки в правую нижнюю.
 * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
 * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
 *
 * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
 */
fun shortestPathOnField(inputName: String): Int {
    TODO()
}

// Задачу "Максимальное независимое множество вершин в графе без циклов"
// смотрите в уроке 5