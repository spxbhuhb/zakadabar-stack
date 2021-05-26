/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.bender.frontend

import kotlinx.browser.window
import kotlinx.coroutines.await
import zakadabar.lib.bender.ClassGenerator
import zakadabar.lib.markdown.frontend.MarkdownView
import zakadabar.lib.markdown.frontend.flavour.ZkMarkdownContext
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.util.io

class Bender(
    private val classGenerator: ClassGenerator,
    private val templateUrl: String,
    private val markdownContext : () -> ZkMarkdownContext = { ZkMarkdownContext(toc = false, hashes = false) }
) : ZkElement() {

    private val resultContainer = ZkElement()
    lateinit var template: String

    override fun onCreate() {
        super.onCreate()

        io {
            template = window.fetch(templateUrl).await().text().await()

            + column {
                + BoEditor(classGenerator, resultContainer, template, markdownContext)
                + resultContainer
            }

            resultContainer += MarkdownView(sourceText = template, context = markdownContext())
        }

    }


}

