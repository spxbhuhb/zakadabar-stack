/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package zakadabar.stack.util

import org.junit.Test
import kotlin.test.assertEquals

class UUIDTest {

    @Test
    fun testMake() {
        val ju = java.util.UUID.randomUUID()

        val array = IntArray(4)
        array[0] = (ju.mostSignificantBits shr 32).toInt()
        array[1] = (ju.mostSignificantBits and 0xffffffff).toInt()
        array[2] = (ju.leastSignificantBits shr 32).toInt()
        array[3] = (ju.leastSignificantBits and 0xffffffff).toInt()

        val cu = UUID(array, 0)

        assertEquals(ju.toString(), cu.toString())
    }

    @Test
    fun testMake2() {

        val cu = UUID()

        val ju = java.util.UUID.fromString(cu.toString())

        assertEquals(4, ju.version())
        assertEquals(2, ju.variant())

        assertEquals(ju.toString(), cu.toString())
    }

    @Test
    fun testMakeFromString() {
        val ju = java.util.UUID.randomUUID()
        val cu = UUID(ju.toString())

        assertEquals(4, ju.version())
        assertEquals(2, ju.variant())

        assertEquals(ju.toString(), cu.toString())
    }

    @Test
    fun testLongConversion() {
        val cu = UUID("ea3d1c2b-0559-4199-96f6-45c4fcfe6ccd")
        val ju = java.util.UUID(cu.msb, cu.lsb)

        assertEquals(ju.toString(), cu.toString())
    }
}
