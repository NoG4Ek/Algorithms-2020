@file:Suppress("UNUSED_PARAMETER")

package lesson1

import java.beans.IntrospectionException
import java.io.File
import java.io.IOException
import java.util.Comparator

/**
 * Сортировка времён
 *
 * Простая
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
 * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
 *
 * Пример:
 *
 * 01:15:19 PM
 * 07:26:57 AM
 * 10:00:03 AM
 * 07:56:14 PM
 * 01:15:19 PM
 * 12:40:31 AM
 *
 * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
 * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
 *
 * 12:40:31 AM
 * 07:26:57 AM
 * 10:00:03 AM
 * 01:15:19 PM
 * 01:15:19 PM
 * 07:56:14 PM
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun sortTimes(inputName: String, outputName: String) { // Трудоемкость Nlg2N + 3(N-1) = O(Nlg2N), Ресурсоемкость O(N)
    val format = Regex(".+[.]txt")
    if (!format.containsMatchIn(inputName))
        throw IOException()

    val outputStream = File(outputName).bufferedWriter()
    val am = mutableListOf<String>()
    val pm = mutableListOf<String>()

    for (line in File(inputName).readLines()) { // N - 1, O(3)
        if (line.split(" ")[1] == "AM")
            if (line.split(" ")[0].split(":")[0] == "12") {
                val newForm = line.split(" ")[0].replaceRange(0, 2, "00")
                am.add(newForm)
            } else {
                am.add(line.split(" ")[0])
            }
        else {
            if (line.split(" ")[0].split(":")[0] == "12") {
                val newForm = line.split(" ")[0].replaceRange(0, 2, "00")
                pm.add(newForm)
            } else {
                pm.add(line.split(" ")[0])
            }
        }
    }

    am.sort() // ***
    pm.sort() // *** Nlg2N, O(N)

    for (i in 0 until am.size) { // *
        if (am[i].split(":")[0] == "00") {
            val newForm = "12:" + am[i].split(":")[1] + ":" + am[i].split(":")[2]
            am.removeAt(i)
            am.add(i, newForm)
        }
    }

    for (i in 0 until pm.size) { // *  N-1, O(4)
        if (pm[i].split(":")[0] == "00") {
            val newForm = "12:" + pm[i].split(":")[1] + ":" + pm[i].split(":")[2]
            pm.removeAt(i)
            pm.add(i, newForm)
        }
    }

    for (data in am) { // **
        outputStream.write("$data AM")
        outputStream.newLine()
    }

    for (i in 0 until pm.size) { // ** N-1, O(2)
        outputStream.write(pm[i] + " PM")
        if (i != pm.size - 1) {
            outputStream.newLine()
        }
    }
    outputStream.close()
}

/**
 * Сортировка адресов
 *
 * Средняя
 *
 * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
 * где они прописаны. Пример:
 *
 * Петров Иван - Железнодорожная 3
 * Сидоров Петр - Садовая 5
 * Иванов Алексей - Железнодорожная 7
 * Сидорова Мария - Садовая 5
 * Иванов Михаил - Железнодорожная 7
 *
 * Людей в городе может быть до миллиона.
 *
 * Вывести записи в выходной файл outputName,
 * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
 * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
 *
 * Железнодорожная 3 - Петров Иван
 * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
 * Садовая 5 - Сидоров Петр, Сидорова Мария
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun sortAddresses(inputName: String, outputName: String) { // Трудоемкость 2Nlg2N + (N-1) = O(Nlg2N), Ресурсоемкость O(N)
    val format = Regex(".+[.]txt")
    if (!format.containsMatchIn(inputName))
        throw IOException()

    val affiliation = mutableMapOf<String, MutableSet<String>>()

    for (line in File(inputName).readLines()) {
        affiliation.getOrPut(line.split(" - ")[1]) { mutableSetOf() }.add(line.split(" - ")[0]) // N - 1, O(N)
    }

    val o = buildString {
        affiliation.keys.sortedWith(
            compareBy({ it.split(" ")[0] }, // Nlg2N
                { it.split(" ")[1].toInt() })
        ).forEach {
            append(it)
            append(" - ")
            val sR = affiliation[it]!! //Nlg2N, O(1)
                .sorted()
                .joinToString(separator = ", ")
            append(sR)
            append(System.lineSeparator())
        }
    }

    File(outputName).writeText(o)
}

/**
 * Сортировка температур
 *
 * Средняя
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
 * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
 * Например:
 *
 * 24.7
 * -12.6
 * 121.3
 * -98.4
 * 99.5
 * -12.6
 * 11.0
 *
 * Количество строк в файле может достигать ста миллионов.
 * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
 * Повторяющиеся строки сохранить. Например:
 *
 * -98.4
 * -12.6
 * -12.6
 * 11.0
 * 24.7
 * 99.5
 * 121.3
 */
fun sortTemperatures(inputName: String, outputName: String) { // Трудоемкость 3(N-1) = O(N - 1), Ресурсоемкость O(N)
    val format = Regex(".+[.]txt")
    if (!format.containsMatchIn(inputName))
        throw IOException()

    val outputStream = File(outputName).bufferedWriter()
    var positive = mutableListOf<Int>()
    var negative = mutableListOf<Int>()

    for (line in File(inputName).readLines()) { // N - 1
        if (line[0] == '-') {
            negative.add(line.replace(".", "").replace("-", "").toInt())
        } else {
            positive.add(line.replace(".", "").toInt())
        }
    }

    positive = countingSort(positive.toIntArray(), 5000).toMutableList() // N - 1, O(N)
    negative = countingSort(negative.toIntArray(), 2730).toMutableList() // N - 1, O(N)

    for (i in negative.size - 1 downTo 0) {
        outputStream.write("-" + negative[i] / 10 + "." + negative[i] % 10)
        outputStream.newLine()
    }

    for (i in 0 until positive.size) {
        outputStream.write("" + positive[i] / 10 + "." + positive[i] % 10)
        if (i != positive.size - 1)
            outputStream.newLine()
    }

    outputStream.close()
}

/**
 * Сортировка последовательности
 *
 * Средняя
 * (Задача взята с сайта acmp.ru)
 *
 * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
 *
 * 1
 * 2
 * 3
 * 2
 * 3
 * 1
 * 2
 *
 * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
 * а если таких чисел несколько, то найти минимальное из них,
 * и после этого переместить все такие числа в конец заданной последовательности.
 * Порядок расположения остальных чисел должен остаться без изменения.
 *
 * 1
 * 3
 * 3
 * 1
 * 2
 * 2
 * 2
 */
fun sortSequence(inputName: String, outputName: String) {
    TODO()
}

/**
 * Соединить два отсортированных массива в один
 *
 * Простая
 *
 * Задан отсортированный массив first и второй массив second,
 * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
 * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
 *
 * first = [4 9 15 20 28]
 * second = [null null null null null 1 3 9 13 18 23]
 *
 * Результат: second = [1 3 4 9 9 13 15 20 23 28]
 */
fun <T : Comparable<T>> mergeArrays(first: Array<T>, second: Array<T?>) {
    TODO()
}

