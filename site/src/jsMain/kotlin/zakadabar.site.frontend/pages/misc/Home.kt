/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.site.frontend.pages.misc

import kotlinx.browser.window
import kotlinx.coroutines.await
import zakadabar.lib.frontend.markdown.MarkdownView
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.util.io

object Home : ZkPage() {

    override fun onCreate() {

        style {
            overflowY = "scroll"
        }

        + MarkdownView(
            """
            # Header 1
            
            ## Header 2
            
            ### Header 3
            
            #### Header 4
            
            ##### Header 5
            
            ##### Header 6
       
            Text content.
            
            Text content with inline **bold** text.

            Text content with inline `pre` text.

            * list item 1
              * list item 1.1
                * list item 1.1.1
              * list item 1.2
            * list item 2
            
            1. hello
              1. list item 1.1
                1. list item 1.1.1
            1. world
            
            An inline [link](../../demo/demo).
            
            ```kotlin
            var actionStatus = LoginAction("demo", Secret("wrong")).execute()
            ```
        """.trimIndent()
        )

        io {
            val source = window.fetch("https://raw.githubusercontent.com/spxbhuhb/zakadabar-stack/master/README.md").await().text().await()
            + MarkdownView(source)
        }
    }

}