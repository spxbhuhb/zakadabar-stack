/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.table

import kotlinx.dom.clear
import zakadabar.demo.frontend.table.TableClasses.Companion.tableClasses
import zakadabar.stack.frontend.elements.ZkBuilder
import zakadabar.stack.frontend.elements.ZkElement
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

open class Table<T> : ZkElement() {

    val columns = mutableListOf<Column<T>>()

    fun setData(data: List<T>): Table<T> {
        element.clear()

        build {
            + table(tableClasses.table) {
                htmlElement.style.cssText = gridTemplateColumns()
                for ((index, row) in data.withIndex()) {
                    + tr(tableClasses.tableRow) {
                        for (column in columns) {
                            + td { column.render(this@Table, this, index, row) }
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
}

open class Column<T>(val prop: KProperty1<T, *>? = null, init: Column<T>.() -> Unit = { }) : ReadOnlyProperty<Table<T>, Column<T>> {

    operator fun provideDelegate(table: Table<T>, prop: KProperty<*>): ReadOnlyProperty<Table<T>, Column<T>> {
        table.columns += this
        return this
    }

    override fun getValue(thisRef: Table<T>, property: KProperty<*>) = this

    open fun render(table: Table<T>, builder: ZkBuilder, index: Int, row: T) {
        with(builder) {
            if (prop != null) {
                + prop.get(row).toString()
            }
        }
    }

    fun gridTemplate(): String {
        return "minmax(100px, 1.33fr)"
    }

}