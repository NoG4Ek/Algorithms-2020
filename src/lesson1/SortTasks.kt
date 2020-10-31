@file:Suppress("UNUSED_PARAMETER")

package lesson1

import java.io.File
import java.io.IOException
import kotlin.math.abs

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
    val time = mutableListOf<String>()

    for (line in File(inputName).readLines()) { // N - 1, O(3)
        val hours: StringBuilder = StringBuilder()
        val tr: StringBuilder = StringBuilder()

        if (line.split(":")[0] == "12") hours.append("00") else hours.append(line.split(":")[0])
        tr.append(line.split(":").subList(1, 3).joinToString(separator = ":"))

        time.add("$hours:$tr")
    }

    time.sortWith(compareBy({ it.split(" ")[1] }, { it.split(" ")[0] })) // ***

    for (i in 0 until time.size) { // ** N-1, O(2)
        if (time[i].split(":")[0] == "00")
            outputStream.write("12:" + time[i].split(":").subList(1, 3).joinToString(separator = ":"))
        else {
            outputStream.write(time[i])
        }
        if (i != time.size - 1) {
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
fun sortAddresses(
    inputName: String,
    outputName: String
) { // Трудоемкость 2Nlg2N + (N-1) = O(Nlg2N), Ресурсоемкость O(N)
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
fun sortTemperatures(inputName: String, outputName: String) { // Трудоемкость 2(N-1) = O(N - 1), Ресурсоемкость O(N)
    val minTemp = 273 * 10
    val maxTemp = 500 * 10
    val format = Regex(".+[.]txt")
    if (!format.containsMatchIn(inputName))
        throw IOException()

    val outputStream = File(outputName).bufferedWriter()
    var temp = mutableListOf<Int>()

    for (line in File(inputName).readLines()) { // N - 1
        temp.add(line.replace(".", "").toInt() + minTemp)
    }

    temp = countingSort(temp.toIntArray(), maxTemp + minTemp).toMutableList() // N - 1, O(N)

    for (i in 0 until temp.size) {
        val old = temp[i] - minTemp
        if (old < 0)
            outputStream.write("-" + abs(old / 10) + "." + abs(old) % 10)
        else
            outputStream.write("" + abs(old / 10) + "." + abs(old) % 10)
        if (i != temp.size - 1)
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

