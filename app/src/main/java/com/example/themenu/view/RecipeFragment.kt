package com.example.themenu.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.themenu.databinding.FragmentRecipeBinding
import com.example.themenu.model.Recipe
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream
import java.lang.Exception


class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var secilenGorsel: Uri? = null
    private var secilenBitmap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener { save(it) }
        binding.deleteButton.setOnClickListener { delete(it) }
        binding.imageView.setOnClickListener { choosePic(it) }

        arguments?.let {
            val info = RecipeFragmentArgs.fromBundle(it).info

            if (info == "new") {
                // yeni bilgi eklenecek
                binding.deleteButton.isEnabled = false
                binding.saveButton.isEnabled = true
            } else {
                binding.deleteButton.isEnabled = true
                binding.saveButton.isEnabled = false
            }
        }

        registerLauncher()
    }

    fun save(view: View) {
        val name = binding.nameText.text.toString()
        val ingredient = binding.ingredientText.text.toString()
        if (secilenBitmap != null) {
            val smallBitmap = createSmallBitmap(secilenBitmap!!, 300)
            val outputStream = ByteArrayOutputStream()
            smallBitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
            val byteArray = outputStream.toByteArray()

            val recipe = Recipe(name, ingredient, byteArray)
        }

    }

    fun delete(view: View) {

    }

    fun choosePic(view: View) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {// izin verilmemiş
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                ) {
                    // snack bar göstermemiz lazım, kullanıcıdan neden izin istediğimizi açıklamak için
                    Snackbar.make(view, "Galeriden görsel seçmeliyiz", Snackbar.LENGTH_INDEFINITE)
                        .setAction(
                            "izin ver",
                            View.OnClickListener {
                                // izin isteyeceğiz
                                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)

                            }
                        ).show()
                } else {
                    // izin iste
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)

                }
            } else {
                // izin verilmiş galeriye git
                val intentToGaleri =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGaleri)
            }

        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {// izin verilmemiş
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    // snack bar göstermemiz lazım, kullanıcıdan neden izin istediğimizi açıklamak için
                    Snackbar.make(view, "Galeriden görsel seçmeliyiz", Snackbar.LENGTH_INDEFINITE)
                        .setAction(
                            "izin ver",
                            View.OnClickListener {
                                // izin isteyeceğiz
                                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                            }
                        ).show()
                } else {
                    // izin iste
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                }
            } else {
                // izin verilmiş galeriye git
                val intentToGaleri =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGaleri)
            }
        }
    }

    private fun registerLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    val intentFromResult = result.data
                    if (intentFromResult != null) {
                        secilenGorsel = intentFromResult.data
                        try {
                            if (Build.VERSION.SDK_INT >= 28) {
                                val source = ImageDecoder.createSource(
                                    requireActivity().contentResolver,
                                    secilenGorsel!!
                                )
                                secilenBitmap = ImageDecoder.decodeBitmap(source)
                                binding.imageView.setImageBitmap(secilenBitmap)
                            } else {
                                secilenBitmap = MediaStore.Images.Media.getBitmap(
                                    requireActivity().contentResolver,
                                    secilenGorsel
                                )
                                binding.imageView.setImageBitmap(secilenBitmap)
                            }
                        } catch (e: Exception) {
                            println(e.localizedMessage)
                        }


                    }
                }
            }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    //izin verildi galeriye gidebiliriz
                    val intentToGaleri =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGaleri)

                } else {
                    Toast.makeText(requireContext(), "izin verilmedi", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun createSmallBitmap(usersBitmap: Bitmap, maximumSize: Int): Bitmap {
        var width = usersBitmap.width
        var height = usersBitmap.height

        val bitmapSize: Double = width.toDouble() / height.toDouble()

        if (bitmapSize > 1) {
            // görsel yatay
            width = maximumSize
            val newHeight = width / bitmapSize
            height = newHeight.toInt()
        } else {
            // görsel dikey
            height = maximumSize
            val newWidth = height * bitmapSize
            width = newWidth.toInt()

        }
        return Bitmap.createScaledBitmap(usersBitmap, width, height, true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}