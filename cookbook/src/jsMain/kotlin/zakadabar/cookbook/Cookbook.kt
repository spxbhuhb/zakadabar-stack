/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook

import org.w3c.dom.HTMLElement
import zakadabar.cookbook.browser.form.select.BoSelectFilter
import zakadabar.cookbook.browser.form.select.StringSelectFilter
import zakadabar.cookbook.browser.help.TextHelpModal
import zakadabar.cookbook.browser.table.border.vertical.TableVerticalBorderOne
import zakadabar.cookbook.browser.table.border.vertical.TableVerticalBorderSome
import zakadabar.cookbook.browser.table.inline.TableEditInline
import zakadabar.cookbook.browser.table.inline.TableEditInlineNoBo
import zakadabar.cookbook.resource.strings
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.application.application
import zakadabar.core.browser.help.TextHelpProvider
import zakadabar.core.module.CommonModule
import zakadabar.core.module.modules
import zakadabar.core.resource.ZkFlavour

class Cookbook : CommonModule {

    override fun onModuleLoad() {
        super.onModuleLoad()
        application.stringStores += strings
        modules += TextHelpProvider()
    }

    fun enrich(htmlElement: HTMLElement, type: String, flavour: ZkFlavour): ZkElement? =
        when (type) {

            "BoSelectFilter" -> BoSelectFilter()
            "StringSelectFilter" -> StringSelectFilter()
            "TextHelpModal" -> TextHelpModal()
            "TableEditInline" -> TableEditInline()
            "TableEditInlineNoBo" -> TableEditInlineNoBo()
            "TableVerticalBorderOne" -> TableVerticalBorderOne()
            "TableVerticalBorderSome" -> TableVerticalBorderSome()

            else -> null

        }?.let { ZkElement(htmlElement) build { + it } }

}