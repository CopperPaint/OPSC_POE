package com.example.opsc_poe

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.opsc_poe.databinding.ActivityViewDetailsFragmentBinding


class View_Activity_Details_Fragment : Fragment(R.layout.activity_view_details_fragment) {

    private var _binding: ActivityViewDetailsFragmentBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!


    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityViewDetailsFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        //create local fragment controller
        val fragmentControl = FragmentHandler()


        val activityIDIndex = requireActivity()!!.intent.extras!!.getInt("activityIDIndex")
        //-------------------------------------------------
        //code here


        var Activity = GlobalClass.activities[GlobalClass.activities.size -1]
        binding.imageView.setImageBitmap(Activity.photo)

        var activity = GlobalClass.activities[activityIDIndex]
        var catIndex = Temp_CategoryDataClass().GetIndex(activity.categoryID, GlobalClass.categories)
        var category = GlobalClass.categories[catIndex]
        val catColour = ColorStateList.valueOf(Color.parseColor(category.colour))

        //passed activity
        binding.tvDescription.text = activity.description  //description
        binding.imgBackIndicator.backgroundTintList = catColour
        binding.tvLogsAmount.backgroundTintList = catColour


        //get goal indexes
        var currentMaxGoal = -1
        var currentMinGoal = -1


        for (j in GlobalClass.goals.indices)
        {
            if (activity.maxgoalID == GlobalClass.goals[j].goalID)
            {
                currentMaxGoal = j
            }

            if (activity.mingoalID == GlobalClass.goals[j].goalID)
            {
                currentMinGoal = j
            }

        }


        //MAX GOAL
        //------------------------------------------------

        var maxGoalCustom = CustomActivity(requireActivity())

        //set the divider bar to be closer to fit all elements inside the view
        val maxParam = maxGoalCustom.binding.vwBar.layoutParams as ViewGroup.MarginLayoutParams
        maxParam.setMargins(28, maxParam.topMargin, maxParam.rightMargin, maxParam.bottomMargin)
        maxGoalCustom.binding.vwBar.layoutParams = maxParam




        maxGoalCustom.binding.tvPrimaryText.text = "Maximum Goal"
        maxGoalCustom.binding.llBlockText.backgroundTintList = catColour

        if (currentMaxGoal == -1)
        {
            maxGoalCustom.binding.tvSecondaryText.text = "Goal Not Set"
            maxGoalCustom.binding.tvBlockText.text = "Hours"
            maxGoalCustom.binding.tvBlockX.text = "X"
        }
        else
        {
            var maxGoal = GlobalClass.goals[currentMaxGoal]
            maxGoalCustom.binding.tvSecondaryText.text = maxGoal.interval
            var (maxhour, maxText, maxColor) = GoalHourCalculator().CheckGoal(maxGoal.interval, maxGoal.amount, activity.activityID)
            val maxBarColor = ColorStateList.valueOf(Color.parseColor(maxColor))
            maxGoalCustom.binding.vwBar.backgroundTintList = maxBarColor
            maxGoalCustom.binding.tvBlockText.text = maxText
            maxGoalCustom.binding.tvBlockX.text = maxhour

        }
        binding.llgoalcontainer.addView(maxGoalCustom)


        //MIN GOAL
        //------------------------------------------------

        var minGoalCustom = CustomActivity(requireActivity())

        //set the divider bar to be closer to fit all elements inside the view
        val minParam: ViewGroup.MarginLayoutParams = minGoalCustom.binding.vwBar.layoutParams as ViewGroup.MarginLayoutParams
        minParam.setMargins(28, minParam.topMargin, minParam.rightMargin, minParam.bottomMargin)
        minGoalCustom.binding.vwBar.layoutParams = minParam


        minGoalCustom.binding.tvPrimaryText.text = "Minimum Goal"
        minGoalCustom.binding.llBlockText.backgroundTintList = catColour
        if (currentMinGoal == -1)
        {
            minGoalCustom.binding.tvSecondaryText.text = "Goal Not Set"
            minGoalCustom.binding.tvBlockText.text = "Hours"
            minGoalCustom.binding.tvBlockX.text = "X"
        }
        else
        {
            var minGoal = GlobalClass.goals[currentMinGoal]
            minGoalCustom.binding.tvSecondaryText.text = minGoal.interval
            var (hour, text, color) = GoalHourCalculator().CheckGoal(minGoal.interval, minGoal.amount, activity.activityID)
            val maxBarColor = ColorStateList.valueOf(Color.parseColor(color))
            minGoalCustom.binding.vwBar.backgroundTintList = maxBarColor
            minGoalCustom.binding.tvBlockText.text = text
            minGoalCustom.binding.tvBlockX.text = hour
        }
        binding.llgoalcontainer.addView(minGoalCustom)


        //calculate how many logs the current activity has
        var currentUserGoalCount = 0

        for (i in GlobalClass.logs.indices) {
            //if (GlobalClass.logs[i].userID == GlobalClass.user.userID) {
                if (GlobalClass.logs[i].activityID == activity.activityID )
                {
                    currentUserGoalCount += 1
                }
           // }
        }

            binding.tvLogsAmount.text = currentUserGoalCount.toString()


        fun goBackToHomeScreen ()
        {
            var intent = Intent(requireContext(), Home_Activity::class.java)
            startActivity(intent)
        }

        fun goToLogs ()
        {
            //set the sign in fragment to be the initial view
            fragmentControl.replaceFragmentAnim(View_Activity_Logs_Fragment(), R.id.fcFragmentContainer, parentFragmentManager, "Up")
        }

        binding.imgBackIndicator.setOnClickListener()
        {
            goBackToHomeScreen()
        }
        binding.tvBackText.setOnClickListener()
        {
            goBackToHomeScreen()
        }



        binding.tvViewLogsText.setOnClickListener()
        {
            goToLogs()
        }
        binding.tvLogsAmount.setOnClickListener()
        {
            goToLogs()
        }

            //-------------------------------------------------


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}