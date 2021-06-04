/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.frontend.pages

import zakadabar.lib.demo.data.TestBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.frontend.application.target
import zakadabar.stack.frontend.application.translate
import zakadabar.stack.frontend.builtin.crud.ZkCrudTarget
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.table.ZkTable


/**
 * CRUD target for [TestBo].
 * 
 * Generated with Bender at 2021-06-04T02:35:21.583Z.
 */
class TestCrud : ZkCrudTarget<TestBo>() {
    init {
        companion = TestBo.Companion
        boClass = TestBo::class
        pageClass = TestForm::class
        tableClass = TestTable::class
    }
}

/**
 * Form for [TestBo].
 * 
 * Generated with Bender at 2021-06-04T02:35:21.583Z.
 */
class TestForm : ZkForm<TestBo>() {
    override fun onCreate() {
        super.onCreate()

        build(translate<TestForm>()) {
            + section {
                + bo::id
                + bo::name
                + bo::value
            }
        }
    }
}

/**
 * Table for [TestBo].
 * 
 * Generated with Bender at 2021-06-04T02:35:21.584Z.
 */
class TestTable : ZkTable<TestBo>() {

    override fun onConfigure() {

        crud = target<TestCrud>()

        titleText = translate<TestTable>()

        add = true
        search = true
        export = true
        
        + TestBo::id
        + TestBo::name
        + TestBo::value
        
        + actions()
    }

    override fun onCreate() {
        super.onCreate()

        val data = (1..10000).map {
            TestBo(
                id = EntityId(it.toLong()),
                name = "Bo $it",
                value = it
            )
        }

        setData(data)
    }
}