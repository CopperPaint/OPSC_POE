package com.example.opsc_poe

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.opsc_poe.databinding.SignUpFragmentBinding

class sign_up_fragment : Fragment(R.layout.sign_up_fragment){


    private var _binding: SignUpFragmentBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SignUpFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        //-------------------------------------------------
        //code here

        //sign in button
        binding.tvSignUpButton.setOnClickListener {
            try
            {
                if (binding.etEmail.text.isNotEmpty() && binding.etUsername.text.isNotEmpty() && binding.etPassword.text.isNotEmpty())
                {

                    //

                    val trySignUp  =  Temp_UserDataClass()

                    var tryValidateUserEmail = trySignUp.ValidateUserEmail(binding.etEmail.text.toString())

                    var (validateUserPasswordBool, validateUserPasswordFeedback) = trySignUp.ValidateUserPassword(binding.etPassword.text.toString())


                    if (tryValidateUserEmail)
                    {
                        if (validateUserPasswordBool)
                        {

                            trySignUp.RegisterUser(
                                binding.etEmail.text.toString(),
                                binding.etUsername.text.toString(),
                                binding.etPassword.text.toString(),
                                requireContext()
                            )
                        }
                        else
                        {
                            GlobalClass.InformUser(getString(R.string.invalidPassword), validateUserPasswordFeedback, requireContext())
                        }
                    }
                    else
                    {
                        GlobalClass.InformUser(getString(R.string.invalidEmail), getString(R.string.invalidEmailMessage), requireContext())
                    }
                }
                else
                {
                    GlobalClass.InformUser(getString(R.string.inputErrorTitle),getString(R.string.incompleteFields), requireContext())
                }
            }
            catch (e: Error)
            {
                GlobalClass.InformUser(getString(R.string.errorTitle), "${e.toString()}", requireContext())
            }
            //-------------------------------------------------
        }
        binding.tvNeedHelpButton.setOnClickListener()
        {
            var intent = Intent(requireContext(), Help::class.java) //ViewActivity

            intent.putExtra(getString(R.string.previousScreenKey), getString(R.string.signUpScreenValue))
            startActivity(intent)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}