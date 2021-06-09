@file:Suppress("DEPRECATION")

package com.capstoneproject.silimbah.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstoneproject.silimbah.adapter.CraftAdapter
import com.capstoneproject.silimbah.databinding.ActivityMainBinding
import com.capstoneproject.silimbah.ml.MobilenetV210224Quant
import com.capstoneproject.silimbah.model.DataCraft
import com.capstoneproject.silimbah.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var backPressed: Long = 0
    private lateinit var adapter: CraftAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var thumbnail: Bitmap

    companion object{
        private const val CAMERA_REQUEST = 111
        private const val CAMERA_PERMISSION = 222
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(false)
        adapter = CraftAdapter()
        adapter.notifyDataSetChanged()
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        binding.fabCamera.setOnClickListener{
            if(ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST)
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION
                )
            }
        }

        showRecycle()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST)
            } else{
                Toast.makeText(this, "sorry", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val labels = application.assets.open("labels.txt").bufferedReader().use { it.readText() }.split("\n")

        if (resultCode == Activity.RESULT_OK){
            if (requestCode == CAMERA_REQUEST){
                thumbnail = data!!.extras!!.get("data") as Bitmap
                binding.imageResult.setImageBitmap(thumbnail)

                val resized = Bitmap.createScaledBitmap(thumbnail, 224, 224, true)
                val model = MobilenetV210224Quant.newInstance(this)
                val tBuffer = TensorImage.fromBitmap(resized)
                val byteBuffer = tBuffer.buffer

// Creates inputs for reference.
                val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
                inputFeature0.loadBuffer(byteBuffer)


// Runs model inference and gets result.
                val outputs = model.process(inputFeature0)
                val outputFeature0 = outputs.outputFeature0AsTensorBuffer
                val max = getMax(outputFeature0.floatArray)
                binding.textView.text = labels[max]

// Releases model resources if no longer used.
                model.close()
            }
        }
    }
    private fun getMax(arr:FloatArray) : Int{
        var index = 0
        var min = 0.0f

        for(i in 0..5){
            if(arr[i] > min){
                min = arr[i]
                index = i
            }
        }
        return index
    }

    private fun showRecycle() {
        binding.rvCraft.setHasFixedSize(true)
        binding.rvCraft.adapter = adapter
        binding.rvCraft.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)

        binding.searchButton.setOnClickListener {
            val craft = textView.text
            if (craft.isEmpty()) return@setOnClickListener
            showLoading(true)
            viewModel.setSearchUser(craft as String)

            viewModel.getsearchUser().observe(this,{
                if (it!=null){
                    adapter.setData(it)
                    showLoading(false)
                }
            })
        }

        adapter.setOnItemClickCallback(object : CraftAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DataCraft) {
                val moveWithObjectIntent = Intent(this@MainActivity, WebViewActivity::class.java)
                moveWithObjectIntent.putExtra(WebViewActivity.EXTRA_URL, data.url)
                startActivity(moveWithObjectIntent)
            }
        })
    }

    private fun showLoading(state: Boolean){
        if(state){
            binding.progressBar.visibility = View.VISIBLE
        } else{
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            return
        } else {
            Toast.makeText(baseContext, "Press again to exit", Toast.LENGTH_SHORT).show()
        }
        backPressed = System.currentTimeMillis()
    }
}