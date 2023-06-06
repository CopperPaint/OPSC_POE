package com.example.opsc_poe

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.NumberPicker.OnValueChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.opsc_poe.databinding.ActivityCreateGoalBinding


class Create_Goal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCreateGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        //set activity
        var activity = GlobalClass.activities[intent.extras?.getInt("CurrentActivity")!!]

        //set activity name
        binding.tvActivity.text = activity.name

        //set status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.Dark_Green)

        //Picker Set up
        binding.npHourGoal.minValue = 1
        binding.npHourGoal.maxValue = 24
        binding.npHourGoal.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS

        val timeFrame = arrayOf<String>("Daily", "Weekly", "Monthly")
        binding.npTimeFrameGoal.minValue = 1
        binding.npTimeFrameGoal.maxValue = 3
        binding.npTimeFrameGoal.displayedValues = timeFrame
        binding.npTimeFrameGoal.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS

        var currentGoalID = intent.extras?.getInt("currentGoalIDIndex")
        var currentGoal = GlobalClass.goals[currentGoalID!!]

        //if goal exists, preset values
        if (currentGoal.isSet)
        {
            var currentInterval = 0

            when (currentGoal.interval){
                "Daily" -> currentInterval = 1
                "Weekly" -> currentInterval = 2
                "Monthly" -> currentInterval = 3
            }

            binding.npTimeFrameGoal.value = currentInterval

            binding.npTimeFrameGoal.setOnValueChangedListener { picker, oldVal, newVal ->

                when (newVal) {
                    1 -> {binding.npHourGoal.maxValue = 24}
                    2 -> {binding.npHourGoal.maxValue = 168}
                    3 -> { binding.npHourGoal.maxValue = 720}
                    else -> {binding.npHourGoal.maxValue = 24}
                }
            }
            binding.npHourGoal.value = currentGoal.amount
        }

        //save goal
        binding.tvSaveButton.setOnClickListener()
        {
            var intervalText = ""
            when (binding.npTimeFrameGoal.value) {
                1 -> {intervalText = "Daily"}
                2 -> {intervalText = "Weekly"}
                3 -> {intervalText = "Monthly"}
                else -> {intervalText = "Daily"}
            }
            currentGoal.interval = intervalText
            currentGoal.amount = binding.npHourGoal.value
            currentGoal.isSet = true

            //return user to the home view screen
            var intent = Intent(this, Home_Activity::class.java) //ViewActivity
            startActivity(intent)
        }
    }
}