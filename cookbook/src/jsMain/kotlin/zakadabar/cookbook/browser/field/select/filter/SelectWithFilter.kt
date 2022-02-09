/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.field.select.filter

import zakadabar.cookbook.cookbookStyles
import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.cookbook.entity.builtin.ExampleReferenceBo
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.field.ZkPropEntitySelectField
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.data.EntityId
import zakadabar.core.util.default

open class SelectWithFilter : ZkForm<ExampleBo>() {

    override var mode = ZkElementMode.Other
    override var bo = default<ExampleBo>()

    lateinit var recordSelectField: ZkPropEntitySelectField<ExampleReferenceBo>

    val options = listOf("option 1", "option 2", "option3").map { it to it }

    override fun onCreate() {
        super.onCreate()

        + cookbookStyles.inlineForm

        + section {

            + bo::recordSelectValue query ::queryRecords saveAs ::recordSelectField filter true

            + bo::stringSelectValue.asSelect() query { options }
        }
    }

    open suspend fun queryRecords(): List<Pair<EntityId<ExampleReferenceBo>, String>> =
        ExampleReferenceBo.all().by { it.name }

}