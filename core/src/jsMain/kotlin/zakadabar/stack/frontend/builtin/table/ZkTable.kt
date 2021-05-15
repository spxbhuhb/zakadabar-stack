/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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
import zakadabar.stack.data.record.StringRecordId
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementState
import zakadabar.stack.frontend.builtin.pages.ZkCrudTarget
import zakadabar.stack.frontend.builtin.table.actions.ZkAddRowAction
import zakadabar.stack.frontend.builtin.table.actions.ZkExportCsvAction
import zakadabar.stack.frontend.builtin.table.actions.ZkSearchAction
import zakadabar.stack.frontend.builtin.table.columns.*
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitle
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitleProvider
import zakadabar.stack.frontend.util.*
import zakadabar.stack.util.UUID
import kotlin.math.min
import kotlin.reflect.KProperty1

/**
 * Table for the browser frontend.
 *
 * Override [onConfigure] to set table configuration properties such as title, icons, etc.
 *
 * @property  crud          The [ZkCrudTarget] that is linked with the table. When specified the functions
 *                          of the table (onCreate, onDblClick for example) will use it to open the
 *                          appropriate page.
 * @property  setAppTitle      When true (default) the app title bar is set for the table. Function [setAppTitleBar] adds the title bar.
 * @property  titleText     Title text to show in the title bar. Used when [titleElement] is not set.
 * @property  titleElement         The element of the title.
 * @property  add           When true a plus icon is added to the title bar. Click on the icon calls [onAddRow].
 * @property  search        When true a search input and icon is added to the title bar. Enter in the search field
 *                          or click on the icon calls [onSearch].
 * @property  export        When true an export icon is added to the title bar. Calls [onExportCsv].
 * @property  rowHeight     Height (in pixels) of one table row, used when calculating row positions for virtualization.
 * @property  columns       Column definitions.
 * @property  preloads      Data load jobs which has to be performed before the table is rendered.
 */
open class ZkTable<T : DtoBase> : ZkElement(), ZkAppTitleProvider {

    // -------------------------------------------------------------------------
    //  Configuration -- meant to set by onConfigure
    // -------------------------------------------------------------------------

    var crud: ZkCrudTarget<*>? = null

    override var setAppTitle = true
    override var titleText: String? = null
    override var titleElement: ZkAppTitle? = null

    var add = false
    var search = false
    var export = false

    open var rowHeight = 42

    val columns = mutableListOf<ZkColumn<T>>()

    open val exportFileName: String
        get() {
            val titleText = titleElement?.text
            return (if (titleText.isNullOrEmpty()) "content" else titleText) + ".csv"
        }

    // -------------------------------------------------------------------------
    //  DOM
    // -------------------------------------------------------------------------

    private lateinit var areas: Areas // areas for intersection observer

    lateinit var tableElement: HTMLTableElement

    private val tbody = document.createElement("tbody") as HTMLTableSectionElement

    // gap before the first row, used to virtualize rows

    private val placeHolderCell = document.createElement("td") as HTMLTableCellElement
    private val placeHolderRow = (document.createElement("tr") as HTMLTableRowElement).also { it.appendChild(placeHolderCell) }

    private var firstShownRow = Int.MAX_VALUE
    private var lastShownRow = - 1

    // -------------------------------------------------------------------------
    //  State -- data of the table, search text, preloaded data
    // -------------------------------------------------------------------------

    lateinit var fullData: List<ZkTableRow<T>>

    open lateinit var filteredData: List<ZkTableRow<T>>

    val preloads = mutableListOf<ZkTablePreload<*>>()

    open var searchText: String? = null

    // -------------------------------------------------------------------------
    //  Lifecycle functions
    // -------------------------------------------------------------------------

    /**
     * Called by [onCreate] to configure the table before building it.
     * This is the place to add columns, switch on and off features.
     */
    open fun onConfigure() {

    }

