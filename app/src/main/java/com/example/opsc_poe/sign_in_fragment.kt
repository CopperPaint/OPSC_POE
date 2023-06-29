package com.example.opsc_poe

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.icu.util.Output
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.opsc_poe.databinding.SignInFragmentBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay


class sign_in_fragment : Fragment(R.layout.sign_in_fragment) {


    private var _binding: SignInFragmentBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SignInFragmentBinding.inflate(inflater, container, false)
        val view = binding.root


        //-------------------------------------------------
        //code here

        //sign in button
        binding.tvSignInButton.setOnClickListener {

            if (binding.etEmail.text.isNotEmpty() &&  binding.etPassword.text.isNotEmpty())
            {
                val trySignIn  =  Temp_UserDataClass()
                val trySubmitSignIn = trySignIn.ValidateUser(binding.etEmail.text.toString(),binding.etPassword.text.toString(), requireContext())



                if (trySubmitSignIn)
                {
                    //if sign in is successful then send user to the the home view screen
                    var intent = Intent(activity, Home_Activity::class.java)
                    startActivity(intent)
                }
            }
            else
            {
                GlobalClass.InformUser("Input Error","Please fill in all fields", requireContext())
            }



/*
val alert = AlertDialog.Builder(this)
        alert.setTitle(messageTitle)
        alert.setMessage(messageText)
        alert.setPositiveButton("OK", null)
        alert.show()
 */




            //-------------------------------------------------
        }

            return view
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }



}