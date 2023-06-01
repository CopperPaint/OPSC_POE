package com.example.opsc_poe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.opsc_poe.databinding.SignInFragmentBinding


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


            val test = ManageUser()
            var trying = test.logInUser(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            //binding.tvEmailPrompt.text = trying

            /*
//test code to show the create goal page
           var intent = Intent(activity, Create_Goal::class.java)
            startActivity(intent)

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