    override fun onCreate() {
        onConfigure()

        className = zkTableStyles.outerContainer
        classList += zkTableStyles.noSelect

        + div(zkTableStyles.contentContainer) {

            + div {
                areas = Areas(element.id, ::onAreasChange, buildPoint, 0).apply { onCreate() }
            }

            tableElement = table(zkTableStyles.table) {

                buildPoint.style.cssText = inlineCss()
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

    override fun onResume() {
        super.onResume()

        setAppTitleBar()

        // this means that setData has been called before onResume
        if (::fullData.isInitialized) {
            render()
        }
    }

    override fun onDestroy() {
        areas.onDestroy()
    }

    override fun setAppTitleBar(contextElements: List<ZkElement>) {
        if (! setAppTitle) return

        val actions = mutableListOf<ZkElement>()
        if (add) actions += ZkAddRowAction(::onAddRow)
        if (export) actions += ZkExportCsvAction(::onExportCsv)
        if (search) actions += ZkSearchAction(::onSearch)

        super.setAppTitleBar(actions)
    }

    // -------------------------------------------------------------------------
    //  Data setter, preload
    // -------------------------------------------------------------------------

    fun <RT : Any> preload(loader: suspend () -> RT) = ZkTablePreload(loader)

    fun setData(data: List<T>): ZkTable<T> {
        io {
            preloads.forEach {
                it.job.join()
            }

            fullData = data.map { ZkTableRow(it) }
            filter()

            // this means that onResume has been called before setData
            if (lifeCycleState == ZkElementState.Resumed) {
                render()
            }
        }

        return this
    }

    // -------------------------------------------------------------------------
    //  Rendering, intersection observer callback
    // -------------------------------------------------------------------------

    private fun inlineCss() = """
        grid-template-columns: ${columns.joinToString(" ") { it.gridTemplate() }};
    """.trimIndent()

    open fun render() {
        build {
            tbody.clear()
            this.buildPoint = tbody

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
                buildPoint.dataset["rid"] = getRowId(row.data)

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

    // -------------------------------------------------------------------------
    //  API functions, intended for override
    // -------------------------------------------------------------------------

    /**
     * Get a unique if for the given row. The id of the row is used by actions and is
     * passed to row based functions such as [onDblClick].
     *
     * Default implementation uses [RecordDto.id] when rows are record dto instances,
     * otherwise it throws [NotImplementedError].
     */
    open fun getRowId(row: T): String {
        if (row is RecordDto<*>) {
            return row.id.toString()
        } else {
            throw NotImplementedError("please override ${this::class}.getRowId when not using crud")
        }
    }

    /**
     * Add a new row to the table. Default implementation calls [ZkCrudTarget.openCreate]
     * when there is a crud, does nothing otherwise.
     */
    open fun onAddRow() {
        crud?.openCreate()
    }

    /**
     * Handles double click on a row. Default implementation calls [ZkCrudTarget.openUpdate]
     * when there is a crud, does nothing otherwise.
     *
     * @param  id  Id of the row as given by [getRowId].
     */
    open fun onDblClick(id: String) {
        val recordId: RecordId<*> = StringRecordId<DtoBase>(id)
        crud?.openUpdate(recordId)
    }

    /**
     * Performs a search on the table, showing only rows that contain [text].
     *
     * Default implementation:
     *
     * * sets [searchText] to [text]
     * * calls [filter]
     * * calls [render]
     */
    open fun onSearch(text: String) {
        searchText = if (text.isEmpty()) null else text
        filter()
        render()
    }

    /**
     * Exports the table to CSV.
     *
     * Default implementation:
     *
     * * exports all the data, not the filtered state
     * * calls [ZkColumn.exportCsv] for each row to build the csv line
     * * pops a download in the browser with the file name set to [exportFileName]
     */
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

    // -------------------------------------------------------------------------
    //  Column builders
    // -------------------------------------------------------------------------

    operator fun <IT> KProperty1<T, RecordId<IT>>.unaryPlus(): ZkRecordIdColumn<T, IT> {
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

    operator fun KProperty1<T, Int>.unaryPlus(): ZkIntColumn<T> {
        val column = ZkIntColumn(this@ZkTable, this)
        columns += column
        return column
    }

    operator fun KProperty1<T, Int?>.unaryPlus(): ZkOptIntColumn<T> {
        val column = ZkOptIntColumn(this@ZkTable, this)
        columns += column
        return column
    }

    operator fun KProperty1<T, Double>.unaryPlus(): ZkDoubleColumn<T> {
        val column = ZkDoubleColumn(this@ZkTable, this)
        columns += column
        return column
    }

    operator fun KProperty1<T, Double?>.unaryPlus(): ZkOptDoubleColumn<T> {
        val column = ZkOptDoubleColumn(this@ZkTable, this)
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