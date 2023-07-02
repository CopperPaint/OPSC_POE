package com.example.opsc_poe

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.opsc_poe.GlobalClass.Companion.ReturnToHome
import com.example.opsc_poe.databinding.ActivityCreateGoalBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Create_Goal : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val binding = ActivityCreateGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Hide the action bar
        supportActionBar?.hide()

        //set status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.Dark_Green)

        var currentActivityIndex = intent.extras?.getInt("CurrentActivity")!!
        //var currentActivityIndex = intent.getIntExtra("CurrentActivity",0)

        //set activity
        var activity = GlobalClass.activities[currentActivityIndex]

        //set activity name
        binding.tvActivity.text = activity.name

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
            try{
                binding.tvScreenFunction.text = "Edit"

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
            catch (e: Error)
            {
                GlobalClass.InformUser("Error", "${e.toString()}", this)
            }
        }

        //save goal
        binding.tvSaveButton.setOnClickListener()
        {

            try{
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

                GlobalScope.launch {
                    var documentID = GlobalClass.documents.GoalIDs[currentGoalID]
                    var DBmanager = ManageDatabase()
                    DBmanager.updateGoalInFirestore(currentGoal, documentID)

                    //READ DATA
                    GlobalClass.categories = DBmanager.getCategoriesFromFirestore(GlobalClass.user.userID)
                    GlobalClass.activities = DBmanager.getActivitesFromFirestore(GlobalClass.user.userID)
                    GlobalClass.goals = DBmanager.getGoalsFromFirestore(GlobalClass.user.userID)
                    GlobalClass.logs = DBmanager.getLogsFromFirestore(GlobalClass.user.userID)
                    GlobalClass.UpdateDataBase = false

                    //back to home page
                    withContext(Dispatchers.Main) {
                        ReturnToHome()
                    }
                }
            }
            catch (e: Error)
            {
                GlobalClass.InformUser("Error", "${e.toString()}", this)
            }
        }

        //Help button
        binding.tvNeedHelpButton.setOnClickListener()
        {
            try{
                var intent = Intent(this, Help::class.java) //ViewActivity

                intent.putExtra("previousScreen", "Create_Goal")
                intent.putExtra("CurrentActivity", currentActivityIndex)
                intent.putExtra("currentGoalIDIndex", currentGoalID)
                //GlobalClass.InformUser("", currentActivityIndex.toString(), this)
                startActivity(intent)
            }
            catch (e: Error)
            {
                GlobalClass.InformUser("Error", "${e.toString()}", this)
            }
        }

        binding.imgBlackTurtle.setOnClickListener()
        {
            ReturnToHome(this)
        }
    }

    fun ReturnToHome()
    {
        //back to home page
        var intent = Intent(this, Home_Activity::class.java)
        startActivity(intent)
    }


    override fun onBackPressed() {}
}