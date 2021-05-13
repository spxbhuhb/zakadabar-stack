/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.text

import kotlin.test.Test
import kotlin.test.assertEquals

class YamlNavTest {

    @Test
    fun test() {
        val source = """
            * [Browser]()
                * [Css](./browser/Css.md)
                * [Buttons](./browser/Buttons.md)
            * [Backend](./backend/Backend.md)
                * [Routing](./backend/Routing.md)
                * [Record Backends](./backend/RecordBackends.md)
        """.trimIndent()

        val items = MarkdownNav().parse(source)

        assertEquals(2, items.size)

        items[0].assert("Browser", "", 2)
        items[0].children[0].assert("Css", "./browser/Css.md", 0)
        items[0].children[1].assert("Buttons", "./browser/Buttons.md", 0)

        items[1].assert("Backend","./backend/Backend.md", 2)
        items[1].children[0].assert("Routing", "./backend/Routing.md", 0)
        items[1].children[1].assert("Record Backends", "./backend/RecordBackends.md", 0)
    }

    private fun MarkdownNav.MarkdownNavItem.assert(label : String, url : String, childrenSize : Int) {
        assertEquals(label, this.label)
        assertEquals(url, this.url)
        assertEquals(childrenSize, this.children.size)
    }

}