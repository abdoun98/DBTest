package com.example.testdb

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.testdb.fragments.AddFragment
import com.example.testdb.fragments.CameraFragment
import com.example.testdb.fragments.DataFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val addFragment = AddFragment()
    private val cameraFragment = CameraFragment()
    private val dataFragment = DataFragment()
    //private val navBar: BottomNavigationView = findViewById(R.id.nav_bar)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(cameraFragment)

        /*
        navBar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.camera_ic -> replaceFragment(cameraFragment)
                R.id.add_ic -> replaceFragment(addFragment)
                R.id.data_ic -> replaceFragment(dataFragment)
            }
            true
        }*/

        var helper = DBHandler(applicationContext)
        // instance of database
        var db = helper.readableDatabase
        var rs = db.rawQuery("SELECT * FROM BETAIL", null)

        if(rs.moveToNext()){
            Toast.makeText(applicationContext, rs.getString(1), Toast.LENGTH_LONG).show()
        }

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener{
            view -> SaveInDB(view)
        }

    }

    private fun SaveInDB(view: View) {

        val code = findViewById<EditText>(R.id.editTextNumber).text.toString()

        val rg = findViewById<RadioGroup>(R.id.radioGroup)
        val checkedID: Int = rg.checkedRadioButtonId
        val sexe = radioSelected(checkedID)

        val mere = findViewById<EditText>(R.id.editTextMere).text.toString()
        val pere = findViewById<EditText>(R.id.editTextPere).text.toString()

        val dbHandler: DBHandler = DBHandler(this)

        if (!code.isEmpty() && !sexe.isEmpty() && !mere.isEmpty() && !pere.isEmpty()) {
            val status = dbHandler.writeData(AnimalClass(code.toInt(), sexe, mere.toInt(), pere))
            if (status > -1) {
                Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()
                findViewById<EditText>(R.id.editTextNumber).text.clear()
                findViewById<RadioGroup>(R.id.radioGroup).clearCheck()
                findViewById<EditText>(R.id.editTextMere).text.clear()
                findViewById<EditText>(R.id.editTextPere).text.clear()

            }
        } else {
            Toast.makeText(applicationContext, "Problem", Toast.LENGTH_LONG).show()
        }
    }

    private fun radioSelected(checkedID: Int): String {
        when(checkedID){
            R.id.radioButtonF -> return "Femelle"
            R.id.radioButtonM -> return "Male"
            else -> {return "pas specifie"}
        }
    }

    private fun replaceFragment(fragment: Fragment){
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }

}