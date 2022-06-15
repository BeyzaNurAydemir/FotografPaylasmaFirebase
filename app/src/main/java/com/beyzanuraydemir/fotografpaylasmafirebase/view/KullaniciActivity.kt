package com.beyzanuraydemir.fotografpaylasmafirebase.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.beyzanuraydemir.fotografpaylasmafirebase.R
import com.beyzanuraydemir.fotografpaylasmafirebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class KullaniciActivity : AppCompatActivity() {

    //FirebaseAuth nesnesi oluştur
    private lateinit var auth : FirebaseAuth
    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //FirebaseAuth başlat
        auth = FirebaseAuth.getInstance()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //Kullanıcı bir defa girince hatırlatma direk uygulamayı açar.
        val guncelKullanici = auth.currentUser
        if(guncelKullanici!=null){
            val intent = Intent(this, HaberlerActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun girisYap(view: View){
        auth.signInWithEmailAndPassword(binding.emailText.text.toString(),binding.passwordText.text.toString()).addOnCompleteListener { task->
            if(task.isSuccessful){

                val guncelKullanici = auth.currentUser?.email.toString()
                Toast.makeText(this,"Welcome: ${guncelKullanici}",Toast.LENGTH_LONG).show()

                val intent = Intent(this, HaberlerActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }


    }

    fun kayitOl(view: View){
        val email = binding.emailText.text.toString()
        val sifre = binding.passwordText.text.toString()

        auth.createUserWithEmailAndPassword(email,sifre).addOnCompleteListener {task ->
            //asenkron çalışır
            if(task.isSuccessful){
                //diğer activitye git
                val intent = Intent(this, HaberlerActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            //hata olursa mesaj ver
            Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }

    }
}