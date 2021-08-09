/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import org.junit.Test
import kotlin.test.assertEquals

class RecipeParserTest {

    @Test
    fun testParse() {
        val source = """
           # Recipe Title

           ```yaml
           level: Beginner
           targets:
             - jvm
             - browser
           tags:
             - css
             - font
           ```

           ## Section

           Section content.

           ## Guides

           - [Introduction: Browser](/doc/guides/browser/Introduction.md)

           ## Code

           - [index-link.html](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/css/fontfiles/index-link.html)
           - [index-inline.html](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/css/fontfiles/index-inline.html)
           - [FontFiles.kt](/cookbook/src/jvmMain/kotlin/zakadabar/cookbook/browser/css/fontfiles/FontFiles.kt)

           ## Section 2

           Section content.

           ### Code2

           - [Introduction: Browser](/doc/guides/browser/Introduction.md)

       """.trimIndent()

        val parser = RecipeParser(source.split("\n")).parse()

        assertEquals("Recipe Title", parser.recipe.title)
        assertEquals("Beginner", parser.recipe.level)

        println(parser.content)
    }
}