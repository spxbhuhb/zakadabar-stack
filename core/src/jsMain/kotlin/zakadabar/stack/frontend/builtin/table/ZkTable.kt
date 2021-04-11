/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table

import kotlinx.browser.document
import kotlinx.datetime.Instant
import kotlinx.dom.clear
import org.w3c.dom.*
import org.w3c.dom.events.MouseEvent
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.record.RecordDto
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.pages.ZkCrudTarget
import zakadabar.stack.frontend.builtin.table.columns.*
import zakadabar.stack.frontend.util.*
import zakadabar.stack.frontend.util.Areas
import zakadabar.stack.util.UUID
import kotlin.math.max
import kotlin.math.min
import kotlin.reflect.KProperty1

/**
 * Table for the browser frontend.
 *
 * Override [onConfigure] to set table configuration properties such as title, icons, etc.
 *
 * @property  crud        The [ZkCrudTarget] that is linked with the table. When specified the functions
 *                        of the table (onCreate, onDblClick for example) will use it to open the
 *                        appropriate page.
 * @property  titleBar    When true a title bar is added to the table. Function [addTitleBar] adds the title bar.
 * @property  title       Title to show in the title bar.
 * @property  add         When true a plus icon is added to the title bar. Click on the icon calls [onAddRow].
 * @property  search      When true a search input and icon is added to the title bar. Enter in the search field
 *                        or click on the icon calls [onSearch].
 * @property  export      When true an export icon is added to the title bar. Calls [onExportCsv].
 * @property  columns     Column definitions.
 * @property  preloads    Data load jobs which has to be performed before the table is rendered.
 */
open class ZkTable<T : DtoBase> : ZkElement() {

    // configuration

    var crud: ZkCrudTarget<*>? = null

    var titleBar = true
    var title = ""
    var add = false
    var search = false
    var export = false

    val rowHeight = 42

    open val exportFileName: String
        get() = (if (title.isEmpty()) "content" else title) + ".csv"

    val columns = mutableListOf<ZkColumn<T>>()

    // DOM and children

    lateinit var areas: Areas

    lateinit var titleBarElement: ZkTableTitleBar

    lateinit var tableElement: HTMLTableElement

    private val tbody = document.createElement("tbody") as HTMLTableSectionElement

    private val placeHolderCell = document.createElement("td") as HTMLTableCellElement
    private val placeHolderRow = (document.createElement("tr") as HTMLTableRowElement).also { it.appendChild(placeHolderCell) }

    private var firstShownRow = Int.MAX_VALUE
    private var lastShownRow = - 1

    // state

    val preloads = mutableListOf<ZkTablePreload<*>>()

    var searchText: String? = null

    lateinit var fullData: List<ZkTableRow<T>>

    lateinit var filteredData: List<ZkTableRow<T>>

    // create, render, set data

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

        addTitleBar()

        + div(ZkTableStyles.contentContainer) {

            + div {
                areas = Areas(element.id, ::onAreasChange, buildElement, 0).apply { onCreate() }
            }

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

    override fun onDestroy() {
        areas.onDestroy()
    }

    open fun addTitleBar() {
        if (! titleBar) return

        + ZkTableTitleBar {
            title = this@ZkTable.title
            if (add) onAddRow = ::onAddRow
            if (search) onSearch = ::onSearch
            if (export) onExportCsv = ::onExportCsv
        }
    }

    fun <RT : Any> preload(loader: suspend () -> RT) = ZkTablePreload(loader)

    fun setData(data: List<T>): ZkTable<T> {
        io {
            preloads.forEach {
                it.job.join()
            }
            fullData = data.map { ZkTableRow(it) }
            filter()
            render()
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

    open fun render() {
        build {
            tbody.clear()
            this.buildElement = tbody

            firstShownRow = Int.MAX_VALUE
            lastShownRow = - 1

            areas.adjustAreas((filteredData.size * rowHeight).toFloat())

            + placeHolderRow

            onAreasChange()
        }
    }

    open fun onAreasChange() {

        // First and last row to show on the screen. Added [min] to lastRowIndex
        // because when the last area is shown the index would equal to the number
        // of rows (generates IndexOutOfBounds).

        val firstRowIndex = (areas.start / rowHeight).toInt()
        val lastRowIndex = min((areas.end / rowHeight).toInt(), filteredData.size - 1)

        // The placeholder fills the gap between the start of the table and
        // the first row shown.

        placeHolderCell.style.cssText = """
            height: ${firstRowIndex * rowHeight}px;
            grid-column: 1 / span ${columns.size};
            background: transparent;
            padding: 0;
            border: 0; 
        """.trimIndent()

        // Remove all rows that became hidden.

        for (index in firstShownRow..lastShownRow) {
            if (index < firstRowIndex || index > lastRowIndex) {
                filteredData[index].element?.remove()
            }
        }

        // Add all new rows.

        val anchor = placeHolderRow.nextSibling

        for (index in firstRowIndex..lastRowIndex) {
            val element = getRowElement(index, filteredData[index])

            if (index < firstShownRow) {
                tbody.insertBefore(element, anchor)
                continue
            }

            if (index > lastShownRow) {
                tbody.appendChild(element)
                continue
            }
        }

        firstShownRow = firstRowIndex
        lastShownRow = lastRowIndex
    }

    /**
     * Get the [HTMLTableRowElement] that belongs to the given index in filtered
     * data.
     *
     * Creates a [ZkTableRow] if missing, creates an [HTMLTableRowElement]
     * and renders the row is missing.
     */
    open fun getRowElement(index: Int, row: ZkTableRow<T>): HTMLTableRowElement {

        var element = row.element

        if (element == null) {

            element = tr {
                buildElement.dataset["rid"] = getRowId(row.data)

                for (column in columns) {
                    + td {
                        column.render(this, index, row.data)
                    }
                }
            }

            row.element = element
        }

        return element
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

    // extended functions

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
        println("searchText = $searchText")
        filter()
        println("filteredData.size = ${filteredData.size}")
        render()
    }

    open fun onExportCsv() {
        val lines = mutableListOf<String>()

        fullData.forEach { row ->
            val fields = mutableListOf<String>()
            columns.forEach { if (it.exportable) fields += it.exportCsv(row.data) }
            lines += fields.joinToString(",")
        }

        val csv = lines.joinToString("\n")

        downloadCsv(exportFileName, csv)
    }

    /**
     * Applies all filters on the table rows.
     */
    open fun filter() {

        if (searchText == null) {
            filteredData = fullData
            return
        }

        filteredData = fullData.filter { filterRow(it.data, searchText) }
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

    // column add functions for properties

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

    operator fun KProperty1<T, UUID>.unaryPlus(): ZkUuidColumn<T> {
        val column = ZkUuidColumn(this@ZkTable, this)
        columns += column
        return column
    }

    operator fun KProperty1<T, UUID?>.unaryPlus(): ZkOptUuidColumn<T> {
        val column = ZkOptUuidColumn(this@ZkTable, this)
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