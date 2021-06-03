/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.crud

import zakadabar.lib.examples.data.builtin.BuiltinBo
import zakadabar.lib.examples.data.builtin.ExampleReferenceBo
import zakadabar.lib.examples.resources.strings
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.application.translate
import zakadabar.stack.frontend.builtin.crud.ZkInlineCrud
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.fields.ZkStringField
import zakadabar.stack.frontend.builtin.table.ZkTable

class BuiltinInlineCrud : ZkInlineCrud<BuiltinBo>() {
    init {
        companion = BuiltinBo.Companion
        boClass = BuiltinBo::class
        editorClass = BuiltinForm::class
        tableClass = BuiltinTable::class
    }
}

/**
 * Form for [BuiltinBo].
 * 
 * Generated with Bender at 2021-05-30T14:32:03.693Z.
 */
class BuiltinForm : ZkForm<BuiltinBo>() {
    override fun onCreate() {
        super.onCreate()

        build(translate<BuiltinForm>()) {
            + section {
                + bo::id
                + bo::booleanValue
                + bo::doubleValue
                + bo::enumSelectValue
                + bo::intValue
                + bo::instantValue
                + opt(bo::optBooleanValue, stringStore.trueText, stringStore.falseText)
                + bo::optDoubleValue
                + bo::optEnumSelectValue
                + bo::optInstantValue
                + bo::optIntValue
                + bo::optSecretValue
                + select(bo::optRecordSelectValue) { ExampleReferenceBo.all().by { it.name } }
                + bo::optStringValue
                + select(bo::optStringSelectValue, options = listOf("option 1", "option 2", "option3"))
                + bo::optTextAreaValue
                + bo::optUuidValue
                + bo::secretValue
                + select(bo::recordSelectValue) { ExampleReferenceBo.all().by { it.name } }
                + ZkStringField(this@BuiltinForm, bo::stringValue).also { this@BuiltinForm.fields += it }
                + select(bo::stringSelectValue, options = listOf("option 1", "option 2", "option3"))
                + textarea(bo::textAreaValue) label strings.textAreaValue
                + bo::uuidValue
            }
        }
    }
}

/**
 * Table for [BuiltinBo].
 * 
 * Generated with Bender at 2021-05-30T14:32:03.693Z.
 */
class BuiltinTable : ZkTable<BuiltinBo>() {

    override fun onConfigure() {

        titleText = translate<BuiltinTable>()

        add = true
        search = true
        export = true
        
        // BuiltinBo::id // record id and opt record id is not supported yet 
        + BuiltinBo::booleanValue
        + BuiltinBo::doubleValue
        + BuiltinBo::enumSelectValue
        + BuiltinBo::intValue
        + BuiltinBo::instantValue
        // + BuiltinBo::optBooleanValue // opt boolean is not supported yet
        + BuiltinBo::optDoubleValue
        // + BuiltinBo::optEnumSelectValue // opt enum is not supported yet
        + BuiltinBo::optInstantValue
        + BuiltinBo::optIntValue
        // + BuiltinBo::optSecretValue // not supported yet
        // BuiltinBo::optRecordSelectValue // record id and opt record id is not supported yet 
        + BuiltinBo::optStringValue
        + BuiltinBo::optStringSelectValue
        + BuiltinBo::optTextAreaValue
        + BuiltinBo::optUuidValue
        // + BuiltinBo::secretValue // not supported yet
        // BuiltinBo::recordSelectValue // record id and opt record id is not supported yet 
        + BuiltinBo::stringValue
        + BuiltinBo::stringSelectValue
        + BuiltinBo::textAreaValue
        + BuiltinBo::uuidValue
        
        + actions()
    }
}