## Multiplatform xlsx spreadsheet generator module

#### extra light API to generate xlsx files

~~~kotlin
    fun main() {
        
        val doc = XlsxDocument()
        val sheet = doc.newSheet("T2 Database")
        
        sheet["A1"].value = "Name"
        sheet["B1"].value = "Date of birth"
        sheet["C1"].value = "Still alive"

        sheet.fillRow("A2", listOf("John Connor", LocalDate(1985, 2, 28), true))
        sheet.fillRow("A3", listOf("Sarah Connor", LocalDate(1964, 8, 13), true))
        
        content.save("terminator.xlsx")
        
    }
~~~

#### ZkTable data export replacement

~~~kotlin
    ... : ZkTable {
    
        // replace csv download with xlsx download
        override fun onExportCsv() = onExportXlsx()
    
    }
~~~
