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

package zakadabar.stack.frontend.util.routing

import org.w3c.dom.url.URLSearchParams
import zakadabar.stack.Stack
import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.frontend.util.routing.Route.Companion.handle
import zakadabar.stack.frontend.util.routing.Route.Companion.route
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RouterTest {

    class TestUiElement(val testId: Int) : ComplexElement()

    private fun makeRequest(path: String) =
        ViewRequest(
            path.split('/').filter { it.isNotEmpty() },
            URLSearchParams(object {}.asDynamic())
        )

    @Test
    fun testRoot() {
        val router = Router()

        router += handle("/") { TestUiElement(1) }

        val view = router.route(makeRequest("/"))

        assertTrue(view is TestUiElement)
        assertEquals(1, view.testId)
    }

    @Test
    fun testNoMatch() {
        val router = Router()

        router += route("/") {
            handle("stuff") { TestUiElement(1) }
        }

        val view = router.route(makeRequest("/"))

        assertNull(view)
    }

    @Test
    fun testTopMatch() {
        val router = Router()

        router += route("/") {
            handle("stuff") { TestUiElement(1) }
        }

        val view = router.route(makeRequest("/stuff"))

        assertTrue(view is TestUiElement)
        assertEquals(1, view.testId)
    }

    @Test
    fun testMore() {
        val router = Router()

        router += route("/1a2b3c") {
            handle("stuff1") { TestUiElement(1) }
            handle("stuff2") { TestUiElement(2) }
        }

        router += route("/2b3c4d") {
            handle("stuff") { TestUiElement(3) }
        }

        var view = router.route(makeRequest("/1a2b3c/stuff1"))

        assertTrue(view is TestUiElement)
        assertEquals(1, view.testId)

        view = router.route(makeRequest("/1a2b3c/stuff2"))

        assertTrue(view is TestUiElement)
        assertEquals(2, view.testId)

        view = router.route(makeRequest("/2b3c4d/stuff"))

        assertTrue(view is TestUiElement)
        assertEquals(3, view.testId)

        view = router.route(makeRequest("/1a2b3c/stuff"))

        assertNull(view)
    }

    @Test
    fun testParam() {
        val router = Router()

        router += route("/") {
            handle("{p}") { TestUiElement(it.pathParams["p"] !!.toInt()) }
        }

        var view = router.route(makeRequest("/12"))

        assertTrue(view is TestUiElement)
        assertEquals(12, view.testId)

        view = router.route(makeRequest("/"))

        assertNull(view)
    }

    @Test
    fun testRest() {
        val router = Router()

        router += route("/${Stack.shid}/entities") {

            handle("") {
                TestUiElement(1)
            }

            handle("/new") {
                TestUiElement(2)
            }

            handle("/{entityId}") {
                TestUiElement(it.pathParams["entityId"] !!.toInt())
            }

            handle("/{entityId}/edit") {
                TestUiElement(100 + it.pathParams["entityId"] !!.toInt())
            }

        }

        var view = router.route(makeRequest("/${Stack.shid}/entities"))

        assertTrue(view is TestUiElement)
        assertEquals(1, view.testId)

        view = router.route(makeRequest("/${Stack.shid}/entities/new"))

        assertTrue(view is TestUiElement)
        assertEquals(2, view.testId)

        view = router.route(makeRequest("/${Stack.shid}/entities/3"))

        assertTrue(view is TestUiElement)
        assertEquals(3, view.testId)

        view = router.route(makeRequest("/${Stack.shid}/entities/3/edit"))

        assertTrue(view is TestUiElement)
        assertEquals(103, view.testId)

    }
}