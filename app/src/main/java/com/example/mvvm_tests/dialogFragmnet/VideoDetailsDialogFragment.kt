package com.example.mvvm_tests.dialogFragmnet

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm_tests.databinding.DialogFragmentErrorLayoutBinding
import com.example.mvvm_tests.databinding.VideoDialogFragmentLayoutBinding
import com.example.mvvm_tests.ui.main.models.DatabaseVideo
import java.util.concurrent.Executors

class VideoDetailsDialogFragment(Data:DatabaseVideo): DialogFragment() {
   val VideoDetails:DatabaseVideo = Data

    private var _binding: VideoDialogFragmentLayoutBinding? = null
    private val binding get() = _binding!!

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            _binding = VideoDialogFragmentLayoutBinding.inflate(inflater, container, false)
            return binding.root
        }

    override fun onResume() {
        super.onResume()
        dialog!!.window!!.setLayout(1000,1500)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.closeButton.setOnClickListener {
            this.dismiss()
        }
        ImageAdd(VideoDetails.thumbnail,binding.ThumbnailView)
        binding.TitleView.text = VideoDetails.title
        binding.description.text = VideoDetails.description
        binding.updated.text = "Updated on :${VideoDetails.updated.substring(0, 10)}"

    }

    fun showME(){
        parentFragment.let { this.show(it!!.childFragmentManager,"") }
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