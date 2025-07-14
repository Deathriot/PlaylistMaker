package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
class SearchActivity : AppCompatActivity() {

    var editTextValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val editText = findViewById<EditText>(R.id.search_edit_text)
        val clearBtn = findViewById<ImageView>(R.id.btn_clear_text_search)
        val backBtn = findViewById<ImageButton>(R.id.btn_settings_back)

        backBtn.setOnClickListener({
            finish()
        })

        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

        if(savedInstanceState != null){
            editText.setText(editTextValue)
        }

        clearBtn.setOnClickListener({
            editText.setText("")
            editTextValue = ""
            inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
        })

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearBtn.visibility = setButtonVisibility(s)

                if (!s.isNullOrEmpty()) {
                    editTextValue = s.toString()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        editText.addTextChangedListener(textWatcher)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(editTextKey, editTextValue)
    }

    override fun onResume() {
        super.onResume()
        val editText = findViewById<EditText>(R.id.search_edit_text)
        editText.setText(editTextValue)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editTextValue = savedInstanceState.getString(editTextKey, "")
    }

    private fun setButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
    }

    companion object{
        const val editTextKey = "editText"
    }
}