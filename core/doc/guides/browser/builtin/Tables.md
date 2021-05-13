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

## Write a Table component

1. create a [DTO](../../common/Data.md) to store the data of one row
1. create a class that extends [ZkTable](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/table/ZkTable.kt)
1. override `onConfigure` to set up the table

```kotlin
class BuiltinTable : ZkTable<BuiltinDto>() {

  override fun onConfigure() {

    // Sets the CRUD the table uses. When set add and double click on rows
    // calls this crud to create a new record or open the record for update.

    crud = BuiltinCrud

    // Set the title of the table.

    title = t("builtin")

    // Enable the add button (plus icon in the header).
    // Enable search (input field in the header).
    // Enable CSV export (download icon in the header).

    add = true
    search = true
    export = true

    // Add columns to the table. Column types are automatically
    // derived from the property type.

    + BuiltinDto::id
    + BuiltinDto::booleanValue
    + BuiltinDto::doubleValue
    + BuiltinDto::enumSelectValue
    + BuiltinDto::instantValue
    + BuiltinDto::stringValue
    + BuiltinDto::uuidValue
    + BuiltinDto::optUuidValue

    // Add a custom column

    + custom {
      label = t("custom")
      render = { row ->
        if ((row.id % 2L) == 0L) {
          + t("odd")
        } else {
          + t("even")
        }
      }
    }

    // Add an actions column, this will contain a "Details" link
    // that calls openUpdate of the crud.

    + actions()
  }

}
```

## Use a Table - CRUD

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

## Use a Table - Direct, Fetched Data

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

## Use a Table - Direct, Generated Data

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

## Custom rendering

* Use the `custom` function to add a column with custom rendering.
* Check the preload section about how to fetch data for building complex values.

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

## Performance

The table should work properly up to 100.000 rows.

With 1.000.000:

* rendering is slowed down because the intersection observer has to handle large number of areas
* Safari lags big time
* Firefox shows 426086 as last row

The solution for these would be to use fewer areas and improve row position handling. However, in that case the
scrollbar position problems might arise.