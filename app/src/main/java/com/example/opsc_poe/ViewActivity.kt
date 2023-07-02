package com.example.opsc_poe

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.opsc_poe.databinding.ActivityViewBinding

class ViewActivity : AppCompatActivity()
{
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val binding = ActivityViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Hide the action bar
        supportActionBar?.hide()

        //set status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.Dark_Green)

        //get activity index
        val activityIDIndex = intent.getIntExtra(getString(R.string.activityIdentityIndex), 0)

        //get activity
        var activity = GlobalClass.activities[activityIDIndex]
        //get activity category index
        var catIndex = Temp_CategoryDataClass().GetIndex(activity.categoryID, GlobalClass.categories)
        //get activity category
        var category = GlobalClass.categories[catIndex]

        //activity name
        binding.tvActivity.text = activity.name
        //category name
        binding.tvCategory.text = category.name

        //create local fragment controller
        val fragmentControl = FragmentHandler()

        //set the sign in fragment to be the initial view
        fragmentControl.replaceFragment(View_Activity_Details_Fragment(), R.id.fcFragmentContainer, supportFragmentManager)

        //settings button
        binding.imgSettingsButton.setOnClickListener()
        {
            try
            {
                var intent = Intent(this, settings_view::class.java)
                intent.putExtra(getString(R.string.previousScreenKey), getString(R.string.activityView))
                intent.putExtra(getString(R.string.currentDataIDKey), activityIDIndex)
                startActivity(intent)
            }
            catch (e: Error)
            {
                GlobalClass.InformUser(getString(R.string.errorTitle), e.toString(), this)
                //return user to the sign in screen
                var intent = Intent(this, MainActivity::class.java) //ViewActivity
                startActivity(intent)
            }
        }
    }
    override fun onBackPressed() {}
}