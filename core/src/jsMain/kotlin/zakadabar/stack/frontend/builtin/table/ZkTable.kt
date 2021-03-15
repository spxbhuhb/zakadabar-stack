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
import zakadabar.stack.frontend.builtin.ZkCrud
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.table.columns.*
import zakadabar.stack.frontend.util.getDatasetEntry
import zakadabar.stack.frontend.util.io
import kotlin.reflect.KProperty1

/**
 * Provides functions to build tables for the browser frontend.
 *
 * Title bar is the bar above the table that contains the title and possibly functions such
 * as create new item, search in the table, etc.
 *
 * The title bar is optional. One is added automatically if one of [title], [onResume] or [onSearch]
 * is specified. You can override the default by setting the [titleBar] property before [onResume] runs.
 *
 * @property  title       Title to show in the title bar.
 * @property  crud        The [ZkCrud] that is linked with the table. When specified the functions
 *                        of the table (onCreate, onDblClick for example) will use it to open the
 *                        appropriate page.
 * @property  onAddRow    Called when the user clicks the plus button in the title bar. This function
 *                        has preference over [crud].
 * @property  onDblClick  Called when the user double clicks on a row. Parameter is the id of the
 *                        row. When [crud] is set this will be the record id. Otherwise it will be
 *                        the value returned by [getRowId]. This function has preference over [crud].
 * @property  titleBar    Title bar of the table. Set this to override the default title bar.
 * @property  columns     Column definitions.
 * @property  preloads    Data load jobs which has to be performed before the table is rendered.
 */
open class ZkTable<T : DtoBase> : ZkElement() {

    var title: String? = null

    var crud: ZkCrud<*>? = null

    var onAddRow: (() -> Unit)? = null
    var onDblClick: ((id: String) -> Unit)? = null
    var onSearch: ((searchText: String) -> Unit)? = null

    var titleBar: ZkTableTitleBar? = null

    val columns = mutableListOf<ZkColumn<T>>()

    val preloads = mutableListOf<ZkTablePreload<*>>()

    private val tbody = document.createElement("tbody") as HTMLTableSectionElement

    override fun onCreate() {
        className = ZkTableStyles.outerContainer

        buildTitleBar()?.let { + it }

        + div(ZkTableStyles.contentContainer) {

            + table(ZkTableStyles.table) {
                buildElement.style.cssText = gridTemplateColumns()
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
            val rid = target.getDatasetEntry("rid") ?: return@on

            if (onDblClick != null) {
                onDblClick?.invoke(rid)
            } else if (crud != null) {
                crud?.openUpdate(rid.toLong())
            }
        }

        on("click") { event ->
            event as MouseEvent
            event.preventDefault()

            val target = event.target as? HTMLElement ?: return@on
            val rid = target.getDatasetEntry("rid") ?: return@on
            val action = target.getDatasetEntry("action") ?: return@on

            if (action == "update") {
                if (onDblClick != null) {
                    onDblClick?.invoke(rid)
                } else if (crud != null) {
                    crud?.openUpdate(rid.toLong())
                }
            }
        }
    }

    private fun buildTitleBar(): ZkTableTitleBar? {

        if (titleBar == null && (title != null || onAddRow != null || onSearch != null || crud != null)) {
            titleBar = ZkTableTitleBar {
                title = this@ZkTable.title
                if (this@ZkTable.onAddRow != null) {
                    onAddRow = this@ZkTable.onAddRow !!
                } else if (this@ZkTable.crud != null) {
                    onAddRow = { this@ZkTable.crud !!.openCreate() }
                }
                this@ZkTable.onSearch?.let { onSearch = it }
            }
        }

        return titleBar
    }

    fun <RT : Any> preload(loader: suspend () -> RT) = ZkTablePreload(loader)

    fun setData(data: List<T>): ZkTable<T> {
        io {
            preloads.forEach {
                it.job.join()
            }

            build {
                tbody.clear()
                this.buildElement = tbody

                for ((index, row) in data.withIndex()) {
                    + tr {
                        buildElement.dataset["rid"] = getRowId(row)

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

    /**
     * Get a unique if for the given row. The id of the row is used by actions and is
     * passed to row based functions such as [onDblClick].
     */
    open fun getRowId(row: T): String {
        if (row is RecordDto<*>) {
            return row.id.toString()
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

    inline operator fun <reified E : Enum<E>> KProperty1<T, E>.unaryPlus(): ZkEnumColumn<T, E> {
        val column = ZkEnumColumn(this@ZkTable, this)
        columns += column
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

    fun custom(builder: ZkCustomColumn<T>.() -> Unit = {}): ZkCustomColumn<T> {
        val column = ZkCustomColumn(this)
        column.builder()
        return column
    }

    fun actions(): ZkActionsColumn<T> {
        return ZkActionsColumn(this@ZkTable)
    }

}