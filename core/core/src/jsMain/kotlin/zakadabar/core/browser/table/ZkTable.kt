/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.dom.clear
import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.ZkElementState
import zakadabar.core.browser.counterbar.ZkCounterBar
import zakadabar.core.browser.crud.ZkCrud
import zakadabar.core.browser.crud.ZkCrudTarget
import zakadabar.core.browser.field.ZkFieldContext
import zakadabar.core.browser.table.columns.*
import zakadabar.core.browser.table.header.ZkAddRowAction
import zakadabar.core.browser.table.header.ZkExportCsvAction
import zakadabar.core.browser.table.header.ZkSearchAction
import zakadabar.core.browser.titlebar.ZkAppTitle
import zakadabar.core.browser.titlebar.ZkAppTitleProvider
import zakadabar.core.browser.titlebar.ZkLocalTitleBar
import zakadabar.core.browser.titlebar.ZkLocalTitleProvider
import zakadabar.core.browser.util.Areas
import zakadabar.core.browser.util.downloadCsv
import zakadabar.core.browser.util.getDatasetEntry
import zakadabar.core.browser.util.io
import zakadabar.core.data.BaseBo
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityId
import zakadabar.core.data.QueryBo
import zakadabar.core.resource.localizedStrings
import zakadabar.core.schema.BoSchema
import zakadabar.core.util.PublicApi
import zakadabar.core.util.UUID
import kotlin.math.min
import kotlin.reflect.KProperty1

/**
 * Table for the browser frontend.
 *
 * Override [onConfigure] to set table configuration properties such as title, icons, etc.
 *
 * @property  crud            The [ZkCrudTarget] that is linked with the table. When specified the functions
 *                            of the table (onCreate, onDblClick for example) will use it to open the
 *                            appropriate page.
 * @property  query           When initialized, the table automatically executes this query during onResume to fill
 *                            the table with data.
 * @property  setAppTitle     When true (default) the app title bar is set for the table. Function [setAppTitleBar] adds the title bar.
 * @property  addLocalTitle   When true, add a local title bar. Default is false.
 * @property  titleText       Title text to show in the title bar. Used when [titleElement] is not set.
 * @property  titleElement    The element of the title.
 * @property  add             When true a plus icon is added to the title bar. Click on the icon calls [onAddRow].
 * @property  search          When true a search input and icon is added to the title bar. Enter in the search field
 *                            or click on the icon calls [onSearch].
 * @property  oneClick        When true single clicks are treated as double clicks (call onDblClick).
 * @property  export          When true an export icon is added to the title bar. Calls [onExportCsv].
 * @property  exportFiltered  When true, the table exports only rows matching the current filter. Default is false.
 * @property  exportHeaders    When true, CSV export contains header row with header labels. Default is false.
 * @property  rowHeight       Height (in pixels) of one table row, used when calculating row positions for virtualization.
 * @property  columns         Column definitions.
 * @property  preloads        Data load jobs which has to be performed before the table is rendered.
 */
open class ZkTable<T : BaseBo> : ZkElement(), ZkAppTitleProvider, ZkLocalTitleProvider, ZkFieldContext {

    // -------------------------------------------------------------------------
    //  Configuration -- meant to set by onConfigure
    // -------------------------------------------------------------------------

    var crud: ZkCrud<T>? = null
        set(value) {
            check(field == null || field === value) { "Table crud is changed after first assignment. This happens when you use a table with ZkInlineCrud, but you set the crud property in onConfigure. Remove the set from onConfigure." }
            field = value
        }

    override var setAppTitle = true
    override var addLocalTitle = false
    override var titleText: String? = null
    override var titleElement: ZkAppTitle? = null

    var add = false
    var search = false

    var export = false
    var exportFiltered = false
    var exportHeaders = false

    var oneClick = false

    var firstOnResume = true
    var runQueryOnResume = true

    var counter = false

    open val rowHeight
        get() = styles.rowHeight

    val columns = mutableListOf<ZkColumn<T>>()

    var query: QueryBo<List<T>>? = null

    open val exportFileName: String
        get() {
            val titleText = titleElement?.text
            return (if (titleText.isNullOrEmpty()) "content" else titleText) + ".csv"
        }

