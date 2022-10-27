package com.example.mvvm_tests

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_tests.dialogFragmnet.VideoDetailsDialogFragment
import com.example.mvvm_tests.ui.main.models.*
import java.util.concurrent.Executors

class NetworkVideoViewAdapter():RecyclerView.Adapter<NetworkVideoViewAdapter.ViewHolder>() {
    private var VideoList = emptyList<DevByteVideo>()
    private var VideoListdetails = emptyList<DatabaseVideo>()
    var parentManager : FragmentManager? =null

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.title)
        val textViewDescription : TextView = itemView.findViewById(R.id.description)
        val imageViewThumbnail : ImageView = itemView.findViewById(R.id.thumbnail)
        val CardView_Click : CardView = itemView.findViewById(R.id.card)
    }

    fun SetData(Videos:List<DevByteVideo>){
        this.VideoList = Videos
        VideoListdetails = DatabaseVideoContainer(Videos).asDomainModel()
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.video_view_design, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = VideoList[position]
        holder.textViewTitle.text = itemsViewModel.title
        holder.textViewDescription.text = "Updated on :${itemsViewModel.updated.substring(0, 10)}"
        ImageAdd(itemsViewModel.thumbnail,holder.imageViewThumbnail)
        holder.CardView_Click.setOnClickListener {
            VideoDetailsDialogFragment(VideoListdetails[position]).show(parentManager!!,"")
        }
    }
    fun GetParantManager(Child:FragmentManager){
        this.parentManager =Child
    }
    override fun getItemCount(): Int {
     return  VideoList.size
    }

    fun ImageAdd(URL:String, imageView: ImageView){
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var image: Bitmap? = null
        executor.execute {
            val imageURL = "$URL"
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)
                handler.post {
                    imageView.setImageBitmap(image)
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}