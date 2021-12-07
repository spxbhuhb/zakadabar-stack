/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.field.custom

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.toast.toastInfo
import zakadabar.core.resource.localized
import zakadabar.core.util.UUID
import zakadabar.core.util.default

class CustomSelectFieldForm: ZkForm<ExampleBo>() {

    override var mode = ZkElementMode.Other
    override var bo = default<ExampleBo>()

    override fun onCreate() {
        super.onCreate()

        val list = listOf(Pair("A", "A"), Pair("B", "B"), Pair("C", "C"))
        + stringSelectField { list[0].first } label "string select" query { list }
        + optStringSelectField { null } label "optional string select" query { list }
        + optBooleanSelectField { null } label "optional boolean select"
        + enumSelectField { Test.A } label "enum select"
        + optEnumSelectField<Test> { null } label "optional enum select"

    }
}

enum class Test {
    A, B, C
}