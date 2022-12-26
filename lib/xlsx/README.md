## Multiplatform xlsx spreadsheet generator module

### Public models

- XlsxDocument
- XlsxSheet
- XlsxCell
- XlsxCoordinate

### Customization
- XlsxConfiguration
- XlsxFormats


### extra light API to generate xlsx files

~~~kotlin
    fun main() {
        
        val doc = XlsxDocument()
        val sheet = doc.newSheet("T2 Database")
        
        sheet["A1"].value = "Name"
        sheet["B1"].value = "Date of birth"
        sheet["C1"].value = "Still alive"

        sheet.fillRow("A2", listOf("John Connor", LocalDate(1985, 2, 28), true))
        sheet.fillRow("A3", listOf("Sarah Connor", LocalDate(1964, 8, 13), true))
        
        // download or save as easy as pie
        content.save("terminator.xlsx")
        
    }
~~~

### ZkTable data export replacement

~~~kotlin
    ... : ZkTable {
    
        // replace csv download with xlsx download
        override fun onExportCsv() = onExportXlsx()
    
    }
~~~

or on a more customizable way 
~~~kotlin
    ... : ZkTable {
    
        // replace csv download with xlsx download
        override fun onExportCsv() = onExportXlsx()
        
        fun onExportXlsx() {
            val cfg = XlsxConfiguration()
            // put some config stuff here
            
            val sheetName = "Daily report"
            
            // generate xlsx model
            val doc = toXlsxDocument(sheetName, cfg)
            
            // acquire the sheet
            val sheet = doc.sheets.first()

            // adjust column width
            sheet.columns["C"].width = 12.5            

            // download
            doc.save(exportXlsxFileName)
            
        }   
    }
~~~
