/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.crud

import zakadabar.lib.examples.data.builtin.ExampleReferenceBo
import zakadabar.core.frontend.builtin.crud.ZkCrudTarget
import zakadabar.core.frontend.builtin.crud.ZkInlineCrud
import zakadabar.core.frontend.builtin.form.ZkForm
import zakadabar.core.frontend.builtin.table.ZkTable
import zakadabar.core.resource.localized

class ExampleReferenceCrud : ZkCrudTarget<ExampleReferenceBo>() {
    init {
        companion = ExampleReferenceBo.Companion
        boClass = ExampleReferenceBo::class
        editorClass = ExampleReferenceForm::class
        tableClass = ExampleReferenceTable::class
    }
}

class ExampleReferenceInlineCrud : ZkInlineCrud<ExampleReferenceBo>() {
    init {
        companion = ExampleReferenceBo.Companion
        boClass = ExampleReferenceBo::class
        editorClass = ExampleReferenceForm::class
        tableClass = ExampleReferenceTable::class
    }
}

class ExampleReferenceForm : ZkForm<ExampleReferenceBo>() {
    override fun onCreate() {
        super.onCreate()

        build(localized<ExampleReferenceForm>()) {
            + section {
                + bo::id
                + bo::name
            }
        }
    }
}

class ExampleReferenceTable : ZkTable<ExampleReferenceBo>() {

    override fun onConfigure() {

        titleText = localized<ExampleReferenceTable>()

        add = true
        search = true
        export = true

        // ExampleReferenceBo::id // record id and opt record id is not supported yet
        + ExampleReferenceBo::name

        + actions()
    }
}