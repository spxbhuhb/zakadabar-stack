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
//                + bo::name
//                + bo::value
//                + bo::test
                + stringField { "Name" } label "name" onChange { value -> bo.name = value }
                + optStringField { null } label "NoName"
                + intField { 5 } label "int" onChange { value -> bo.value = value}
                + optIntField { null } label "NoValue"
                + doubleField { 5.5 } label "double"
                + optDoubleField { null } label "no double"
                + longField { 555555L } label "long"
                + optLongField { null } label "no long"
                + booleanField { true } label "boolean"
                + localDateField { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date } label "local date"
                + optLocalDateField { null } label "no local date"
                + localDateTimeField { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) } label "local date time"
                + optLocalDateField { null } label "no local date time"
                + instantField { Clock.System.now() } label "instant"
                + optIntField { null } label "no instant"
                + textAreaField { "lalalala" } label "text area"
                + optTextAreaField { null } label "no text area"
                + uuidField { UUID() } label "uuid"
                + optUuidField { null } label "no uuid"

                val list = listOf(Pair("A", "A"), Pair("B", "B"), Pair("C", "C"))
                val booleanList = listOf(Pair(true, "true"), Pair(false, "false"))
                + stringSelectField { list[0].first } label "string select" query { list }
                + optStringSelectField { null } label "no string select" query { list }
                + optBooleanSelectField { null } label "no boolean select" query { booleanList}
                + enumSelectField { Test.A } label "enum"
                + optEnumSelectField<Test> { null } label "no enum"

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