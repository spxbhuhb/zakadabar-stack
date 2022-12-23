/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx

import zakadabar.lib.xlsx.dom.Node
import zakadabar.lib.xlsx.dom.toXml
import kotlin.test.Test
import kotlin.test.assertContentEquals

class XmlTest {

    @Test
    fun testSimpleXml() {

        val doc = Node("foo", "id" to "1") {
            + Node("bars") {
                + Node("bar", "name" to "Name1")
                + Node("bar")
            }
        }

        val xml = doc.toXml()

        val ref = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<foo id=\"1\"><bars><bar name=\"Name1\"/><bar/></bars></foo>"
        assertContentEquals(ref.encodeToByteArray(), xml)

    }
}