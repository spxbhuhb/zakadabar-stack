/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table

import kotlinx.browser.document
import kotlinx.datetime.Instant
import kotlinx.dom.clear
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLTableSectionElement
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.set
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.frontend.builtin.table.columns.*
import zakadabar.stack.frontend.elements.ZkCrud
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.util.getDatasetEntry
import zakadabar.stack.frontend.util.launch
import kotlin.reflect.KProperty1

open class ZkTable<T : DtoBase> : ZkElement() {

    var title: String? = null

    var crud: ZkCrud<*>? = null

    var onCreate: (() -> Unit)? = null
    var onUpdate: ((id: RecordId<T>) -> Unit)? = null
    var onSearch: ((searchText: String) -> Unit)? = null

    var titleBar: ZkTableTitleBar? = null

    val columns = mutableListOf<ZkColumn<T>>()

    val preloads = mutableListOf<ZkTablePreload<*>>()

    private val tbody = document.createElement("tbody") as HTMLTableSectionElement

    override fun init() = build {
        className = ZkTableStyles.outerContainer

        buildTitleBar()?.let { + it }

        + div(ZkTableStyles.contentContainer) {

            + table(ZkTableStyles.table) {
                currentElement.style.cssText = gridTemplateColumns()
                + thead {
                    columns.forEach { it.renderHeader(this) }
                }
                + tbody
            }
        }

        // this is here to prevent text selection on double click
        on("mousedown") { event ->
            event as MouseEvent
            if (event.detail > 1) {
                event.preventDefault()
            }
        }

        // this handles the double click itself
        on("dblclick") { event ->
            event as MouseEvent
            event.preventDefault()

            val target = event.target as HTMLElement
            val rid = target.getDatasetEntry("rid")?.toLongOrNull() ?: return@on

            if (onUpdate != null) {
                onUpdate?.invoke(rid)
            } else if (crud != null) {
                crud?.openUpdate(rid)
            }
        }

        on("click") { event ->
            event as MouseEvent
            event.preventDefault()

            val target = event.target as HTMLElement
            val rid = target.getDatasetEntry("rid")?.toLongOrNull() ?: return@on
            val action = target.getDatasetEntry("action") ?: return@on

            if (action == "update") {
                if (onUpdate != null) {
                    onUpdate?.invoke(rid)
                } else if (crud != null) {
                    crud?.openUpdate(rid)
                }
            }
        }
    }

    private fun buildTitleBar(): ZkTableTitleBar? {

        if (titleBar == null && (title != null || onCreate != null || onSearch != null || crud != null)) {
            titleBar = ZkTableTitleBar {
                title = this@ZkTable.title
                if (this@ZkTable.onCreate != null) {
                    onCreate = this@ZkTable.onCreate !!
                } else if (this@ZkTable.crud != null) {
                    onCreate = { this@ZkTable.crud !!.openCreate() }
                }
                this@ZkTable.onSearch?.let { onSearch = it }
            }
        }

        return titleBar
    }

    fun <RT : Any> preload(loader: suspend () -> RT) = ZkTablePreload(loader)

    fun setData(data: List<T>): ZkTable<T> {
        launch {
            preloads.forEach {
                it.job.join()
            }

            build {
                tbody.clear()
                this.currentElement = tbody

                for ((index, row) in data.withIndex()) {
                    + tr {
                        currentElement.dataset["rid"] = getRowId(row).toString()

                        for (column in columns) {
                            + td {
                                column.render(this, index, row)
                            }
                        }

                    }
                }
            }
        }

        return this
    }

    open fun getRowId(row: T): Long {
        if (row is RecordDto<*>) {
            return row.id
        } else {
            throw NotImplementedError("please override ${this::class}.getRowId when not using crud")
        }
    }

    private fun gridTemplateColumns(): String {
        var s = "grid-template-columns:"
        for (column in columns) {
            s += " " + column.gridTemplate()
        }
        return "$s;"
    }

    operator fun KProperty1<T, RecordId<T>>.unaryPlus(): ZkRecordIdColumn<T> {
        val column = ZkRecordIdColumn(this@ZkTable, this)
        columns += column
        return column
    }

    fun actions(): ZkActionsColumn<T> {
        return ZkActionsColumn(this@ZkTable)
    }

    operator fun ZkActionsColumn<T>.unaryPlus(): ZkActionsColumn<T> {
        columns += this
        return this
    }

    operator fun KProperty1<T, String>.unaryPlus(): ZkStringColumn<T> {
        val column = ZkStringColumn(this@ZkTable, this)
        columns += column
        return column
    }

    operator fun KProperty1<T, String?>.unaryPlus(): ZkOptStringColumn<T> {
        val column = ZkOptStringColumn(this@ZkTable, this)
        columns += column
        return column
    }

    operator fun KProperty1<T, Double>.unaryPlus(): ZkDoubleColumn<T> {
        val column = ZkDoubleColumn(this@ZkTable, this)
        columns += column
        return column
    }

    operator fun KProperty1<T, Boolean>.unaryPlus(): ZkBooleanColumn<T> {
        val column = ZkBooleanColumn(this@ZkTable, this)
        columns += column
        return column
    }

    fun custom(builder: ZkCustomColumn<T>.() -> Unit = {}): ZkCustomColumn<T> {
        val column = ZkCustomColumn(this)
        column.builder()
        return column
    }

    operator fun KProperty1<T, Instant>.unaryPlus(): ZkInstantColumn<T> {
        val column = ZkInstantColumn(this@ZkTable, this)
        columns += column
        return column
    }

    operator fun KProperty1<T, Instant?>.unaryPlus(): ZkOptInstantColumn<T> {
        val column = ZkOptInstantColumn(this@ZkTable, this)
        columns += column
        return column
    }

    operator fun ZkCustomColumn<T>.unaryPlus(): ZkCustomColumn<T> {
        columns += this
        return this
    }

}