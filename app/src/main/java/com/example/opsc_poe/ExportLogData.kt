package com.example.opsc_poe

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ExportLogData(

    var logActivityName: String = "",
    //var logName: String = arrayListOf<String>()
    var logHour: Double = 0.0,
    var logDate: LocalDate = LocalDate.parse(("01-01-1900"), DateTimeFormatter.ofPattern("d-M-yyyy"))
)
{
    fun toCSVRow(): String
    {
        return "\"${logActivityName}\", ${logHour}, $logDate"
    }
}