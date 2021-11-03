/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook

import org.w3c.dom.HTMLElement
import zakadabar.cookbook.browser.crud.basic.BasicCrud
import zakadabar.cookbook.browser.field.onchange.FieldOnChangeForm
import zakadabar.cookbook.browser.field.update.FieldUpdateForm
import zakadabar.cookbook.browser.form.select.filter.BoSelectFilter
import zakadabar.cookbook.browser.form.select.filter.StringSelectFilter
import zakadabar.cookbook.browser.form.select.radio.RadioGroupSelect
import zakadabar.cookbook.browser.form.submit.enter.SubmitOnEnter
import zakadabar.cookbook.browser.help.TextHelpModal
import zakadabar.cookbook.browser.navigation.direction.NavigationDirection
import zakadabar.cookbook.browser.sidebar.icons.SideBarWithIcons
import zakadabar.cookbook.browser.table.action.TableCustomActions
import zakadabar.cookbook.browser.table.basic.BasicTable
import zakadabar.cookbook.browser.table.border.vertical.TableVerticalBorderSome
import zakadabar.cookbook.browser.table.customColumn.TableCustomColumn
import zakadabar.cookbook.browser.table.exportFileName.TableExportFileName
import zakadabar.cookbook.browser.table.inline.TableEditInline
import zakadabar.cookbook.browser.table.inline.TableEditInlineNoBo
import zakadabar.cookbook.browser.table.saveElement.TableSaveElement
import zakadabar.cookbook.browser.table.tableInTab.TableInTab
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

            "BasicCrud" -> BasicCrud()
            "BasicTable" -> BasicTable()
            "BoSelectFilter" -> BoSelectFilter()
            "FieldOnChangeForm" -> FieldOnChangeForm()
            "FieldUpdateForm" -> FieldUpdateForm()
            "NavigationDirection" -> NavigationDirection()
            "RadioGroupSelect" -> RadioGroupSelect()
            "SideBarWithIcons" -> SideBarWithIcons()
            "StringSelectFilter" -> StringSelectFilter()
            "SubmitOnEnter" -> SubmitOnEnter()
            "TableCustomActions" -> TableCustomActions()
            "TableCustomColumn" -> TableCustomColumn()
            "TableEditInline" -> TableEditInline()
            "TableEditInlineNoBo" -> TableEditInlineNoBo()
            "TableExportFileName" -> TableExportFileName()
            "TableInTab" -> TableInTab()
            "TableSaveElement" -> TableSaveElement()
            "TableVerticalBorderSome" -> TableVerticalBorderSome()
            "TextHelpModal" -> TextHelpModal()

            else -> null

        }?.let { ZkElement(htmlElement) build { + it } }

}