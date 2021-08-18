/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook

import org.w3c.dom.HTMLElement
import zakadabar.cookbook.browser.help.TextHelpModal
import zakadabar.cookbook.browser.table.inline.TableEditInline
import zakadabar.cookbook.resource.strings
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.application.application
import zakadabar.core.browser.help.TextHelpProvider
import zakadabar.core.browser.layout.zkLayoutStyles
import zakadabar.core.module.CommonModule
import zakadabar.core.module.modules
import zakadabar.core.resource.ZkFlavour
import zakadabar.core.resource.css.px

class Cookbook : CommonModule {

    override fun onModuleLoad() {
        super.onModuleLoad()
        application.stringStores += strings
        modules += TextHelpProvider()
    }

    fun enrich(htmlElement: HTMLElement, type: String, flavour: ZkFlavour): ZkElement? {
        return when (type) {
            "TextHelpModal" -> htmlElement.zk { + TextHelpModal() }

            "TableEditInline" -> htmlElement.zk {
                height = 400.px
                + zkLayoutStyles.fixBorder
                + TableEditInline()
            }

            else -> null
        }
    }

    fun HTMLElement.zk(builder : ZkElement.() -> Unit) : ZkElement =
        ZkElement(this) build { builder() }

}