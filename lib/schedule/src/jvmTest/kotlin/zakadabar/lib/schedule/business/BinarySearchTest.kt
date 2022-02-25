/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.schedule.business

import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.math.absoluteValue
import kotlin.test.assertEquals

class BinarySearchTest {

    inline val Char.old
        get() = this to false

    inline val Char.new
        get() = this to true

    @Test
    fun testJob() = runBlocking {

        val index = emptyList<Char>().binarySearch {
            it.compareTo('a')
        }.let { (it + 1).absoluteValue }

        assertEquals(0, index)


        // [b, d, f]
        // insert "a" -> return value = -1 -> insert position = 0
        // insert "b" -> return value = 1 -> insert position = 1

        insert('a', 'a'.new, 'b'.old, 'd'.old, 'f'.old)
        insert('b', 'b'.old, 'b'.new, 'd'.old, 'f'.old)
        insert('c', 'b'.old, 'c'.new, 'd'.old, 'f'.old)
        insert('d', 'b'.old, 'd'.old, 'd'.new, 'f'.old)
        insert('e', 'b'.old, 'd'.old, 'e'.new, 'f'.old)
        insert('f', 'b'.old, 'd'.old, 'f'.old, 'f'.new)
        insert('g', 'b'.old, 'd'.old, 'f'.old, 'g'.new)
    }

    fun insert(insert: Char, vararg expectation: Pair<Char, Boolean>) {
        val list = mutableListOf('b' to false, 'd' to false, 'f' to false)

        val index = list.binarySearch {
            it.first.compareTo(insert)
        }.let { (it + 1).absoluteValue }

        list.add(index, insert to true)

        assertEquals(expectation[0], list[0])
        assertEquals(expectation[1], list[1])
        assertEquals(expectation[2], list[2])
        assertEquals(expectation[3], list[3])
    }


}