    // -------------------------------------------------------------------------
    //  ZkFieldBackend
    // -------------------------------------------------------------------------

    override val readOnly = false

    override val useShadow = false

    override val schema = BoSchema.NO_VALIDATION

    override val addLabel = false

    override var styles = zkTableStyles

    override fun validate() {}

    override fun submit() {}

    // -------------------------------------------------------------------------
    //  DOM
    // -------------------------------------------------------------------------

    private lateinit var areas: Areas // areas for intersection observer

    lateinit var contentContainer: ZkElement

    lateinit var tableElement: HTMLTableElement

    val tbody = document.createElement("tbody") as HTMLTableSectionElement

    // gap before the first row, used to virtualize rows

    val placeHolderCell = document.createElement("td") as HTMLTableCellElement
    val placeHolderRow = (document.createElement("tr") as HTMLTableRowElement).also { it.appendChild(placeHolderCell) }

    var contentScrollTop: Double = 0.0
    var contentScrollLeft: Double = 0.0

    protected var firstShownRow = Int.MAX_VALUE
    protected var lastShownRow = - 1

    // -------------------------------------------------------------------------
    //  State -- data of the table, search text, preloaded data
    // -------------------------------------------------------------------------

    lateinit var fullData: List<ZkTableRow<T>>

    open lateinit var filteredData: List<ZkTableRow<T>>

    val preloads = mutableListOf<ZkTablePreload<*>>()

    open var searchText: String? = null

    open var counterBar = ZkCounterBar("")
    open var allCount: Int? = null // if the full data size of the very first query is not equal to all count, it is possible to set here
    open var needToSetAllCounter = true

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

        + styles.outerContainer

        + buildLocalTitleBar()

        + zke(styles.contentContainer) {

            + div {
                areas = Areas(element.id, ::onAreasChange, buildPoint, 0).apply { onCreate() }
            }

            + table(styles.table) {
                buildPoint.style.cssText = inlineCss()
                + thead {
                    + styles.noSelect
                    columns.forEach { + it }
                }
                + tbody
            }.also {
                tableElement = it
            }

            on("scroll") {
                contentScrollTop = this.element.scrollTop
                contentScrollLeft = this.element.scrollLeft
            }

        }.also {
            contentContainer = it
        }

        on("mousedown", ::onMouseDown)
        on("dblclick", ::onDblClick)
        on("click", ::onClick)

