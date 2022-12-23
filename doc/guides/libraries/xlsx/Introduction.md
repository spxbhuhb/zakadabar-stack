# Library: xlsx

A module to generate xlsx spreadsheet files with multiplatform capability.

## Features
- extra light API
- it works both jvm and js platforms
- more worksheet in a file
- cell indexing with excel-like letter-number coordinates "A1", "BC23" or coordinate numbers [5,2]
- customizable date formats (minute, second or milliseconds precision)
- easy ZkTable content exporting instead of csv
- on-demand enum and boolean localization
- auto number-format applying for Boolean, Number and Date types

## Restrictions
- no formulas
- no charts
- no extra formatting (colors, borders, alignments)
- no extra content (images, links, embedded media)


## Setup

To use this module in your application:

1. add the gradle dependency

### Common
**gradle**

```kotlin
implementation("hu.simplexion.zakadabar:xlsx:$contentVersion")
```

## Use

### General example
```kotlin
    val doc = XlsxDocument()
    val sheet1 = doc["Stuff Members"]
    
    // set header line
    sheet1.setRow("A1", listOf("name", "birth", "height", "dead"))
    
    // set data records
    (2..10).forEach { row->
        sheet1[1, row].value = "Name-$row"
        sheet1[2, row].value = LocalDate(2022,12,21) 
        sheet1[3, row].value = row / 2.0
        sheet1[4, row].value = row % 2 == 0
    }

    val sheet2 = doc["Summary"]

    sheet2["A1"].value = "summary"
    sheet2["A2"].value = 8

    sheet2["B1"].value = "extra"
    sheet2["B2"].value = false

    sheet2["C1"].value = "timestamp"
    sheet2["C2"].value = Clock.System.now()

    val content = doc.toContentMap()
    // in js trigger a download via browser
    // in jvm save a file to filesystem
    content.saveXlsx("test.xlsx")

    // in jvm the content can be written directly into java.io.OutputStream
    // content.writeTo(anArbitraryOutputStream)
    
```

### Browser

There is a convenient extension function: onExportXlsx to generate and download ZkTable content in the browser.


##### ZkTable csv download replacement


```kotlin
    import zakadabar.lib.xlsx.browser.onExportXlsx

    class ...Table : ZkTable<...Bo>() {

        override fun onExportCsv() = onExportXlsx("Data sheet")
        
    }
```

##### Customization

via XlsxConfiguration class

```kotlin
    override fun onExportCsv() {
        val cfg = XlsxConfiguration().apply {
            localizedEnums = true
            instantFormat = XlsxNumberFormat.ISO_DATETIME_MIN
        }
        
        onExportXlsx("Customized data sheet", cfg)
    }
```

directly in XlsxDocument
```kotlin
    val cfg = XlsxConfiguration().apply {
        ...
    }

    val doc = XlsxDocument(cfg)
```

## Restrictions
### Supported Value Types
- Boolean
- Enum<*>
- Number (Int, Long, Double, ...)
- String
- LocalDate
- LocalDateTime
- Instant (with configurable TimeZone)

Boolean and Enum types can be localized, controlled by configuration parameter.
Any other types converted into String via toString() method

### Supported Number Formats
- general (unformatted)
- built-in date, according to the client's language
- built-in datetime, according to the client's language
- ISO-8601 date
- ISO-8601 date and time with minute precision
- ISO-8601 date and time with second precision
- ISO-8601 date and time with millisecond precision

## References

- [Structure of a SpreadsheetML document](https://learn.microsoft.com/en-us/office/open-xml/structure-of-a-spreadsheetml-document)

- [ECMA-376](https://www.ecma-international.org/publications-and-standards/standards/ecma-376/) Office Open XML file formats

- [ISO-8601](https://en.wikipedia.org/wiki/ISO_8601) - Date and Time formatting

- [RFC-3339](https://www.rfc-editor.org/rfc/rfc3339) - Date and Time on the Internet: Timestamps
