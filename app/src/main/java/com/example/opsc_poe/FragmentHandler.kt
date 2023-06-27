package com.example.opsc_poe

import android.R
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction



class FragmentHandler {
//class to handle to use of fragments and navigating between them


    //method to switch between fragments (uses the desired fragment, the ID of the fragment container and the supportFragmentManager)


    public fun replaceFragmentAnim(fragment : Fragment, fragmentContainerID : Int, fragmentManager : FragmentManager, anim : String) {
        //begin transition to desired fragment
        val fragmentTransaction = fragmentManager.beginTransaction()

        //var to hold animation
        var chosenAnim = 0

        //choose a fragment animation
        when (anim) {
            "Up" -> {
                //chosenAnim = com.example.opsc_poe.R.anim.slide_up

                //define fragment animations
                fragmentTransaction.setCustomAnimations(
                    com.example.opsc_poe.R.anim.slide_in_up,
                    com.example.opsc_poe.R.anim.slide_out_down,
                    com.example.opsc_poe.R.anim.slide_in_down,
                    com.example.opsc_poe.R.anim.slide_out_up
                )

            }
            "Down" -> {
                //chosenAnim = com.example.opsc_poe.R.anim.slide_down

                //define fragment animations
                fragmentTransaction.setCustomAnimations(

                    com.example.opsc_poe.R.anim.slide_in_down,
                    com.example.opsc_poe.R.anim.slide_out_up,
                    com.example.opsc_poe.R.anim.slide_in_up,
                    com.example.opsc_poe.R.anim.slide_out_down


                )

            }
            "Left" -> {
                //chosenAnim = com.example.opsc_poe.R.anim.slide_left

                //define fragment animations
                fragmentTransaction.setCustomAnimations(
                    com.example.opsc_poe.R.anim.slide_in_left,
                    com.example.opsc_poe.R.anim.slide_out_right,
                    com.example.opsc_poe.R.anim.slide_in_right,
                    com.example.opsc_poe.R.anim.slide_out_left
                )
            }
            "Right" -> {
                //chosenAnim = com.example.opsc_poe.R.anim.slide_right

                //define fragment animations
                fragmentTransaction.setCustomAnimations(
                    com.example.opsc_poe.R.anim.slide_in_right,
                    com.example.opsc_poe.R.anim.slide_out_left,
                    com.example.opsc_poe.R.anim.slide_in_left,
                    com.example.opsc_poe.R.anim.slide_out_right
                )
            }
            "Left_Half" -> {
                //chosenAnim = com.example.opsc_poe.R.anim.slide_left_half

                //define fragment animations
                fragmentTransaction.setCustomAnimations(
                    com.example.opsc_poe.R.anim.slide_in_left_half,
                    com.example.opsc_poe.R.anim.slide_out_right_half,
                    com.example.opsc_poe.R.anim.slide_in_right_half,
                    com.example.opsc_poe.R.anim.slide_out_left_half
                )
            }
            "Right_Half" -> {
                //chosenAnim = com.example.opsc_poe.R.anim.slide_right_half

                //define fragment animations
                fragmentTransaction.setCustomAnimations(
                    com.example.opsc_poe.R.anim.slide_in_right_half,
                    com.example.opsc_poe.R.anim.slide_out_left_half,
                    com.example.opsc_poe.R.anim.slide_in_left_half,
                    com.example.opsc_poe.R.anim.slide_out_right_half
                )
            }
        }



        //set the desired fragment to be swapped
        fragmentTransaction.replace(fragmentContainerID, fragment)

        //commit fragment change
        fragmentTransaction.commit()
    }



    public fun replaceFragment(fragment : Fragment, fragmentContainerID : Int, fragmentManager : FragmentManager) {
        //begin transition to desired fragment
        val fragmentTransaction = fragmentManager.beginTransaction()

        //set the desired fragment to be swapped
        fragmentTransaction.replace(fragmentContainerID, fragment)

        //commit fragment change
        fragmentTransaction.commit()
    }








}