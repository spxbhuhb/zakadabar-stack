/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.frontend.pages

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import zakadabar.core.browser.application.target
import zakadabar.core.browser.crud.ZkCrudTarget
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.browser.toast.toastInfo
import zakadabar.core.data.EntityId
import zakadabar.core.resource.localized
import zakadabar.core.util.UUID
import zakadabar.lib.blobs.browser.image.ZkImagesField
import zakadabar.lib.demo.data.DemoBlob
import zakadabar.lib.demo.data.DemoBo
import zakadabar.lib.demo.enums.Test

/**
 * CRUD target for [DemoBo].
 * 
 * Generated with Bender at 2021-06-04T02:35:21.583Z.
 */
class DemoCrud : ZkCrudTarget<DemoBo>() {
    init {
        companion = DemoBo.Companion
        boClass = DemoBo::class
        editorClass = DemoForm::class
        tableClass = DemoTable::class
    }
}

class DemoForm : ZkForm<DemoBo>() {
    override fun onCreate() {
        super.onCreate()

        build(localized<DemoForm>()) {
            + section {
                + bo::id
                + bo::name
                + bo::value
                + bo::test

            }

            + ZkImagesField(this, DemoBlob.comm, bo.id) {
                DemoBlob(EntityId(), bo.id, "image", it.name, it.type, it.size.toLong())
            }
        }

    }
}

/**
 * Table for [DemoBo].
 * 
 * Generated with Bender at 2021-06-04T02:35:21.584Z.
 */
class DemoTable : ZkTable<DemoBo>() {

    override fun onConfigure() {

        crud = target<DemoCrud>()

        titleText = localized<DemoTable>()

        add = true
        search = true
        export = true

        + DemoBo::id
        + DemoBo::name
        + DemoBo::value

        + actions()
    }

}