@file:Suppress("UNUSED_PARAMETER")

package lesson2

import lesson1.countingSort
import java.io.File
import java.io.IOException
import java.util.*


/**
 * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
 * Простая
 *
 * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
 * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
 *
 * 201
 * 196
 * 190
 * 198
 * 187
 * 194
 * 193
 * 185
 *
 * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
 * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
 * Вернуть пару из двух моментов.
 * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
 * Например, для приведённого выше файла результат должен быть Pair(3, 4)
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun optimizeBuyAndSell(inputName: String): Pair<Int, Int> { // O(N), O(1)
    val format = Regex(".+[.]txt")
    if (!format.containsMatchIn(inputName))
        throw IOException()

    val diff = mutableListOf<Int>()
    var firstTime = true
    var first = 0
    var second = 0

    for (line in File(inputName).readLines()) {
        if (firstTime) {
            first = line.toInt()
            firstTime = false
        } else {
            second = line.toInt()
            diff.add(second - first)
            first = second
        }
    }

    var ans = diff[0]
    var l = 0
    var r = 0
    var sum = 0
    var minSum = 0
    var minPos = -1
    for (i in 0 until diff.size) {
        sum += diff[i]
        val cur = sum - minSum
        if (cur > ans) {
            ans = cur
            l = minPos + 1
            r = i
        }
        if (sum < minSum) {
            minSum = sum
            minPos = i
        }
    }
    return Pair(l + 1, r + 2)
}

/**
 * Задача Иосифа Флафия.
 * Простая
 *
 * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
 *
 * 1 2 3
 * 8   4
 * 7 6 5
 *
 * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
 * Человек, на котором остановился счёт, выбывает.
 *
 * 1 2 3
 * 8   4
 * 7 6 х
 *
 * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
 * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
 *
 * 1 х 3
 * 8   4
 * 7 6 Х
 *
 * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
 *
 * 1 Х 3
 * х   4
 * 7 6 Х
 *
 * 1 Х 3
 * Х   4
 * х 6 Х
 *
 * х Х 3
 * Х   4
 * Х 6 Х
 *
 * Х Х 3
 * Х   х
 * Х 6 Х
 *
 * Х Х 3
 * Х   Х
 * Х х Х
 *
 * Общий комментарий: решение из Википедии для этой задачи принимается,
 * но приветствуется попытка решить её самостоятельно.
 */
fun josephTask(menNumber: Int, choiceInterval: Int): Int {
    TODO()
}

/**
 * Наибольшая общая подстрока.
 * Средняя
 *
 * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
 * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
 * Если общих подстрок нет, вернуть пустую строку.
 * При сравнении подстрок, регистр символов *имеет* значение.
 * Если имеется несколько самых длинных общих подстрок одной длины,
 * вернуть ту из них, которая встречается раньше в строке first.
 */
fun longestCommonSubstring(first: String, second: String): String { // O(N^2), O(N)
    val coll = mutableListOf<MutableList<Int>>()

    for (i in first.indices) { // Рес: O(N)
        coll.add(mutableListOf())
        for (j in second.indices){
            coll[i].add(0)
        }
    }
    var max = 0
    var place = 0
    for (i in first.indices) {
        for (j in second.indices) {
            if (first[i] == second[j]) {
                if (i != 0 && j != 0) {
                    coll[i][j] = coll[i - 1][j - 1] + 1
                    if (max < coll[i][j]) {
                        max = coll[i][j]
                        place = i
                    }
                } else {
                    coll[i][j] = 1
                    if (max == 0) {
                        max = 1
                        place = i
                    }
                }
            }
        }
    }

    return first.substring(place - max + 1, place + 1)
}

/**
 * Число простых чисел в интервале
 * Простая
 *
 * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
 * Если limit <= 1, вернуть результат 0.
 *
 * Справка: простым считается число, которое делится нацело только на 1 и на себя.
 * Единица простым числом не считается.
 */
// Количество простых чисел до N : N / lnN
fun calcPrimesNumber(limit: Int): Int { // O(Nlog(logN)), O(N)
    if (limit > 1) {
        val primeN = BooleanArray(limit + 1)
        var count = 0
        Arrays.fill(primeN, true)
        primeN[0] = false
        primeN[1] = false
        for (i in 2 until primeN.size) { // N / P (P - простое число)
            if (primeN[i]) {
                count++
                var j = 2
                while (i * j < primeN.size) {
                    primeN[i * j] = false
                    ++j
                }
            }
        }
        return count
    } else return 0
}
