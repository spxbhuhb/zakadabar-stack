/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.sqlite.bundle

import zakadabar.stack.frontend.application.target
import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.table.ZkTable
import zakadabar.stack.resources.localized


/**
 * CRUD target for [ExampleBo].
 * 
 * Generated with Bender at 2021-07-19T02:06:15.094Z.
 */
class ExampleCrud : ZkCrudTarget<ExampleBo>() {
    init {
        companion = ExampleBo.Companion
        boClass = ExampleBo::class
        editorClass = ExampleForm::class
        tableClass = ExampleTable::class
    }
}

/**
 * Form for [ExampleBo].
 * 
 * Generated with Bender at 2021-07-19T02:06:15.094Z.
 */
class ExampleForm : ZkForm<ExampleBo>() {
    override fun onCreate() {
        super.onCreate()

        build(localized<ExampleForm>()) {
            + section {
                + bo::id
                + bo::c1
                + bo::c2
            }
        }
    }
}

/**
 * Table for [ExampleBo].
 * 
 * Generated with Bender at 2021-07-19T02:06:15.094Z.
 */
class ExampleTable : ZkTable<ExampleBo>() {

    override fun onConfigure() {

        crud = target<ExampleCrud>()

        titleText = localized<ExampleTable>()

        add = true
        search = true
        export = true
        
        + ExampleBo::id
        + ExampleBo::c1
        + ExampleBo::c2
        
        + actions()
    }
}