package com.example.kisileruygulamasi

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kisileruygulamasi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),SearchView.OnQueryTextListener {
    private lateinit var tasarim:ActivityMainBinding

    private lateinit var kisilerListe:ArrayList<Kisiler>
    private lateinit var adapter:KisilerAdapter

    private lateinit var vt:VeritabaniYardimcisi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tasarim = ActivityMainBinding.inflate(layoutInflater)
        setContentView(tasarim.root)

        tasarim.toolbar.title = "Kişiler Uygulaması"
        setSupportActionBar(tasarim.toolbar)

        tasarim.rv.setHasFixedSize(true)                                           // Tasarımın daha iyi görünmesini sağlıyor
        tasarim.rv.layoutManager = LinearLayoutManager(this)                //RecyclerView in alt alta görünmesini sağlıyor

        vt = VeritabaniYardimcisi(this)

        tumKisilerAl()

        tasarim.fab.setOnClickListener {

            alertGoster()
        }



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)

        val item = menu?.findItem(R.id.action_ara)     //Arama özelliğini burada aktif ediyoruz. Bu satırda arama tasarımından nesne oluşturduk
        val searchView = item?.actionView as SearchView   //Burada item nesnesine SearchView özelliğini verdik
        searchView.setOnQueryTextListener(this)         //Burada searchView nesnesini aktif ettik

        return super.onCreateOptionsMenu(menu)
    }

    fun alertGoster(){

        val tasarim = LayoutInflater.from(this).inflate(R.layout.alert_tasarim,null)
        val editTextAd = tasarim.findViewById(R.id.editTextAd) as EditText
        val editTextTel = tasarim.findViewById(R.id.editTextTel) as EditText

        val ad = AlertDialog.Builder(this)

        ad.setTitle("Kişi Ekle")
        ad.setView(tasarim)
        ad.setPositiveButton("Ekle"){dialogInterface, i ->
            val kisi_Ad = editTextAd.text.toString().trim()
            val kisi_Tel = editTextTel.text.toString().trim()

            Kisilerdao().kisiEkle(vt,kisi_Ad,kisi_Tel)
            tumKisilerAl()

            Toast.makeText(applicationContext,"$kisi_Ad kaydedildi",Toast.LENGTH_SHORT).show()

        }

        ad.setNegativeButton("İptal"){dialogInterface, i ->

        }

        ad.create().show()
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        aramaYap(query)

        Log.e("Gönderilen Arama",query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        aramaYap(newText)

        Log.e("Harf Girdikçe", newText)

        return true
    }

    fun tumKisilerAl(){

        kisilerListe = Kisilerdao().tumKisiler(vt)

        adapter = KisilerAdapter(this,kisilerListe,vt)

        tasarim.rv.adapter = adapter

    }

    fun aramaYap(aramaKelime:String){

        kisilerListe = Kisilerdao().kisiAra(vt,aramaKelime)

        adapter = KisilerAdapter(this,kisilerListe,vt)

        tasarim.rv.adapter = adapter

    }

}