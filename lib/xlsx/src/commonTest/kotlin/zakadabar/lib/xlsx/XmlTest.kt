/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx

import zakadabar.lib.xlsx.internal.dom.Node.Companion.node
import zakadabar.lib.xlsx.internal.dom.toXml
import kotlin.test.Test
import kotlin.test.assertEquals

class XmlTest {

    @Test
    fun testSimpleXml() {
        val doc = node("foo") {
            this["id"] = "1"
            + node("bars") {
                + node("bar") {
                    this["name"] = "Name1"
                }
                + node("bar")
            }
        }

        val sb = StringBuilder()
        doc.toXml(sb::append)
        val xml = sb.toString()

        val ref  = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><foo id=\"1\"><bars><bar name=\"Name1\"/><bar/></bars></foo>"
        assertEquals(ref, xml)
    }
}