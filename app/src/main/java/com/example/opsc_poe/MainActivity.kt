package com.example.opsc_poe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.opsc_poe.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        //Hide the action bar
        supportActionBar?.hide()

        try
        {
            //set status bar color
            window.statusBarColor = ContextCompat.getColor(this, R.color.Dark_Green)

            //Set view binding
            val binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            //create local fragment controller
            val fragmentControl = FragmentHandler()

            //set the sign in fragment to be the initial view
            fragmentControl.replaceFragment(sign_in_fragment(), R.id.fcFragmentContainer, supportFragmentManager)

            //get the intent value if it exists
            var showSignUp = intent.getBooleanExtra("LoadSignUp", false)

            if (showSignUp)
            {
                fragmentControl.replaceFragment(sign_up_fragment(), R.id.fcFragmentContainer, supportFragmentManager)
            }

            //Sign in view activation
            binding.tvSignIn.setOnClickListener{
                //replaceFragment(sign_in_fragment())
                fragmentControl.replaceFragmentAnim(sign_in_fragment(), R.id.fcFragmentContainer, supportFragmentManager, "Left_Half", this)
            }

            //Sign up view activation
            binding.tvSignUp.setOnClickListener{
                //replaceFragment(sign_up_fragment())
                fragmentControl.replaceFragmentAnim(sign_up_fragment(), R.id.fcFragmentContainer, supportFragmentManager, "Right_Half", this)
            }
        }
        catch (e: Error)
        {
            GlobalClass.InformUser("Error", "${e.toString()}", this)
        }
    }

  /*  private fun replaceFragment(fragment : Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fcFragmentContainer, fragment)
        fragmentTransaction.commit()
    }*/
  override fun onBackPressed() {}
}