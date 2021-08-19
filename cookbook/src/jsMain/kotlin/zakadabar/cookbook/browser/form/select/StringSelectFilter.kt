/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.form.select

import zakadabar.cookbook.cookbookStyles
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.field.ZkSelectBase
import zakadabar.core.browser.field.onSelect
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.data.BaseBo
import zakadabar.core.util.default

class ExampleStringFormBo(
    var value: String
) : BaseBo

class StringSelectFilter : ZkForm<ExampleStringFormBo>() {

    override var mode = ZkElementMode.Other
    override var bo = default<ExampleStringFormBo>()

    // value of the filtering select, we don't use this directly

    var filterValue: String? = null

    // options of the filtering select

    val filterOptions = (1..9).map { it.toString() to "group $it" }

    // all possible options for the filtered select

    val allOptions = (0..99).map { it.toString() to "item $it" }

    // the select we want to filter

    lateinit var filteredField: ZkSelectBase<String>

    override fun onCreate() {
        super.onCreate()

        + cookbookStyles.inlineForm

        + this::filterValue.asSelect() query { filterOptions } onSelect ::onFilterSelect
        + bo::value.asSelect() query { emptyList() } saveAs { filteredField = it }

    }

    fun onFilterSelect(value: Pair<String?, String>?) {

        // when filter is "not selected", clear filtered items and value

        val filterBy = value?.first ?: return filteredField.update(emptyList(), null)

        // when filter is selected, create a new item list for the filtered select

        val newItems = allOptions.filter { it.first.toLong() / 10 == filterBy.toLong() }

        // also, check if the currently selected value is in the new item list

        val newValue = filteredField.getPropValue()?.let { current ->
            newItems.firstOrNull { it.first == current }
        }

        // update the filtered select

        filteredField.update(newItems, newValue)
    }
}