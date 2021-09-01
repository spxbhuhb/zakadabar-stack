/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.form.select

import zakadabar.cookbook.cookbookStyles
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.field.ZkSelectBase
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.data.BaseBo
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityId
import zakadabar.core.util.default

// this is the BO we use for filtering

class ExampleFilterBo(
    override var id: EntityId<ExampleFilterBo>
) : EntityBo<ExampleFilterBo> {
    override fun getBoNamespace() = throw NotImplementedError()
    override fun comm() = throw NotImplementedError()
}

// this is the BO we filter for

class ExampleFilteredBo(
    override var id: EntityId<ExampleFilteredBo>
) : EntityBo<ExampleFilteredBo> {
    override fun getBoNamespace() = throw NotImplementedError()
    override fun comm() = throw NotImplementedError()
}

class ExampleReferenceFormBo(
    var reference: EntityId<ExampleFilteredBo>
) : BaseBo

class BoSelectFilter : ZkForm<ExampleReferenceFormBo>() {

    override var mode = ZkElementMode.Other
    override var bo = default<ExampleReferenceFormBo>()

    // value of the filtering select, we don't use this directly

    var filterValue: EntityId<ExampleFilterBo>? = null

    // options of the filtering select

    val filterOptions = (0..9).map { EntityId<ExampleFilterBo>(it.toString()) to "group $it" }

    // all possible options for the filtered select

    val allOptions = (0..99).map { EntityId<ExampleFilteredBo>(it.toString()) to "item $it" }

    // the select we want to filter

    lateinit var filteredField: ZkSelectBase<EntityId<ExampleFilteredBo>,*>

    override fun onCreate() {
        super.onCreate()

        + cookbookStyles.inlineForm

        + this::filterValue query { filterOptions } onSelect ::onFilterSelect
        + bo::reference query { emptyList() } saveAs { filteredField = it }

    }

    fun onFilterSelect(value: Pair<EntityId<ExampleFilterBo>?, String>?) {

        // when filter is "not selected", clear filtered items and value

        val filterBy = value?.first ?: return filteredField.update(emptyList(), null, false)

        // when filter is selected, create a new item list for the filtered select

        val newItems = allOptions.filter { it.first.toLong() / 10 == filterBy.toLong() }

        // also, check if the currently selected value is in the new item list

        val newValue = filteredField.getPropValue()?.let { current ->
            newItems.firstOrNull { it.first == current }
        }

        // update the filtered select

        filteredField.update(newItems, newValue, false)
    }
}