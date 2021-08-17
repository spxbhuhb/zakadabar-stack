/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.bender.frontend

import kotlinx.browser.window
import kotlinx.coroutines.await
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.util.io
import zakadabar.lib.bender.ClassGenerator
import zakadabar.lib.markdown.browser.flavour.ZkMarkdownContext

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
        }

    }


}

