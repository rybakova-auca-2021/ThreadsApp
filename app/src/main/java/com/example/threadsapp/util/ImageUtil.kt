package com.example.threadsapp.util

import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class ImageUtil {
    companion object {
        private const val PICK_IMAGE_REQUEST = 1

        fun chooseImage(fragment: Fragment) {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            fragment.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
        }

        fun loadImage(fragment: Fragment, imageView: ImageView, uri: Uri) {
            Glide.with(fragment)
                .load(uri)
                .into(imageView)
        }
    }
}
