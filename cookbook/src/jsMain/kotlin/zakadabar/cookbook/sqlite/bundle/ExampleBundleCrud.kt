/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.sqlite.bundle

import zakadabar.core.browser.application.target
import zakadabar.core.browser.crud.ZkCrudTarget
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.resource.localized


/**
 * CRUD target for [ExampleBundle].
 * 
 * Generated with Bender at 2021-07-19T01:43:03.148Z.
 */
class ExampleBundleCrud : ZkCrudTarget<ExampleBundle>() {
    init {
        companion = ExampleBundle.Companion
        boClass = ExampleBundle::class
        editorClass = ExampleBundleForm::class
        tableClass = ExampleBundleTable::class
    }
}

/**
 * Form for [ExampleBundle].
 * 
 * Generated with Bender at 2021-07-19T01:43:03.148Z.
 */
class ExampleBundleForm : ZkForm<ExampleBundle>() {
    override fun onCreate() {
        super.onCreate()

        build(localized<ExampleBundleForm>()) {
            + section {
                + bo::id
                + bo::name
            }
        }
    }
}

/**
 * Table for [ExampleBundle].
 * 
 * Generated with Bender at 2021-07-19T01:43:03.148Z.
 */
class ExampleBundleTable : ZkTable<ExampleBundle>() {

    override fun onConfigure() {

        crud = target<ExampleBundleCrud>()

        titleText = localized<ExampleBundleTable>()

        add = true
        search = true
        export = true
        
        + ExampleBundle::id
        + ExampleBundle::name
        
        + actions()
    }
}