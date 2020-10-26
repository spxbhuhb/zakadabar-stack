/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
fun main(args: Array<String>) {
    for (i in 1 until 50) {
        println(row(0, 35, i))
    }
    for (i in 35 until 95) {
        println(row(35, 70, i))
    }
}

fun row(start: Int, end: Int, i: Int): String {

    val c1 = one(start, end, i, 1)
    val c2 = one(start, end, i, 4)
    val c3 = one(start, end, i, 7)
    val c4 = one(start, end, i, 10)
    val c5 = one(start, end, i, 13)

    return "[$c1,$c2,$c3,$c4,$c5],"
}

fun one(start: Int, end: Int, i: Int, pos: Int): String {
    return when {
        i - pos - start < 0 -> "\"\""
        i - pos + 1 > end -> "\"\""
        else -> "\"${i - pos + 1}\""
    }
}