package com.example.notificationexercise

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notificationexercise.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //SHAREDPREFERENCES KOD BLOKLARI

        sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE)

        binding.button.setOnClickListener {
            val intent= Intent(this,MainActivity2::class.java)
            startActivity(intent)
        }

        binding.save.setOnClickListener {
            save()
        }
        binding.upload.setOnClickListener {
            upload()
        }
        binding.delete.setOnClickListener {
            delete()
        }
    }


    private fun save(){
        val editor=sharedPreferences.edit()

        val personName=binding.personName.text.toString()
        val password=binding.personPassword.text.toString()
        val age=binding.personAge.text.toString().toInt()

        editor.apply {
            putString("personName",personName)
            putString("password",password)
            putInt("age",age)
        }.apply()


        Toast.makeText(this,"Kayıt Başarılı",Toast.LENGTH_SHORT).show()
    }

    private fun upload(){
        val userName=sharedPreferences.getString("personName",null)
        val password=sharedPreferences.getString("password",null)
        val age=sharedPreferences.getInt("age",0)

        binding.textView.setText(" $userName ş $password yaş $age")
    }
    private fun delete(){
        val editor=sharedPreferences.edit()
        editor.clear().apply()

        Toast.makeText(this,"Silme Başarılı",Toast.LENGTH_SHORT).show()
    }
}