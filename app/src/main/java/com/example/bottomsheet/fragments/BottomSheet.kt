package com.example.bottomsheet.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.bottomsheet.R
import com.example.bottomsheet.databinding.FragmentBottomSheetBinding
import com.example.bottomsheet.interfaces.Communicator
import com.example.bottomsheet.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.cameraLyt.setOnClickListener {
            if (checkPermissions(activity, Constants.CAPTURE_IMAGE_PERMISSION_CODE)) {
                captureImageLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
            }
        }

        binding.galleryLyt.setOnClickListener {
            if (checkPermissions(activity, Constants.SELECT_IMAGE_PERMISSION_CODE)) {
                val intent = Intent()
                intent.apply {
                    type = Constants.IMAGE_TYPE
                    putExtra(Intent.ACTION_GET_CONTENT, true)
                    action = Intent.ACTION_GET_CONTENT
                }

                selectImageLauncher.launch(
                    Intent.createChooser(
                        intent,
                        getString(R.string.select_picture)
                    )
                )
            }
        }

        return view
    }

    private val captureImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val communicator = activity as Communicator
                val data: Intent? = result.data
                val capturedImage = data?.extras?.get("data") as Bitmap
                communicator.onCapturedImage(capturedImage)
                this.dismiss()
            }
        }

    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val communicator = activity as Communicator
                val data: Intent? = result.data

                val selectedImageUri: Uri? = data?.data
                if (null != selectedImageUri) {
                    communicator.onSelectedImage(selectedImageUri)
                    this.dismiss()
                }
            }
        }


    private fun checkPermissions(activity: FragmentActivity?, i: Int): Boolean {
        return when (i) {
            Constants.SELECT_IMAGE_PERMISSION_CODE -> if (ContextCompat.checkSelfPermission(
                    binding.root.context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (activity != null) {
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        PackageManager.PERMISSION_GRANTED
                    )
                }

                false
            } else {
                true
            }

            else -> if (ContextCompat.checkSelfPermission(
                    binding.root.context,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (activity != null) {
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.CAMERA),
                        PackageManager.PERMISSION_GRANTED
                    )
                }

                false
            } else {
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}