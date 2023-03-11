package com.maytemur.fotografpaylasmafirebase.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.core.View
import com.maytemur.fotografpaylasmafirebase.R
import com.maytemur.fotografpaylasmafirebase.model.PostKoleksiyonu
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_satir.view.*
import java.util.zip.Inflater

class HaberRcyclerAdaptor(val postListesi:ArrayList<PostKoleksiyonu>): RecyclerView.Adapter<HaberRcyclerAdaptor.PostTutucu>() {
    class PostTutucu (itemView: android.view.View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostTutucu {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_satir,parent,false)
        return PostTutucu(view)
    }

    override fun onBindViewHolder(holder: PostTutucu, position: Int) {
        holder.itemView.recyler_satir_kullanici_email.text = postListesi[position].kullaniciEmail
        holder.itemView.recycler_satir_kullanici_yorum.text = postListesi[position].kullaniciYorum
        //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);  -- kullanış şekli
        Picasso.get().load(postListesi[position].gorselUrl).into(holder.itemView.recycler_satir_imageview)

    }

    override fun getItemCount(): Int {
        return postListesi.size

    }

}