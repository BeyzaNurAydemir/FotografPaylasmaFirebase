package com.beyzanuraydemir.fotografpaylasmafirebase.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.beyzanuraydemir.fotografpaylasmafirebase.R
import com.beyzanuraydemir.fotografpaylasmafirebase.adapter.HaberRecyclerAdapter
import com.beyzanuraydemir.fotografpaylasmafirebase.databinding.ActivityHaberlerBinding
import com.beyzanuraydemir.fotografpaylasmafirebase.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HaberlerActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityHaberlerBinding
    private lateinit var database : FirebaseFirestore
    private lateinit var recyclerViewAdapter: HaberRecyclerAdapter

    var postListesi = ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_haberler)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_haberler)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        verileriAl()
        var layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        recyclerViewAdapter = HaberRecyclerAdapter(postListesi)
        binding.recyclerView.adapter = recyclerViewAdapter

    }

    fun verileriAl(){
        database.collection("Post").orderBy("tarih",Query.Direction.DESCENDING).addSnapshotListener { snaphot, exception ->
            if(exception != null){
                Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if(snaphot != null){
                    //snapshot boş değilse ==false ile aynı anlamda
                    if(!snaphot.isEmpty){
                        //snapshotta kesin bir şey var
                        val documents = snaphot.documents

                        postListesi.clear()

                        for(document in documents){
                            val kullaniciEmail = document.get("kullaniciemail") as String
                            val kullaniciYorumu = document.get("kullaniciyorum") as String
                            val gorselUrl = document.get("gorselurl") as String

                            val indirilenPost = Post(kullaniciEmail,kullaniciYorumu,gorselUrl)
                            postListesi.add(indirilenPost)

                        }
                        recyclerViewAdapter.notifyDataSetChanged()

                    }
                }
            }

        }

    }

    //Menuyu baglamak için gerekli fonk. override et
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Menuyu bagliyoruz
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.secenekler_menusu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Item secim kontrolu
        if(item.itemId == R.id.fotograf_paylas){
            //Fotograf paylasma activitye git
            val intent = Intent(this, FotografPaylasmaActivity::class.java)
            startActivity(intent)
        }else if (item.itemId == R.id.cikis_yap){
            auth.signOut()   //firebaseden cikis yap
            val intent = Intent(this, KullaniciActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}