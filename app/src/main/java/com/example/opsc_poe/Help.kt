package com.example.opsc_poe

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.opsc_poe.databinding.ActivityHelpBinding

class Help : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        //Set view binding
        val binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Hide the action bar
        supportActionBar?.hide()

        //set status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.Dark_Green)

        //get previous screen
        var previousScreen = intent.getStringExtra(getString(R.string.previousScreenKey))

        //messages for help
        when (previousScreen)
        {
            getString(R.string.createGoalScreenValue) -> {
                binding.tvHelpName.text = getString(R.string.CreateGoalHelpHeading)
               binding.tvHelpMessage.text = getString(R.string.CreateGoalHelp)
            }
            getString(R.string.createActivityScreenValue) -> {
                binding.tvHelpName.text = getString(R.string.CreateActivityHelpHeading)
                binding.tvHelpMessage.text = getString(R.string.CreateActivityHelp)
            }
            getString(R.string.signUpScreenValue) -> {
                //return user to the initial view screen
                binding.tvHelpName.text = getString(R.string.SignUpHelpHeading)
                binding.tvHelpMessage.text = getString(R.string.SignUpHelp)
            }

        }

        //method to go back
        fun GoBack(previousScreenVar: String)
        {
            try
            {
                when (previousScreenVar)
                {
                    getString(R.string.createGoalScreenValue) -> {
                        var returningActivityID = intent.getIntExtra(getString(R.string.currentActivityIndex), 0)
                        var returningGoalID = intent.getIntExtra(getString(R.string.currentGoalIdentityIndex), 0)

                        //return user to the initial view screen
                        var intent = Intent(this, Create_Goal::class.java)
                        //GlobalClass.InformUser("on Help", returningActivityID.toString(), this)
                        intent.putExtra(getString(R.string.currentActivityIndex), returningActivityID)
                        intent.putExtra(getString(R.string.currentGoalIdentityIndex), returningGoalID)
                        startActivity(intent)
                    }
                    getString(R.string.createActivityScreenValue) -> {
                        var returningActivityID = intent.getIntExtra(getString(R.string.activityIdentityIndex), 0)

                        //return user to the initial view screen
                        var intent = Intent(this, CreateActivity::class.java)
                        intent.putExtra(getString(R.string.activityIdentityIndex), returningActivityID)
                        startActivity(intent)
                    }
                    getString(R.string.signUpScreenValue) -> {
                        //return user to the initial view screen
                        var intent = Intent(this, MainActivity::class.java)
                        intent.putExtra(getString(R.string.loadSignUpKey), true)
                        startActivity(intent)
                    }

                }
            }
            catch (e: Error)
            {
                GlobalClass.InformUser(getString(R.string.errorTitle), e.toString(), this)
                //return user to the sign in screen
                var intent = Intent(this, MainActivity::class.java) //ViewActivity
                startActivity(intent)
            }
        }

        //back button
        binding.tvBackText.setOnClickListener()
        {
            try
            {
                if (previousScreen != null)
                {
                    GoBack(previousScreen)
                }
            }
            catch (e: Error)
            {
                GlobalClass.InformUser(getString(R.string.errorTitle), e.toString(), this)
                //return user to the sign in screen
                var intent = Intent(this, MainActivity::class.java) //ViewActivity
                startActivity(intent)
            }
        }

        //back image
        binding.imgBackIndicator.setOnClickListener()
        {
            try
            {
                if (previousScreen != null)
                {
                    GoBack(previousScreen)
                }
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