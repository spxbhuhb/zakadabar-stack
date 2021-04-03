/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table

import kotlinx.browser.document
import kotlinx.datetime.Instant
import kotlinx.dom.clear
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLTableElement
import org.w3c.dom.HTMLTableSectionElement
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.set
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.pages.ZkCrudTarget
import zakadabar.stack.frontend.builtin.table.columns.*
import zakadabar.stack.frontend.util.getDatasetEntry
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.plusAssign
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
 * @property  crud        The [ZkCrudTarget] that is linked with the table. When specified the functions
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

    var add: Boolean = false
    var search: Boolean = false

    var crud: ZkCrudTarget<*>? = null

    var titleBar: ZkTableTitleBar? = null

    val columns = mutableListOf<ZkColumn<T>>()

    val preloads = mutableListOf<ZkTablePreload<*>>()

    var searchText: String? = null

    lateinit var fullData: List<T>

    lateinit var filteredData: List<T>

    lateinit var tableElement: HTMLTableElement

    private val tbody = document.createElement("tbody") as HTMLTableSectionElement

    /**
     * Called by [onCreate] to configure the table before building it.
     * This is the place to add columns, switch on and off features.
     */
    open fun onConfigure() {

    }

    override fun onCreate() {
        onConfigure()

        className = ZkTableStyles.outerContainer
        classList += ZkTableStyles.noSelect

        buildTitleBar()?.let { + it }

        + div(ZkTableStyles.contentContainer) {

            tableElement = table(ZkTableStyles.table) {
                buildElement.style.cssText = gridTemplateColumns()
                + thead {
                    columns.forEach { + it }
                }
                + tbody
            }

            + tableElement
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

            onDblClick(rid)
        }

        on("click") { event ->
            event as MouseEvent
            event.preventDefault()

            val target = event.target as? HTMLElement ?: return@on
            val rid = target.getDatasetEntry("rid") ?: return@on
            val action = target.getDatasetEntry("action") ?: return@on

            if (action == "update") {
                onDblClick(rid)
            }
        }
    }

    private fun buildTitleBar(): ZkTableTitleBar? {

        if (titleBar == null && (title != null || add || search || crud != null)) {
            titleBar = ZkTableTitleBar {
                title = this@ZkTable.title ?: ""
                if (add) onAddRow = ::onAddRow
                if (search) onSearch = ::onSearch
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
            fullData = data
            filter()
            render()
        }

        return this
    }

    /**
     * Applies all filters on the table rows.
     */
    open fun filter() {

        if (searchText == null) {
            filteredData = fullData
            return
        }

        filteredData = fullData.filter { filterRow(it, searchText) }
    }

    /**
     * Applies the filters to one row to decide if that row should be
     * present in the filtered table.
     */
    open fun filterRow(row: T, text: String?): Boolean {
        columns.forEach {
            if (it.matches(row, searchText)) return true
        }
        return false
    }

    open fun render() {
        build {
            tbody.clear()
            this.buildElement = tbody

            for ((index, row) in filteredData.withIndex()) {
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

    open fun onAddRow() {
        crud?.openCreate()
    }

    /**
     * Handles double click on a row. Default implementation calls openUpdate
     * of the crud if there is a crud.
     *
     * @param  id  Id of the row.
     */
    open fun onDblClick(id: String) {
        crud?.openUpdate(id.toLong())
    }

    open fun onSearch(text: String) {
        searchText = if (text.isEmpty()) null else text
        filter()
        render()
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