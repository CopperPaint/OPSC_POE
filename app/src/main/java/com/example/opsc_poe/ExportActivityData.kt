package com.example.opsc_poe

class ExportActivityData (

    var activityName: String = "",
    var activityCategory: String = "",
    var activityDescription: String = "",
    var activityLogCount: Int = 0,

    var activityGoalCompletedAmount: Double = 0.0,

    var activityMaximumGoalInterval: String = "",
    var activityMaximumGoalTime: Double = 0.0,
    //var activityMaximumGoalCompletedAmount: Double = 0.0,
    var activityMaximumGoalRemaining: Double = 0.0,

    var activityMinimumGoalInterval: String = "",
    var activityMinimumGoalTime: Double = 0.0,
    //var activityMinimumGoalCompletedAmount: Double = 0.0,
    var activityMinimumGoalRemaining: Double = 0.0
)
{

    fun toCSVRow(): String
    {
        return "\"${activityName}\", \"${activityDescription}\", \"${activityCategory}\", ${activityLogCount}, ${activityGoalCompletedAmount}, \"${activityMaximumGoalInterval}\", ${activityMaximumGoalTime}, ${activityMaximumGoalRemaining}, \"${activityMinimumGoalInterval}\", ${activityMinimumGoalTime}, ${activityMinimumGoalRemaining}"
    }

}