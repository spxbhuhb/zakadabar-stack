## Multiplatform xlsx spreadsheet generator module

#### extra light API to generate xlsx files

~~~kotlin
    fun main() {
        
        val doc = XlsxDocument()
        val sheet = doc["T2 Database"]
        
        sheet["A1"].value = "Name"
        sheet["B1"].value = "Date of birth"
        sheet["C1"].value = "Still alive"

        sheet.setRow("A2", listOf("John Connor", LocalDate(1985, 2, 28), true))
        sheet.setRow("A3", listOf("Sarah Connor", LocalDate(1964, 8, 13), true))

        val content = doc.toContentMap()
        
        content.saveXlsx("terminator.xlsx")
        
    }
~~~

#### ZkTable data export replacement

~~~kotlin
    ... : ZkTable {
    
        // replace csv download with xlsx download
        override fun onExportCsv() = onExportXlsx()
    
    }
~~~
