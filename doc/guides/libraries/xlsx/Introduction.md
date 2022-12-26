# Library: xlsx

A module to generate xlsx spreadsheet files with multiplatform capability.

## Features
- extra light API
- it works both jvm and js platforms
- more worksheet in a file
- cell indexing with excel-like letter-number coordinates ["A1"], ["BC23"] or with numeric coordinates [5,2]
- customizable date formats (minute, second or milliseconds precision)
- customizable numeric formats
- ZkTable content exporting
- enum and boolean localization
- auto number-format applying for Boolean, Number and Date types
- customizable column widths 

## Restrictions
- no formulas
- no charts
- no extra formatting (colors, borders, alignments)
- no extra content (images, links, embedded media)
- no macros


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

    val cfg = XlsxConfiguration()
    // put custom settings here
    // cfg.dateFormat = cfg.formats.ISO_DATE
    // cfg.localizedEnums = true
    // ...
    
    val doc = XlsxDocument(cfg)
    
    // Create a new Worksheet
    val sheet1 = doc.newSheet("Stuff Members")

    // adjust column width
    sheet1.columns["A"].width = 15.0
    sheet1.columns["B"].width = 11.4

    // set header line
    sheet1.fillRow("A1", listOf("name", "birth", "height", "dead"))
    
    // set data records
    (2..10).forEach { row->
        sheet1[1, row].value = "Name-$row"
        sheet1[2, row].value = LocalDate(2022,12,21) 
        sheet1[3, row].value = row / 2.0
        sheet1[4, row].value = row % 2 == 0
    }

    // create another Worksheet
    val sheet2 = doc.newSheet("Summary")

    sheet2["A1"].value = "summary"
    sheet2["A2"].value = 8

    sheet2["B1"].value = "extra"
    sheet2["B2"].value = false

    sheet2["C1"].value = "timestamp"
    sheet2["C2"].value = Clock.System.now()

    // saving as easy as pie
    doc.save("test.xlsx")

    // in jvm the content can be written directly into java.io.OutputStream
    // doc.writeTo(anArbitraryOutputStream)
    
```

### Browser

##### ZkTable csv download replacement

There is a convenient extension function: onExportXlsx to generate and download ZkTable content in the browser.

```kotlin
    import zakadabar.lib.xlsx.browser.onExportXlsx

    class ...Table : ZkTable<...Bo>() {

        override fun onExportCsv() = onExportXlsx()
        
    }
```

##### Customization

write own onExportXlsx function

```kotlin
    fun onExportXlsx() {
    
        // the customization instance
        val cfg = XlsxConfiguration()

        // create custom date format
        val customDateFormat = cfg.formats.CustomNumberFormat("yyyy. mm. dd. hh:mm")
        cfg.dateTimeFormat = customDateFormat
        cfg.instantFormat = customDateFormat

        // create custom number format
        val roundedAndThousandSeparatedNumberFormat = cfg.formats.CustomNumberFormat("#,##0.000")
        
        // create document with custom configuration
        val doc = toXlsxDocument("Sheet 1", cfg)
    
        // access sheet
        val sheet = doc.sheets.first()
    
        // apply number format on a cell
        sheet["B3"].numberFormat = roundedAndThousandSeparatedNumberFormat

        // other operations here
        // adjusting column width adding more Worksheets, etc.

        // in the browser the xlsx file will be downloaded
        // there is exportXlsxFileName property as kotlin extension
        doc.save(exportXlsxFileName)
    
    }
```


### Supported Value Types
- Boolean
- Enum<*>
- Number (Int, Long, Double, ...)
- String
- LocalDate
- LocalDateTime
- Instant (with configurable TimeZone)

Boolean and Enum types can be localized by configuration.

Any other types converted into String via toString() method.

### Supported Number Formats
- general (unformatted)
- built-in date, according to the client's language
- built-in datetime, according to the client's language
- ISO-8601 date
- ISO-8601 date and time with minute precision
- ISO-8601 date and time with second precision
- ISO-8601 date and time with millisecond precision

Moreover any other custom format can be created by xlsx format code

## References

- [Structure of a SpreadsheetML document](https://learn.microsoft.com/en-us/office/open-xml/structure-of-a-spreadsheetml-document) - Microsoft

- [ECMA-376](https://www.ecma-international.org/publications-and-standards/standards/ecma-376/) Office Open XML file formats

- [ISO-8601](https://en.wikipedia.org/wiki/ISO_8601) - Date and Time formatting

- [RFC-3339](https://www.rfc-editor.org/rfc/rfc3339) - Date and Time on the Internet: Timestamps
