package com.example.opsc_poe

import android.content.Context
import android.content.res.Resources
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ExportLogData(

    private val defaultDate: String = "",
    private val dateFormat: String = "",

    var logActivityName: String = "",
    var logHour: Double = 0.0,
    var logDate: LocalDate = LocalDate.parse((defaultDate), DateTimeFormatter.ofPattern(dateFormat)) //"d-M-yyyy"
)
{
    fun toCSVRow(): String
    {
        return "\"${logActivityName}\", ${logHour}, $logDate"
    }
}