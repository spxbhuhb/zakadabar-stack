/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table

import org.w3c.dom.HTMLElement
import org.w3c.dom.get
import org.w3c.dom.set
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.frontend.elements.ZkBuilder
import zakadabar.stack.frontend.elements.ZkCrud
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

abstract class Column<T> : ReadOnlyProperty<Table<T>, Column<T>> {

    protected lateinit var table: Table<T>

    operator fun provideDelegate(table: Table<T>, prop: KProperty<*>): ReadOnlyProperty<Table<T>, Column<T>> {
        this.table = table
        table.columns += this
        return this
    }

    override fun getValue(thisRef: Table<T>, property: KProperty<*>) = this

    abstract fun render(builder: ZkBuilder, index: Int, row: T)

    open fun gridTemplate(): String {
        return "minmax(100px, 1fr)"
    }

}

open class RecordIdColumn<T>(
    private val prop: KProperty1<T, RecordId<T>>
) : Column<T>() {

    override fun getValue(thisRef: Table<T>, property: KProperty<*>) = this

    override fun render(builder: ZkBuilder, index: Int, row: T) {
        with(builder) {
            + "# ${prop.get(row)}"
        }
    }

}

open class StringColumn<T>(
    private val prop: KProperty1<T, String>
) : Column<T>() {

    override fun getValue(thisRef: Table<T>, property: KProperty<*>) = this

    override fun render(builder: ZkBuilder, index: Int, row: T) {
        with(builder) {
            + prop.get(row)
        }
    }

}

open class DoubleColumn<T>(
    private val prop: KProperty1<T, Double>
) : Column<T>() {

    override fun getValue(thisRef: Table<T>, property: KProperty<*>) = this

    override fun render(builder: ZkBuilder, index: Int, row: T) {
        with(builder) {
            + prop.get(row).toString()
        }
    }

}

open class OpenUpdateColumn<T>(
    private val prop: KProperty1<T, RecordId<T>>,
    private val crud: ZkCrud<T>
) : Column<T>() {

    override fun getValue(thisRef: Table<T>, property: KProperty<*>) = this

    override fun render(builder: ZkBuilder, index: Int, row: T) {
        with(builder) {
            htmlElement.dataset["recordId"] = prop.get(row).toString()
            + "edit"
            on("click") {
                val target = it.target as? HTMLElement ?: return@on
                val recordId = target.dataset["recordId"]?.toLongOrNull() ?: return@on
                crud.openUpdate(recordId)
            }
        }
    }

}

open class CustomColumn<T>(
    private val render: ZkBuilder.(T) -> Unit
) : Column<T>() {

    override fun getValue(thisRef: Table<T>, property: KProperty<*>) = this

    override fun render(builder: ZkBuilder, index: Int, row: T) {
        builder.render(row)
    }

}