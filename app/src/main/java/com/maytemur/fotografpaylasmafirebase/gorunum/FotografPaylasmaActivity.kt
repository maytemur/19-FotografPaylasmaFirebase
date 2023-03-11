package com.maytemur.fotografpaylasmafirebase.gorunum

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.maytemur.fotografpaylasmafirebase.R
import kotlinx.android.synthetic.main.activity_fotograf_paylasma.*
import java.util.*

class FotografPaylasmaActivity : AppCompatActivity() {
    var gorselUri: Uri? = null
    var gorselBitmap: Bitmap? = null
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent(),
                    ActivityResultCallback {
                        //handle uri
                        gorselUri = it
                        imageView.setImageURI(it)
                    })
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fotograf_paylasma)
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

    }

    fun paylas(view: View) {
        //depo işlemleri
        //UUID - universal unique id
        val uuid = UUID.randomUUID()
        val gorselIsmi = "${uuid}.jpg"

        val reference = storage.reference
        val gorselReference = reference.child("images").child(gorselIsmi)
        if (gorselUri != null) {
            //println("paylaş urisi ${gorselUri}")
            gorselReference.putFile(gorselUri!!).addOnSuccessListener {
                val yuklenenGorselReference = FirebaseStorage.getInstance().reference.child("images").child(gorselIsmi)
                yuklenenGorselReference.downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString()
                    val guncelKullaniciEmaili = auth.currentUser!!.email.toString()
                    val kullaniciYorumu = yorumText.text.toString()
                    val tarih = Timestamp.now()
                    //veritabanı işlemleri

                    val postHashMap = hashMapOf <String,Any> ()
                    postHashMap.put("gorselUrl",downloadUrl)
                    postHashMap.put("kullaniciEmail",guncelKullaniciEmaili)
                    postHashMap.put("kullaniciYorum",kullaniciYorumu)
                    postHashMap.put("tarih",tarih)

                    database.collection("postKoleksiyonu").add(postHashMap).addOnCompleteListener {
                        if (it.isSuccessful){
                            finish()
                        }
                    }.addOnFailureListener { exception ->
                        Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()


                    }
                }.addOnFailureListener { exception->
                    Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()

                }
            }
        }



    }

    fun gorselSec(view: View) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //izni almamışız
                println("gorsel sec izin iste calisti")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } else { //izin zaten var

            getContent.launch("image/*")
            println("gorselSecClick  ${gorselUri.toString()}")
//            if (gorselUri != null) {
//                if (Build.VERSION.SDK_INT >= 28) {
//                    val source = ImageDecoder.createSource(this.contentResolver,gorselUri!!)
//                    gorselBitmap = ImageDecoder.decodeBitmap(source)
//                    imageView.setImageBitmap(gorselBitmap)
//                }else {
//                    gorselBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,gorselUri)
//                    imageView.setImageBitmap(gorselBitmap)
//                }
//
//            }

    }
   }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==1) {
            if (grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //izin verilince yapılacaklar

                getContent.launch("image/*")
                println("onrequest  ${gorselUri.toString()}")
//                if (gorselUri != null) {
//                    if (Build.VERSION.SDK_INT >= 28) {
//                        val source = ImageDecoder.createSource(this.contentResolver,gorselUri!!)
//                        gorselBitmap = ImageDecoder.decodeBitmap(source)
//                        imageView.setImageBitmap(gorselBitmap)
//                    }else {
//                        gorselBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,gorselUri)
//                        imageView.setImageBitmap(gorselBitmap)
//                    }
//
//                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}



//val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//    uri?.let { gorselUri= uri }
//}