package com.example.opsc_poe

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.opsc_poe.GlobalClass.Companion.DoubleToTime
import com.example.opsc_poe.GlobalClass.Companion.NoUserAppData
import com.example.opsc_poe.GlobalClass.Companion.user
import com.example.opsc_poe.databinding.HomeActivityViewFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class home_activity_view_fragment : Fragment(R.layout.home_activity_view_fragment) {

    private var _binding: HomeActivityViewFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeActivityViewFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        //-------------------------------------------------
        //code here

        //Test Data
        //GlobalClass.user.userID = 5



        //Read Data
        GlobalScope.launch{
            try
            {
                if (GlobalClass.UpdateDataBase == true)
                {
                    var DBManger = ManageDatabase()
                    GlobalClass.categories = DBManger.getCategoriesFromFirestore(user.userID)
                    GlobalClass.activities = DBManger.getActivitesFromFirestore(user.userID)
                    GlobalClass.goals = DBManger.getGoalsFromFirestore(user.userID)
                    GlobalClass.logs = DBManger.getLogsFromFirestore(user.userID)
                    GlobalClass.UpdateDataBase = false
                }
                withContext(Dispatchers.Main) {
                    UpdateUI()
                }
            }
            catch (e: Error)
            {
                GlobalClass.InformUser("Error", "${e.toString()}", requireContext())
            }
        }

        //-------------------------------------------------
        return view
    }

    //method to Update UI Components
    @SuppressLint("Range")
    fun UpdateUI()
    {
        try
        {
            //remove loading screen
            binding.ivloadingturt.visibility = View.GONE
            binding.progressBar.visibility = View.GONE

            var userHasData = false
            for (i in GlobalClass.activities.indices)
            {
                if (GlobalClass.activities[i].userID == user.userID)
                {
                    userHasData = true
                    break
                }
            }

            if (userHasData == false) //if user has no data
            {
                NoUserAppData(binding.llBars, activity, requireContext(),"Activity", 0)
            }
            else //if user has data
            {
                val activityLayout = binding.llBars
                for (i in GlobalClass.activities.indices)
                {
                    //if the activity belongs to the signed in user
                    if (GlobalClass.activities[i].userID == GlobalClass.user.userID)
                    {
                        //create new custom activity
                        var newActivity = CustomActivity(activity)
                        //set primary text
                        newActivity.binding.tvPrimaryText.text = GlobalClass.activities[i].name

                        //get activity category
                        var index = Temp_CategoryDataClass().GetIndex(GlobalClass.activities[i].categoryID, GlobalClass.categories)
                        var category = GlobalClass.categories[index]

                        //set secondary text
                        newActivity.binding.tvSecondaryText.text = category.name

                        //set the activity color shape color
                        val catColour = ColorStateList.valueOf(Color.parseColor(category.colour))
                        newActivity.binding.llBlockText.backgroundTintList = catColour
                        //newActivity.binding.llBlockText.backgroundTintList =  ColorStateList.valueOf(Color.parseColor("#5c37d7"))

                        var hour = ""
                        var text = ""

                        var currentMaxGoal = -1
                        var currentMinGoal = -1

                        //find activity goals
                        for (j in GlobalClass.goals.indices)
                        {
                            if (GlobalClass.activities[i].maxgoalID == GlobalClass.goals[j].goalID)
                            {
                                currentMaxGoal = j
                            }

                            if (GlobalClass.activities[i].mingoalID == GlobalClass.goals[j].goalID)
                            {
                                currentMinGoal = j
                            }
                        }

                        //set goals
                        var minGoal = GlobalClass.goals[currentMinGoal]
                        var maxGoal = GlobalClass.goals[currentMaxGoal]

                        if (minGoal.isSet)
                        {
                            if (maxGoal.isSet) //both goals
                            {
                                var (hour, text, color) = GoalHourCalculator().CalculateHours(currentMinGoal, currentMaxGoal, GlobalClass.activities[i].activityID)
                                val barColor = ColorStateList.valueOf(Color.parseColor(color))
                                newActivity.binding.vwBar.backgroundTintList = barColor
                                newActivity.binding.tvBlockText.text = text
                                newActivity.binding.tvBlockX.text = DoubleToTime(hour)
                            }
                            else //min only
                            {
                                var goal = GlobalClass.goals[currentMinGoal]
                                var (hour, text, color) = GoalHourCalculator().CheckGoal(goal.interval, goal.amount, GlobalClass.activities[i].activityID)
                                val barColor = ColorStateList.valueOf(Color.parseColor(color))
                                newActivity.binding.vwBar.backgroundTintList = barColor
                                newActivity.binding.tvBlockText.text = text
                                newActivity.binding.tvBlockX.text = GlobalClass.DoubleToTime(hour)
                            }
                        }
                        else
                        {
                            if (maxGoal.isSet) //max only
                            {
                                var goal = GlobalClass.goals[currentMaxGoal]
                                var (hour, text, color) = GoalHourCalculator().CheckGoal(goal.interval, goal.amount, GlobalClass.activities[i].activityID)
                                val barColor = ColorStateList.valueOf(Color.parseColor(color))
                                newActivity.binding.vwBar.backgroundTintList = barColor
                                newActivity.binding.tvBlockText.text = text
                                newActivity.binding.tvBlockX.text = GlobalClass.DoubleToTime(hour)
                            }
                            else //no goals
                            {
                                var total = 0.0
                                for (k in GlobalClass.logs.indices)
                                {
                                    if (GlobalClass.logs[k].activityID == GlobalClass.activities[i].activityID)
                                    {
                                        total = total + GlobalClass.logs[k].hours
                                    }
                                }
                                newActivity.binding.tvBlockText.text = "Total Hours:"
                                newActivity.binding.tvBlockX.text = GlobalClass.DoubleToTime(total.toString())
                            }
                        }
                    if (minGoal.isSet)
                    {
                        if (maxGoal.isSet) //both goals
                        {
                            var (hour, text, color) = GoalHourCalculator().CalculateHours(currentMinGoal, currentMaxGoal, GlobalClass.activities[i].activityID)
                            val barColor = ColorStateList.valueOf(Color.parseColor(color))
                            newActivity.binding.vwBar.backgroundTintList = barColor
                            newActivity.binding.tvBlockText.text = text
                            newActivity.binding.tvBlockX.text = DoubleToTime(hour, requireContext())
                        }
                        else //min only
                        {
                            var goal = GlobalClass.goals[currentMinGoal]
                            var (hour, text, color) = GoalHourCalculator().CheckGoal(goal.interval, goal.amount, GlobalClass.activities[i].activityID)
                            val barColor = ColorStateList.valueOf(Color.parseColor(color))
                            newActivity.binding.vwBar.backgroundTintList = barColor
                            newActivity.binding.tvBlockText.text = text
                            newActivity.binding.tvBlockX.text = GlobalClass.DoubleToTime(hour, requireContext())
                        }
                    }
                    else
                    {
                        if (maxGoal.isSet) //max only
                        {
                            var goal = GlobalClass.goals[currentMaxGoal]
                            var (hour, text, color) = GoalHourCalculator().CheckGoal(goal.interval, goal.amount, GlobalClass.activities[i].activityID)
                            val barColor = ColorStateList.valueOf(Color.parseColor(color))
                            newActivity.binding.vwBar.backgroundTintList = barColor
                            newActivity.binding.tvBlockText.text = text
                            newActivity.binding.tvBlockX.text = GlobalClass.DoubleToTime(hour, requireContext())
                        }
                        else //no goals
                        {
                            var total = 0.0
                            for (k in GlobalClass.logs.indices)
                            {
                                if (GlobalClass.logs[k].activityID == GlobalClass.activities[i].activityID)
                                {
                                    total = total + GlobalClass.logs[k].hours
                                }
                            }
                            newActivity.binding.tvBlockText.text = "Total Hours:"
                            newActivity.binding.tvBlockX.text = GlobalClass.DoubleToTime(total.toString(), requireContext())
                        }
                    }

                        //select activity binding
                        newActivity.setOnClickListener(){
                            var intent = Intent(activity, ViewActivity::class.java)
                            intent.putExtra("activityIDIndex", i)
                            startActivity(intent)
                        }

                        //add the new view
                        activityLayout.addView(newActivity)
                    }
                }
            }
        }
        catch (e: Error)
        {
            GlobalClass.InformUser("Error", "${e.toString()}", requireContext())
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
