/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.crud

import zakadabar.lib.examples.data.builtin.ExampleReferenceDto
import zakadabar.stack.frontend.application.translate
import zakadabar.stack.frontend.builtin.crud.ZkInlineCrud
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.table.ZkTable


// -----------------------------------------------------------------------------
//  Crud
// -----------------------------------------------------------------------------

class ExampleReferenceCrud : ZkInlineCrud<ExampleReferenceDto>() {
    init {
        companion = ExampleReferenceDto.Companion
        boClass = ExampleReferenceDto::class
        editorClass = ExampleReferenceForm::class
        tableClass = ExampleReferenceTable::class
    }
}

// -----------------------------------------------------------------------------
//  Form
// -----------------------------------------------------------------------------

class ExampleReferenceForm : ZkForm<ExampleReferenceDto>() {
    override fun onCreate() {
        super.onCreate()

        build(translate<ExampleReferenceForm>()) {
            + section {
                + bo::name
            }
        }
    }
}

// -----------------------------------------------------------------------------
//  Table
// -----------------------------------------------------------------------------

class ExampleReferenceTable : ZkTable<ExampleReferenceDto>() {

    override fun onConfigure() {

        titleText = translate<ExampleReferenceTable>()

        add = true
        search = true
        export = true

        + ExampleReferenceDto::id
        + ExampleReferenceDto::name
        
        + actions()
    }
}