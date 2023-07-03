package com.example.opsc_poe

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.opsc_poe.databinding.SignInFragmentBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

        try {
            //Read Data
            GlobalScope.launch{
                if (GlobalClass.UpdateDataBase == true)
                {
                    var DBManger = ManageDatabase()

                    GlobalClass.categories = DBManger.getCategoriesFromFirestore(GlobalClass.user.userID)
                    GlobalClass.allUsers = DBManger.getAllUsersFromFirestore()
                    GlobalClass.activities = DBManger.getActivitesFromFirestore(GlobalClass.user.userID)
                    GlobalClass.goals = DBManger.getGoalsFromFirestore(GlobalClass.user.userID)
                    GlobalClass.logs = DBManger.getLogsFromFirestore(GlobalClass.user.userID)
                    GlobalClass.UpdateDataBase = false
                }
                //withContext(Dispatchers.Main) {
                //    UpdateUI()
                //}
            }
        }
        catch (e: Error)
        {
            GlobalClass.InformUser(getString(R.string.errorTitle), "${e.toString()}", requireContext())
        }

        //sign in button
        binding.tvSignInButton.setOnClickListener {

            if (binding.etEmail.text.isNotEmpty() &&  binding.etPassword.text.isNotEmpty())
            {
                val trySignIn  =  Temp_UserDataClass()
                val trySubmitSignIn = trySignIn.ValidateUser(binding.etEmail.text.toString(),binding.etPassword.text.toString(), requireContext())



                if (trySubmitSignIn)
                {
                    //GlobalClass.InformUser(GlobalClass.user.userID.toString(), "", requireContext())
                    //if sign in is successful then send user to the the home view screen
                    var intent = Intent(activity, Home_Activity::class.java)
                    startActivity(intent)
                }
            }
            else
            {
                GlobalClass.InformUser(getString(R.string.inputErrorTitle),getString(R.string.incompleteFields), requireContext())
            }

        }

        binding.tvForgotPasswordButton.setOnClickListener()
        {

            val etPopUp = EditText(requireContext())
            etPopUp.hint = getString(R.string.askForEmail)

            var alert = AlertDialog.Builder(requireContext())
            alert.setTitle(getString(R.string.forgotPasswordTitle))
            alert.setMessage(getString(R.string.forgotPasswordMessage))
            alert.setView(etPopUp)
            alert.setPositiveButton(getString(R.string.alertSend), DialogInterface.OnClickListener { dialog, whichButton -> })
            alert.setNegativeButton(getString(R.string.alertCancel), DialogInterface.OnClickListener { dialog, whichButton -> })
            alert.show()

        }
            return view
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
}