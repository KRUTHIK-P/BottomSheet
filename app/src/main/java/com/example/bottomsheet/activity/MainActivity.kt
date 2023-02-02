package com.example.bottomsheet.activity

import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bottomsheet.fragments.BottomSheet
import com.example.bottomsheet.interfaces.Communicator
import com.example.bottomsheet.databinding.ActivityMainBinding
import com.example.bottomsheet.utils.Constants


class MainActivity : AppCompatActivity(), Communicator {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.addImageBtn.setOnClickListener {
            val bottomSheet = BottomSheet()
            bottomSheet.show(supportFragmentManager, Constants.TAG)
        }
    }

    override fun onCapturedImage(image: Bitmap) {
        binding.image.setImageBitmap(image)
    }

    override fun onSelectedImage(image: Uri) {
        binding.image.setImageURI(image)
    }


}