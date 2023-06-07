package com.example.opsc_poe

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import com.example.opsc_poe.GlobalClass.Companion.ReturnToHome
import com.example.opsc_poe.databinding.ActivityCreateCategoryBinding
import yuku.ambilwarna.AmbilWarnaDialog

class CreateCategory : AppCompatActivity()
{
    val default_Activity_Yellow = 16769154
    private var colorPreview: View? = null
    private var defaultcolour = default_Activity_Yellow //Activity Yellow color

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCreateCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Hide the action bar
        supportActionBar?.hide()

        //set status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.Dark_Green)

        //colour picker
        //--------------------------------------------------------------------------------
        colorPreview = findViewById<View>(R.id.preview_selected_color)
        val picker = findViewById<Button>(R.id.pick_color_button)
        picker.setOnClickListener()
        {
            openColorPickerDialogue()
        }
        //Return Home
        binding.imgBlackTurtle.setOnClickListener()
        {
            ReturnToHome(this)
        }


        //create category button
        //--------------------------------------------------------------------------------

            var categoryIDIndex = intent.getIntExtra("categoryIDIndex", -1)

            if (categoryIDIndex == -1)
            {

                binding.btnCreate.setOnClickListener()
                {
                    //create new category object
                    var category = Temp_CategoryDataClass(
                        categoryID = GlobalClass.categories.size + 1,
                        userID = GlobalClass.user.userID,
                        name = binding.etName.text.toString(),
                        description = binding.etDescription.text.toString(),
                        colour = intToColourString(defaultcolour)
                    )
                    //save to global class
                    GlobalClass.categories.add(category)

                    //back to home page
                    var intent = Intent(this, Home_Activity::class.java)
                    startActivity(intent)
                }

            }
            else
            {
                var category = GlobalClass.categories[categoryIDIndex]
                binding.etName.setText(category.name)
                binding.previewSelectedColor.backgroundTintList = ColorStateList.valueOf(Color.parseColor(category.colour))

                defaultcolour = category.colour.toColorInt()
                var previewColor = ColorStateList.valueOf(Color.parseColor(intToColourString(defaultcolour)))
                colorPreview?.backgroundTintList = previewColor

                binding.etDescription.setText(category.description)
                binding.tvScreenFunction.text = "Edit"
                binding.btnCreate.text = "Save"


                binding.btnCreate.setOnClickListener()
                {
                    category.name = binding.etName.text.toString()
                    category.colour = intToColourString(defaultcolour)
                    category.description = binding.etDescription.text.toString()

                    //back to home page
                    var intent = Intent(this, Home_Activity::class.java)
                    startActivity(intent)
                }
            }
        }

    //Color Picker Popup
    fun openColorPickerDialogue()
    {
        val colorPickerDialogue = AmbilWarnaDialog(this, defaultcolour,
            object : AmbilWarnaDialog.OnAmbilWarnaListener {
                override fun onCancel(dialog: AmbilWarnaDialog?) {
                    // leave this function body as blank
                }
                override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                    defaultcolour = color
                    var previewColor = ColorStateList.valueOf(Color.parseColor(intToColourString(defaultcolour)))
                    colorPreview?.backgroundTintList = previewColor
                }
            })
        colorPickerDialogue.show()
    }

    //Convert Color Int to String
    fun intToColourString(color: Int): String
    {
        return String.format("#%06X", 0xFFFFFF and color)
    }

    override fun onBackPressed() {}
}