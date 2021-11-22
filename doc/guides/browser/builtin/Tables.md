# Tables

* CSS grid for layout
* virtualization
  * intersection observer based
  * up to 100.000 rows, see [performance](#Performance)
* search and sort a bit slow for 100.000 rows, but it's acceptable for now
* features:
  * automatic header text translation based on property name
  * resizable columns (hover over header, move handle)
  * sort (click on column header)
  * search (type into search field and Enter or click the icon)
  * CSV export
  * preload data dependencies before render
  
## Write a Table

1. create a [Business Object](../../common/Data.md) to store the data of one row
1. create a class that extends [ZkTable](/core/core/src/jsMain/kotlin/zakadabar/core/browser/table/ZkTable.kt)
1. override `onConfigure` to set up the table

```kotlin
class BasicTable : ZkTable<ExampleBo>() {

  override fun onConfigure() {

    // These are not necessary, I've added them so the recipe looks better.

    height = 400.px
    + zkLayoutStyles.fixBorder

    // Options for the table.

    add = false // do not show the add action in the title
    search = true // show the search action in the title
    export = true // show the export action in the title

    // This is the title of the table. As we use the local title bar
    // we want to show some title.

    titleText = localized<BasicTable>()
    addLocalTitle = true

    // This is the query the table uses to fetch the data.

    query = ExampleQuery()

    // You can add columns by the properties. If you don't set a label, the
    // table will use the localized property name.

    + ExampleBo::id size 4.em
    + ExampleBo::doubleValue size 4.em
    + ExampleBo::enumSelectValue
    + ExampleBo::localDateValue label "Date".localized

    // Or you can add them with getters, useful for nested data structures.
    // In this case label is necessary as the property name does not exists,
    // so it is not possible to translate it properly.

    + boolean { booleanValue } label "Boolean".localized
    + optBoolean { optBooleanValue } label "OptBoolean".localized
    + optEntityId { optRecordSelectValue } size 4.em label "Reference".localized

    // Actions for the user to execute on the given row. The default shows
    // a "DETAILS" link. Details performs the same action as the
    // "onDblClick" of the table.

    + actions()
  }

  override fun onDblClick(id: String) {
    toastSuccess { "You clicked on details of $id!" }
  }

}
```

## Customization

### Column Size

Change the column size with the `size` configuration option:

```kotlin
+ ExampleBo::stringValue size 10.em
```

### Column Label

Change the column label with the `label` configuration option:

```kotlin
+ ExampleBo::stringValue label "MyLabel".localized
```

### Single Click Open

By default, the table opens the detail page on double click. The `oneClick` 
parameter changes this behaviour. When it is true, the table treats single clicks 
as double clicks.

```kotlin
override fun onConfigure() {
    oneClick = true
}
```

### Export

The export action exports the data in the table into CSV file and offers it for
download. 

`exportCsv` function of the table performs the export. It goes over the rows and 
calls the `exportCsv` function of each column for the given row to produce the
fields of the row.

#### Action

The export action in the table header may be turned off/on with the `export` configuration
variable:

```kotlin
override fun onConfigure() {
    export = true
}
```

#### Filtered Only

By default, the export action includes all rows in the export, no matter what filtering
the user applied on the data. This behavior can be changed with the `exportFiltered` table
configuration:

```kotlin
override fun onConfigure() {
    exportFiltered = true
}
```

Example: [Export Only Filtered Rows](/doc/cookbook/browser/table/export/filtered/recipe.md)

#### Headers

To include header labels in the exported CSV file, use the `exportHeaders` configuration variable:

```kotlin
override fun onConfigure() {
    exportHeaders = true
}
```

Example: [Table Export Headers](/doc/cookbook/browser/table/export/headers/recipe.md)

#### Exclude a Column

To exclude a column from the export, set its `exportable` configuration variable to `false`:

```kotlin
override fun onConfigure() {
    + ExampleBo::intValue exportable false
}
```

#### File Name

To change the name of the generated file, override the getter of the `exportFileName` variable.

Example: [Table Export File Name](/doc/cookbook/browser/table/export/filename/recipe.md)

### Styling

The `styles` property of `ZkTable` contains the style sheet that table instance
uses. This defaults to the value of the
[zkTableStyles](/core/core/src/jsMain/kotlin/zakadabar/core/browser/table/zkTableStyles.kt) global variable.

To change the styles of all tables, extend [ZkTableStyles](/core/core/src/jsMain/kotlin/zakadabar/core/browser/table/zkTableStyles.kt)
and assign an instance of the extended class to `zkTableStyles`.

You can also change the styles of a few or just one specific table by:

- overriding the `styles` property
- assigning a new value to `styles` before `onCreate` runs

Example: [Vertical Table Cell Border](/doc/cookbook/browser/table/border/vertical/recipe.md)

<div data-zk-enrich="TableVerticalBorderSome"></div>

## Extension Columns

Extension columns make it possible to add editable columns to the table without having
the corresponding field in the business object.

Implemented extension columns:

- [ZkBooleanExtensionColumn](/core/core/src/jsMain/kotlin/zakadabar/core/browser/table/columns/ZkBooleanExtensionColumn.kt)
- [ZkStringExtensionColumn](/core/core/src/jsMain/kotlin/zakadabar/core/browser/table/columns/ZkStringExtensionColumn.kt)

Example: [Inline Table Edit](/doc/cookbook/browser/table/inline/recipe.md)

<div data-zk-enrich="TableEditInlineNoBo"></div>

## Custom Actions

The `actions` shorthand of the table accepts a builder function that may be used to
override the default "Details" action and/or add more actions.

You can use the "action" shorthand to add a new 

```kotlin
+ actions {
    + action("hello") { index, row ->
        toastSuccess { "You clicked on 'hello' of ${row.stringValue} (index: $index)." }
    }
    + action("world") { index, row ->
        toastSuccess { "You clicked on 'world' of ${row.stringValue} (index: $index)." }
    }
}
```

Example: [Custom Table Actions](/doc/cookbook/browser/table/action/recipe.md)

## Index Column

Add an index column with the `index` builder function.

The index column displays the current index (plus one) of the row in the table.

Sort and filter resets the value displayed by the index column.

Example: [Table Index Column](/doc/cookbook/browser/table/indexColumn/recipe.md)

<div data-zk-enrich="TableIndexColumn"></div>

## Custom Columns

Add custom columns by:

- using the `custom` shorthand, or
- extending ZkColumn.

### Shorthand

Use the `custom` function to add a column:

```kotlin
+ custom {
    label = "Tags"
    render = { + it.tags.joinToString(", ") }
    matcher = { row, filter -> row.tags.firstOrNull { filter in it } != null}
    sorter = { }
} size "max-content"
```

Example: [Custom Table Column](/doc/cookbook/browser/table/customColumn/recipe.md)

## Preload

Preload is for situations when you don't want to build the actual row content on server side but compose it on client
side like the lookup for descriptions above.

Preloads run before first table render, so you can use the values safely.

```kotlin
class Table : ZkTable<ShipDto>() {

  // These preloads are a bit too many, it might be better to create a
  // DTO for this table and build the result properly on server side.

  private val seas by preload { SeaDto.allAsMap() }
  private val ports by preload { PortDto.allAsMap() }
  private val speeds by preload { SpeedDto.allAsMap() }
  private val accounts by preload { AccountPublicDto.allAsMap() }

  override fun onConfigure() {
    title = Strings.ships
    crud = Ships

    add = true
    search = true

    + ShipDto::id
    + ShipDto::name
    + ShipDto::hasPirateFlag

    + custom {
      label = Strings.speed
      render = { + speeds[it.speed]?.description }
    }

    + custom {
      label = Strings.captain
      render = { + accounts[it.captain]?.fullName }
    }

    + custom {
      label = Strings.port
      render = {
        val port = ports[it.port]
        val sea = seas[port?.sea]
        + "${port?.name ?: "Davy Jones Locker"} (${sea?.name ?: "At World's End"})"
      }
    }

    + actions()
  }

}
```

## Examples

### Crud

Set `tableClass` of the crud object.

```kotlin
object BuiltinCrud : ZkCrudTarget<BuiltinDto>() {
  init {
    companion = BuiltinDto.Companion
    dtoClass = BuiltinDto::class
    pageClass = BuiltinForm::class
    tableClass = BuiltinTable::class
  }
}
```

### Direct, Fetched Data

```kotlin
object FetchedTable : ZkPage(cssClasses = arrayOf(grow)) {

  override fun onCreate() {
    super.onCreate()
    io {
      // Add the table and set the data. It is not important to use these
      // together, you can add the table and set the data later.

      + BuiltinTable().setData(BuiltinDto.all())
    }
  }

}
```

### Direct, Generated Data

```kotlin
object Table : ZkPage() {

  override fun onCreate() {
    super.onCreate()

    classList += grow

    // Create a template DTO.

    val template: BuiltinDto = default { }

    // Create the data to display.

    val data = (1..100).map { template.copy(id = it.toLong()) }

    // Add the table and set the data. It is not important to use these
    // together, you can add the table and set the data later.

    + BuiltinTable().setData(data)
  }

}
```

### Table With Many Rows

This table contains 10.000 generated rows.

<div data-zk-enrich="TableBigExample"></div>

## Performance

The table should work properly up to 100.000 rows.

With 1.000.000:

* rendering is slowed down because the intersection observer has to handle large number of areas
* Safari lags big time
* Firefox shows 426086 as last row

The solution for these would be to use fewer areas and improve row position handling. However, in that case the
scrollbar position problems might arise.
