package com.example.opsc_poe

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

data class Temp_LogDataClass(
    var logID: Int = 0,
    var activityID: Int = 0,
    var userID: Int = 0,
    var startDate: LocalDate = LocalDate.now(),
    var endDate: LocalDate = LocalDate.now(),
    var hours: Double = 0.0
)

data class LogStore(
    var logID: Int,
    var activityID: Int,
    var userID: Int,
    var startDate: String,
    var endDate: String,
    var hours: Double
)
