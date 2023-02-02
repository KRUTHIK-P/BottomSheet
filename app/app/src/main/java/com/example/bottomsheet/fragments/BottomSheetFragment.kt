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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.bottomsheet.R
import com.example.bottomsheet.databinding.FragmentBottomSheetBinding
import com.example.bottomsheet.interfaces.Communicator
import com.example.bottomsheet.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


@Suppress("DEPRECATION")
class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null

    private val binding get() = _binding!!

    private val selectImagePermissionCode = 1
    private val captureImagePermissionCode = 2
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.cameraLyt.setOnClickListener {
            if (checkPermissions(activity, captureImagePermissionCode)) {
                startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), captureImagePermissionCode)
            }
        }

        binding.galleryLyt.setOnClickListener {
            if (checkPermissions(activity, selectImagePermissionCode)) {
                val intent = Intent()
                intent.apply {
                    type = Constants.IMAGE_TYPE
                    putExtra(Intent.ACTION_GET_CONTENT, true)
                    action = Intent.ACTION_GET_CONTENT
                }

                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), selectImagePermissionCode)
            }
        }

        return view
    }


    private fun checkPermissions(activity: FragmentActivity?, i: Int): Boolean {
        return when (i) {
            selectImagePermissionCode -> if (ContextCompat.checkSelfPermission(
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


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val communicator = activity as Communicator
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == selectImagePermissionCode) {
                val selectedImageUri: Uri? = data?.data
                if (null != selectedImageUri) {
                    communicator.selectedImage(selectedImageUri)
                    this.dismiss()
                }
            }

            if (requestCode == captureImagePermissionCode) {
                val capturedImage = data?.extras?.get("data") as Bitmap
                communicator.capturedImage(capturedImage)
                this.dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}