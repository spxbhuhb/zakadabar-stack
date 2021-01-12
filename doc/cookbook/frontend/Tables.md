# Tables

1. create a [RecordDto](../../../core/src/commonMain/kotlin/zakadabar/stack/data/record/RecordDto.kt) that stores data for a row (ShipDto in the example below)
1. create a class that extends [ZkTable](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/table/ZkTable.kt)
1. define an `init` block and add the columns like this:

```kotlin
class ShipTable : ZkTable<ShipDto>() {

    private val speeds by preload { SpeedDto.allAsMap() }

    init {
        + ShipDto::id build { label = "#" }
        + ShipDto::name
        + custom { render = { speeds[it.speed]?.description } }
        + ShipDto::id.actions(Ships)
    }

}
```

## Customizing builtin columns

* Add `build` and a function to customize the column.

Customizable properties

| name | description |
| ---- | ---- |
| label | the text in the header of the column |

## Custom rendering

* Use the `custom` function to add a column with custom rendering.
* Check the preload section about how to fetch data for building complex values.

## Preload

Preload is for situations when you don't want to build the actual row content on server side but compose it on client side like the lookup for descriptions above.

Preloads run before table render, so you can use the values safely.

## Actions

If there is a [ZkCrud](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/elements/ZkCrud.kt) you can add an `actions` column on the `id` property of the DTO. See the example above.

## Rendering

* Each column has a `renderHeader` function to render the header.
* Each column has a `render` function to render the content of the column.
* Each cell is a ZkElement and `renderHeader`/`render` is called in the builder of that ZkElement.