        if (counter) + counterBar
    }

    override fun onResume() {
        super.onResume()

        setAppTitleBar()

        val query = this.query

        when {
            query != null && (runQueryOnResume || firstOnResume) -> {
                setData(emptyList())
                io {
                    setData(query.execute()) // calls render
                }
            }

            // this means that setData has been called before onResume
            ::fullData.isInitialized && firstOnResume -> {
                render()
            }
        }

        if (! firstOnResume) {
            window.requestAnimationFrame {
                contentContainer.element.scrollTo(contentScrollLeft, contentScrollTop)
            }
        }

        firstOnResume = false

    }

    override fun onDestroy() {
        areas.onDestroy()
    }

    override fun setAppTitleBar(contextElements: List<ZkElement>) {
        if (! setAppTitle) return

        super.setAppTitleBar(titleActions())
    }

    override fun buildLocalTitleBar(contextElements: List<ZkElement>): ZkElement? =
        if (addLocalTitle) {
            ZkLocalTitleBar(titleText ?: localizedStrings.getNormalized(this::class.simpleName ?: ""), titleActions() + contextElements)
        } else {
            null
        }

    protected fun titleActions(): List<ZkElement> {

        val actions = mutableListOf<ZkElement>()

        if (add) actions += ZkAddRowAction(::onAddRow)
        if (export) actions += ZkExportCsvAction(::onExportCsv)
        if (search) actions += ZkSearchAction(searchText ?: "", ::onSearch)

        return actions
    }

    open fun setCounter() {

        if (needToSetAllCounter && allCount == null && ::fullData.isInitialized && fullData.isNotEmpty()){
            allCount = fullData.size
            needToSetAllCounter = false
        }

        val all = allCount ?: ""
        val count = if (::filteredData.isInitialized) filteredData.size.toString() else all

        counterBar.text = "${localizedStrings.counterTitle} $count/$all"
        counterBar.onCreate()

    }

    // -------------------------------------------------------------------------
    //  Event Handlers
    // -------------------------------------------------------------------------

    open fun getRowId(event: Event): String? {
        if (event !is MouseEvent) return null

        val target = event.target
        if (target !is HTMLElement) return null

        return target.getDatasetEntry("rid")
    }

    open fun onClick(event: Event) {
        if (oneClick) getRowId(event)?.let { onDblClick(it) }
    }

    /**
     * Prevent text selection on double click.
     */
    open fun onMouseDown(event: Event) {
        event as MouseEvent
        if (event.detail > 1) {
            event.preventDefault()
        }
    }

    open fun onDblClick(event: Event) {
        event.preventDefault()
        getRowId(event)?.let { onDblClick(it) }
    }

    // -------------------------------------------------------------------------
    //  Data setter, preload
    // -------------------------------------------------------------------------

    fun <RT : Any> preload(loader: suspend () -> RT) = ZkTablePreload(loader)

    /**
     * Set data of the table. Asynchronous, waits for the preloads
     * to finish before the data is actually set.
     *
     * @param  data  The data to set.
     */
    fun setData(data: List<T>): ZkTable<T> {
        io {
            preloads.forEach {
                it.job.join()
            }

            fullData = data.map { ZkTableRow(it) }

            columns.forEach { it.onTableSetData() }

            filter()

            // this means that onResume has been called before setData
            if (lifeCycleState == ZkElementState.Resumed) {
                render()
            }
        }

        return this
    }

    /**
     * Execute the given query and set the table data to its
     * result. Asynchronous, waits for the preloads to finish
     * before the data is actually set.
     *
     * Calls the list version of [setData] with the query result.
     *
     * @param  query  The query to execute
     */
    fun setData(query: QueryBo<List<T>>) {
        io {
            setData(query.execute())
        }
    }

    // -------------------------------------------------------------------------
    //  Rendering, intersection observer callback
    // -------------------------------------------------------------------------

    open fun inlineCss() = """
        grid-template-columns: ${columns.joinToString(" ") { it.gridTemplate() }};
    """.trimIndent()

    open fun render() {
        build {
            tbody.clear()

            // clear all cached row renders as sorting calls render and
            // it may change the column index

            for (row in fullData) {
                row.element = null
            }

            this.buildPoint = tbody

            firstShownRow = Int.MAX_VALUE
            lastShownRow = - 1

            val height = (filteredData.size + 1) * rowHeight
            areas.adjustAreas(height.toFloat())
            areas.start = 0f
            areas.end = areas.areaHeight * areas.activeAreas.size
            areas.element.scrollTop = 0.0

            + placeHolderRow

            onAreasChange()

            if (counter) setCounter()
        }
    }

    /**
     * Redraws the currently shown rows of the table.
     * Deletes all cached row renders.
     * Sets scroll position to the latest known value
     */
    fun redraw(): ZkTable<T> {
        // clear the body of the table

        tbody.clear()
        + placeHolderRow

        // clear all cached renders

        for (row in fullData) {
            row.element = null
        }

        for (index in firstShownRow..lastShownRow) {
            tbody.appendChild(
                getRowElement(index, filteredData[index])
            )
        }

        window.requestAnimationFrame {
            contentContainer.element.scrollTo(contentScrollLeft, contentScrollTop)
        }

        return this
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
     * Default implementation uses [EntityBo.id] when rows are record bo instances,
     * otherwise it throws [NotImplementedError].
     */
    open fun getRowId(row: T): String {
        if (row is EntityBo<*>) {
            return row.id.toString()
        } else {
            throw NotImplementedError("please override ${this::class}.getRowId when not using crud")
        }
    }

    /**
     * Get the data of a given row by row ID.
     */
    open fun getRowData(id: String): T =
        fullData.first { getRowId(it.data) == id }.data

    /**
     * Set the data of a given row by row ID. Does not update the UI,
     * call [redraw] for that.
     */
    open fun setRowData(data: T, optional: Boolean = false) {
        val id = getRowId(data)
        if (optional) {
            fullData.firstOrNull { getRowId(it.data) == id }?.data = data
        } else {
            fullData.first { getRowId(it.data) == id }.data = data
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
        crud?.openUpdate(EntityId(id))
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
        searchText = text.ifEmpty { null }
        filter()
        render()
    }

    /**
     * Exports the table to CSV.
     *
     * Default implementation:
     *
     * * exports all the data, not the filtered state, can be changed with [exportFiltered]
     * * calls [ZkColumn.exportCsv] for each row to build the csv line
     * * pops a download in the browser with the file name set to [exportFileName]
     */
    open fun onExportCsv() {
        val lines = mutableListOf<String>()

        val data = if (exportFiltered) filteredData else fullData

        if (exportHeaders) {
            val fields = mutableListOf<String>()
            columns.forEach { if (it.exportable) fields += it.exportCsvHeader() }
            lines += fields.joinToString(";")
        }

        data.forEach { row ->
            val fields = mutableListOf<String>()
            columns.forEach { if (it.exportable) fields += it.exportCsv(row.data) }
            lines += fields.joinToString(";")
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

        val lc = searchText?.lowercase()

        filteredData = fullData.filter { filterRow(it.data, lc) }
    }

    /**
     * Applies the filters to one row to decide if that row should be
     * present in the filtered table.
     */
    open fun filterRow(row: T, text: String?): Boolean {
        columns.forEach {
            if (it.matches(row, text)) return true
        }
        return false
    }

    // -------------------------------------------------------------------------
    //  Column builders
    // -------------------------------------------------------------------------

    inline fun <reified CT : ZkColumn<T>> CT.add(prop: KProperty1<T, *>): CT {
        label = localizedStrings.getNormalized(prop.name)
        columns += this
        return this
    }

    operator fun ZkActionsColumn<T>.unaryPlus(): ZkActionsColumn<T> {
        columns += this
        return this
    }

    fun <IT> entityId(getter: T.() -> EntityId<IT>): ZkEntityIdColumnV2<T, IT> =
        ZkEntityIdColumnV2(this@ZkTable, getter)

    operator fun <IT> KProperty1<T, EntityId<IT>>.unaryPlus(): ZkEntityIdColumnV2<T, IT> =
        ZkEntityIdColumnV2(this@ZkTable) { this.get(it) }
            .add(this)

    fun <IT> optEntityId(getter: T.() -> EntityId<IT>?): ZkOptEntityIdColumnV2<T, IT> =
        ZkOptEntityIdColumnV2(this@ZkTable, getter)

    operator fun <IT> KProperty1<T, EntityId<IT>?>.unaryPlus(): ZkOptEntityIdColumnV2<T, IT> =
        ZkOptEntityIdColumnV2(this@ZkTable) { this.get(it) }
            .add(this)

    fun string(getter: T.() -> String): ZkStringColumnV2<T> =
        ZkStringColumnV2(this@ZkTable, getter)

    operator fun KProperty1<T, String>.unaryPlus(): ZkStringColumnV2<T> =
        ZkStringColumnV2(this@ZkTable) { row -> this.get(row) }
            .add(this)

    @PublicApi
    fun optString(getter: T.() -> String?): ZkOptStringColumnV2<T> =
        ZkOptStringColumnV2(this@ZkTable, getter)

    operator fun KProperty1<T, String?>.unaryPlus(): ZkOptStringColumnV2<T> =
        ZkOptStringColumnV2(this@ZkTable) { row -> this.get(row) }
            .add(this)

    fun int(getter: T.() -> Int): ZkIntColumnV2<T> =
        ZkIntColumnV2(this@ZkTable, getter)

    operator fun KProperty1<T, Int>.unaryPlus(): ZkIntColumnV2<T> =
        ZkIntColumnV2(this@ZkTable) { row -> this.get(row) }
            .add(this)

    @PublicApi
    fun optInt(getter: T.() -> Int?): ZkOptIntColumnV2<T> =
        ZkOptIntColumnV2(this@ZkTable, getter)

    operator fun KProperty1<T, Int?>.unaryPlus(): ZkOptIntColumnV2<T> =
        ZkOptIntColumnV2(this@ZkTable) { row -> this.get(row) }
            .add(this)

    fun long(getter: T.() -> Long): ZkLongColumnV2<T> =
        ZkLongColumnV2(this@ZkTable, getter)

    operator fun KProperty1<T, Long>.unaryPlus(): ZkLongColumnV2<T> =
        ZkLongColumnV2(this@ZkTable) { row -> this.get(row) }
            .add(this)

    @PublicApi
    fun optLong(getter: T.() -> Long?): ZkOptLongColumnV2<T> =
        ZkOptLongColumnV2(this@ZkTable, getter)

    operator fun KProperty1<T, Long?>.unaryPlus(): ZkOptLongColumnV2<T> =
        ZkOptLongColumnV2(this@ZkTable) { row -> this.get(row) }
            .add(this)

    fun double(getter: T.() -> Double): ZkDoubleColumnV2<T> =
        ZkDoubleColumnV2(this@ZkTable, getter)

    operator fun KProperty1<T, Double>.unaryPlus(): ZkDoubleColumnV2<T> =
        ZkDoubleColumnV2(this@ZkTable) { row -> this.get(row) }
            .add(this)

    @PublicApi
    fun optDouble(getter: T.() -> Double?): ZkOptDoubleColumnV2<T> =
        ZkOptDoubleColumnV2(this@ZkTable, getter)

    operator fun KProperty1<T, Double?>.unaryPlus(): ZkOptDoubleColumnV2<T> =
        ZkOptDoubleColumnV2(this@ZkTable) { row -> this.get(row) }
            .add(this)

    fun boolean(getter: T.() -> Boolean): ZkBooleanColumnV2<T> =
        ZkBooleanColumnV2(this@ZkTable, getter)

    operator fun KProperty1<T, Boolean>.unaryPlus(): ZkBooleanColumnV2<T> =
        ZkBooleanColumnV2(this@ZkTable) { row -> this.get(row) }
            .add(this)

    fun optBoolean(getter: T.() -> Boolean?): ZkOptBooleanColumnV2<T> =
        ZkOptBooleanColumnV2(this@ZkTable, getter)

    operator fun KProperty1<T, Boolean?>.unaryPlus(): ZkOptBooleanColumnV2<T> =
        ZkOptBooleanColumnV2(this@ZkTable) { row -> this.get(row) }
            .add(this)

    fun <E : Enum<E>> enum(getter: T.() -> E): ZkEnumColumnV2<T, E> =
        ZkEnumColumnV2(this@ZkTable, getter)

    inline operator fun <reified E : Enum<E>> KProperty1<T, E>.unaryPlus(): ZkEnumColumnV2<T, E> =
        ZkEnumColumnV2(this@ZkTable) { row -> this.get(row) }
            .add(this)

    @PublicApi
    @JsName("columnOptEnum")
    fun <E : Enum<E>> optEnum(getter: T.() -> E?): ZkOptEnumColumnV2<T, E> =
        ZkOptEnumColumnV2(this@ZkTable, getter)

    @JsName("columnOptEnumProp")
    inline operator fun <reified E : Enum<E>> KProperty1<T, E?>.unaryPlus(): ZkOptEnumColumnV2<T, E> =
        ZkOptEnumColumnV2(this@ZkTable) { row -> this.get(row) }
            .add(this)

    fun instant(getter: T.() -> Instant): ZkInstantColumnV2<T> =
        ZkInstantColumnV2(this@ZkTable, getter)

    operator fun KProperty1<T, Instant>.unaryPlus(): ZkInstantColumnV2<T> =
        ZkInstantColumnV2(this@ZkTable) { row -> this.get(row) }
            .add(this)

    @PublicApi
    fun optInstant(getter: T.() -> Instant?): ZkOptInstantColumnV2<T> =
        ZkOptInstantColumnV2(this@ZkTable, getter)

    operator fun KProperty1<T, Instant?>.unaryPlus(): ZkOptInstantColumnV2<T> =
        ZkOptInstantColumnV2(this@ZkTable) { row -> this.get(row) }
            .add(this)

    fun localDate(getter: T.() -> LocalDate): ZkLocalDateColumnV2<T> =
        ZkLocalDateColumnV2(this@ZkTable, getter)

    operator fun KProperty1<T, LocalDate>.unaryPlus(): ZkLocalDateColumnV2<T> =
        ZkLocalDateColumnV2(this@ZkTable) { row -> this.get(row) }
            .add(this)

    @PublicApi
    fun optLocalDate(getter: T.() -> LocalDate?): ZkOptLocalDateColumnV2<T> =
        ZkOptLocalDateColumnV2(this@ZkTable, getter)

    operator fun KProperty1<T, LocalDate?>.unaryPlus(): ZkOptLocalDateColumnV2<T> =
        ZkOptLocalDateColumnV2(this@ZkTable) { row -> this.get(row) }
            .add(this)

    fun localDateTime(getter: T.() -> LocalDateTime): ZkLocalDateTimeColumnV2<T> =
        ZkLocalDateTimeColumnV2(this@ZkTable, getter)

    operator fun KProperty1<T, LocalDateTime>.unaryPlus(): ZkLocalDateTimeColumnV2<T> =
        ZkLocalDateTimeColumnV2(this@ZkTable) { row -> this.get(row) }
            .add(this)

    @PublicApi
    fun optLocalDateTime(getter: T.() -> LocalDateTime?): ZkOptLocalDateTimeColumnV2<T> =
        ZkOptLocalDateTimeColumnV2(this@ZkTable, getter)

    operator fun KProperty1<T, LocalDateTime?>.unaryPlus(): ZkOptLocalDateTimeColumnV2<T> =
        ZkOptLocalDateTimeColumnV2(this@ZkTable) { row -> this.get(row) }
            .add(this)

    @PublicApi
    fun uuid(getter: T.() -> UUID): ZkUuidColumnV2<T> =
        ZkUuidColumnV2(this@ZkTable, getter)

    operator fun KProperty1<T, UUID>.unaryPlus(): ZkUuidColumnV2<T> =
        ZkUuidColumnV2(this@ZkTable) { row -> this.get(row) }
            .add(this)

    @PublicApi
    fun optUuid(getter: (T) -> UUID?): ZkOptUuidColumnV2<T> =
        ZkOptUuidColumnV2(this@ZkTable, getter)

    operator fun KProperty1<T, UUID?>.unaryPlus(): ZkOptUuidColumnV2<T> =
        ZkOptUuidColumnV2(this@ZkTable) { row -> this.get(row) }
            .add(this)

    operator fun ZkColumn<T>.unaryPlus(): ZkColumn<T> {
        columns += this
        return this
    }

    fun custom(builder: ZkCustomColumn<T>.() -> Unit = {}): ZkCustomColumn<T> {
        val column = ZkCustomColumn(this)
        column.builder()
        return column
    }

    fun actions(builder: (ZkActionsColumn<T>.() -> Unit)? = null): ZkActionsColumn<T> {
        return if (builder != null) {
            ZkActionsColumn(this@ZkTable, builder)
        } else {
            ZkActionsColumn(this@ZkTable) {
                + action(localizedStrings.details) { _, row ->
                    onDblClick(getRowId(row))
                }
            }
        }
    }

    fun index(builder: (ZkElement.(index: Int,) -> Unit)? = null): ZkIndexColumn<T> {
        return if (builder != null) {
            ZkIndexColumn(this@ZkTable, builder)
        } else {
            ZkIndexColumn(this@ZkTable) { index -> + (index + 1).toString() }
        }
    }

    // -------------------------------------------------------------------------
    //  Convenience
    // -------------------------------------------------------------------------

    /**
     * Set the column to size.
     */
    infix fun ZkColumn<T>.size(size: String): ZkColumn<T> {
        this.max = size
        return this
    }

    /**
     * Set the column to size.
     */
    infix fun ZkColumn<T>.label(text: String): ZkColumn<T> {
        this.label = text
        return this
    }

    /**
     * Marks the column as exportable or non-exportable.
     */
    infix fun ZkColumn<T>.exportable(isExportable: Boolean): ZkColumn<T> {
        this.exportable = isExportable
        return this
    }

}