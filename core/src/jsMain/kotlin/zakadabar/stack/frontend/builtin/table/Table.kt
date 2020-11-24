/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table

import kotlinx.browser.document
import org.w3c.dom.HTMLTableSectionElement
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.frontend.builtin.table.TableClasses.Companion.tableClasses
import zakadabar.stack.frontend.elements.ZkBuilder
import zakadabar.stack.frontend.elements.ZkCrud
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.util.launch
import kotlin.reflect.KProperty1

open class Table<T> : ZkElement() {

    val columns = mutableListOf<Column<T>>()

    val preloads = mutableListOf<Preload<*>>()

    private val tbody = document.createElement("tbody") as HTMLTableSectionElement

    override fun init() = build {
        + table(tableClasses.table) {
            htmlElement.style.cssText = gridTemplateColumns()
            + thead {
                columns.forEach { it.renderHeader(this) }
            }
            + tbody
        }
    }

    fun <RT : Any> preload(loader: suspend () -> RT) = Preload(loader)

    fun setData(data: List<T>): Table<T> {
        launch {
            preloads.forEach {
                it.job.join()
            }

            build {
                this.htmlElement = tbody
                for ((index, row) in data.withIndex()) {
                    + tr {
                        for (column in columns) {
                            + td { column.render(this, index, row) }
                        }
                    }
                }
            }
        }

        return this
    }

    private fun gridTemplateColumns(): String {
        var s = "grid-template-columns:"
        for (column in columns) {
            s += " " + column.gridTemplate()
        }
        return "$s;"
    }

    fun column(prop: KProperty1<T, RecordId<T>>) = RecordIdColumn(prop)

    fun column(prop: KProperty1<T, String>) = StringColumn(prop)

    fun column(prop: KProperty1<T, Double>) = DoubleColumn(prop)

    fun column(render: ZkBuilder.(T) -> Unit) = CustomColumn(render)

    fun updateLink(prop: KProperty1<T, RecordId<T>>, crud: ZkCrud<T>) = OpenUpdateColumn(prop, crud)

}