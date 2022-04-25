/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook

import org.w3c.dom.HTMLElement
import zakadabar.cookbook.blob.attachment.AttachmentsField
import zakadabar.cookbook.blob.image.ImagesField
import zakadabar.cookbook.browser.crud.basic.BasicCrud
import zakadabar.cookbook.browser.field.all.AllPropFields
import zakadabar.cookbook.browser.field.all.AllPropFieldsWithLabel
import zakadabar.cookbook.browser.field.all.AllValueFields
import zakadabar.cookbook.browser.field.all.AllValueFieldsIo
import zakadabar.cookbook.browser.field.custom.CustomInputFieldForm
import zakadabar.cookbook.browser.field.custom.CustomSelectFieldForm
import zakadabar.cookbook.browser.field.onchange.FieldOnChangeForm
import zakadabar.cookbook.browser.field.select.filter.SelectWithFilter
import zakadabar.cookbook.browser.field.update.FieldUpdateForm
import zakadabar.cookbook.browser.form.modal.FormInModalOpen
import zakadabar.cookbook.browser.form.select.filter.BoSelectFilter
import zakadabar.cookbook.browser.form.select.filter.StringSelectFilter
import zakadabar.cookbook.browser.form.select.radio.RadioGroupSelect
import zakadabar.cookbook.browser.form.submit.enter.SubmitOnEnter
import zakadabar.cookbook.browser.help.TextHelpModal
import zakadabar.cookbook.browser.navigation.direction.NavigationDirection
import zakadabar.cookbook.browser.sidebar.icons.SideBarWithIcons
import zakadabar.cookbook.browser.tabcontainer.background.TabContainerBackground
import zakadabar.cookbook.browser.table.action.TableCustomActions
import zakadabar.cookbook.browser.table.basic.BasicTable
import zakadabar.cookbook.browser.table.border.vertical.TableVerticalBorderSome
import zakadabar.cookbook.browser.table.counter.TableWithCounter
import zakadabar.cookbook.browser.table.customColumn.TableCustomColumn
import zakadabar.cookbook.browser.table.export.filename.TableExportFileName
import zakadabar.cookbook.browser.table.export.filtered.TableExportFiltered
import zakadabar.cookbook.browser.table.export.headers.TableExportHeaders
import zakadabar.cookbook.browser.table.indexColumn.TableIndexColumn
import zakadabar.cookbook.browser.table.inline.TableEditInline
import zakadabar.cookbook.browser.table.inline.TableEditInlineNoBo
import zakadabar.cookbook.browser.table.multiLevel.TableMultiLevel
import zakadabar.cookbook.browser.table.saveElement.TableSaveElement
import zakadabar.cookbook.browser.table.tableInTab.TableInTab
import zakadabar.cookbook.browser.table.variableHeight.TableVariableHeight
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
            "TabContainerBackground" -> TabContainerBackground()
            "TableCustomActions" -> TableCustomActions()
            "TableCustomColumn" -> TableCustomColumn()
            "TableEditInline" -> TableEditInline()
            "TableEditInlineNoBo" -> TableEditInlineNoBo()
            "TableExportFileName" -> TableExportFileName()
            "TableExportFiltered" -> TableExportFiltered()
            "TableExportHeaders" -> TableExportHeaders()
            "TableIndexColumn" -> TableIndexColumn()
            "TableWithCounter" -> TableWithCounter()
            "TableInTab" -> TableInTab()
            "TableSaveElement" -> TableSaveElement()
            "TableVerticalBorderSome" -> TableVerticalBorderSome()
            "TextHelpModal" -> TextHelpModal()
            "CustomInputFieldForm" -> CustomInputFieldForm()
            "CustomSelectFieldForm" -> CustomSelectFieldForm()
            "AllPropFields" -> AllPropFields()
            "AllPropFieldsWithLabel" -> AllPropFieldsWithLabel()
            "AllValueFields" -> AllValueFields()
            "AllValueFieldsIo" -> AllValueFieldsIo()
            "SelectWithFilter" -> SelectWithFilter()
            "FormInModalOpen" -> FormInModalOpen()
            "AttachmentsField" -> AttachmentsField()
            "ImagesField" -> ImagesField()
            "TableVariableHeight" -> TableVariableHeight()
            "TableMultiLevel" -> TableMultiLevel()


            else -> null

        }?.let { ZkElement(htmlElement) build { + it } }

}