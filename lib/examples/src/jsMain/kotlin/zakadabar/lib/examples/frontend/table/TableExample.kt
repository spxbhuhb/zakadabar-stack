/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.table

import org.w3c.dom.HTMLElement
import zakadabar.core.data.BaseBo
import zakadabar.core.schema.BoSchema
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.layout.zkLayoutStyles
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.browser.toast.toastSuccess


class ExampleTable : ZkTable<TableExampleDto>() {

    override fun onConfigure() {
        setAppTitle = false // this table is a simple example, it should not override the page title

        + TableExampleDto::name
        + TableExampleDto::scientificName
        + TableExampleDto::likes
        + TableExampleDto::hasThorns

        + actions()
    }

    override fun getRowId(row: TableExampleDto) = row.name

    override fun onDblClick(id: String) {
        toastSuccess { "You clicked on details!" }
    }

}

/**
 * DTO classes are usually defined in commonMain. This one here is to make the
 * example easier to write, but it is not accessible on the backend and it has
 * no communication feature.
 */
class TableExampleDto(
    var name: String,
    var scientificName: String,
    var likes: Int,
    var hasThorns: Boolean
) : BaseBo {
    override fun schema() = BoSchema {
        + ::name
        + ::scientificName
        + ::likes
        + ::hasThorns
    }
}

/**
 * This example shows all built in table columns with generated table data.
 */
class TableExample(
    element: HTMLElement,
) : ZkElement(element) {

    override fun onCreate() {
        super.onCreate()

        style {
            height = "200px" // because table shrinks
        }

        + zkLayoutStyles.fixBorder

        val data = listOf(
            TableExampleDto("Rose", "rosa", 341, true),
            TableExampleDto("Lily", "lilium", 384, false),
            TableExampleDto("Tulip", "tulipa", 287, false),
            TableExampleDto("Orchid", "phalaenopsis", 223, false),
            TableExampleDto("Carnation", "dianthus", 123, false)
        )

        // Add the table and set the data. It is not important to use these
        // together, you can add the table and set the data later.

        + ExampleTable().setData(data)
    }

}