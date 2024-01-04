package com.example.passportphotocomparisonthesis.FaceRecognition

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.passportphotocomparisonthesis.R
import com.example.passportphotocomparisonthesis.databinding.DetailsLayoutBinding
import com.example.passportphotocomparisonthesis.databinding.FragmentFaceComparisonBinding
import com.example.passportphotocomparisonthesis.ml.Facenet
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.pow
import kotlin.math.sqrt

class FaceComparisonFragment : Fragment() {

    private lateinit var binding: FragmentFaceComparisonBinding
    private val args: FaceComparisonFragmentArgs by navArgs()
    private var dialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFaceComparisonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val resizedDocPhoto = Bitmap.createScaledBitmap(args.documentPhoto, 160, 160, true)
        val resizedSelfiePhoto = Bitmap.createScaledBitmap(args.selfiePhoto, 160, 160, true)

        binding.userDocumentImageView.setImageBitmap(resizedDocPhoto)
        binding.userSelfieImageView.setImageBitmap(resizedSelfiePhoto)

        val documentPhotoTensor = getEmbeddings(getByteBufferFromBitmap(resizedDocPhoto))
        val selfiePhotoTensor = getEmbeddings(getByteBufferFromBitmap(resizedSelfiePhoto))

        val distance = euclideanDistance(documentPhotoTensor, selfiePhotoTensor)

        binding.resultTextView.setOnClickListener {
            if (distance < 0.7) {
                showDetailsDialog(distance)
            } else {
                showDetailsDialog(distance)
            }
        }


        if (distance < 0.7) {
            binding.resultTextView.text = "Photos Are Matching \n" + " (Click for more info)"
            binding.resultTextView.setTextColor(Color.parseColor("#45B45C"))
            binding.resultImageView.setImageResource(R.drawable.ic_same_person)
        } else {
            binding.resultTextView.text = "Photos Don't Match \n" + " (Click for more info)"
            binding.resultTextView.setTextColor(Color.parseColor("#B44545"))
            binding.resultImageView.setImageResource(R.drawable.ic_not_same_person)

        }

    }

    private fun showDetailsDialog(distance: Float) {

         var dialogBinding = DetailsLayoutBinding.inflate(layoutInflater, null, false)

        dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        // Set the text programmatically
        dialogBinding.distanceTextView.text = String.format("%.2f", distance)

        dialog!!.show()

    }

    // Function to calculate Euclidean distance
    fun euclideanDistance(embedding1: TensorBuffer, embedding2: TensorBuffer): Float {
        var sum = 0.0f
        for (i in 0 until embedding1.floatArray.size) {
            sum += (embedding1.floatArray[i] - embedding2.floatArray[i]).pow(2)
        }
        return sqrt(sum)
    }

    fun getEmbeddings(byteBuffer: ByteBuffer): TensorBuffer {

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 160, 160, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        val model = Facenet.newInstance(requireContext())
        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        // Releases model resources if no longer used.
        model.close()
        return outputFeature0

    }

    fun getByteBufferFromBitmap(bitmap: Bitmap): ByteBuffer{
        // Convert Bitmap to ByteBuffer

        val byteBuffer = ByteBuffer.allocateDirect(4 * 1 * 160 * 160 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(160 * 160)
        bitmap.getPixels(
            intValues,
            0,
            bitmap.width,
            0,
            0,
            bitmap.width,
            bitmap.height
        )
        var pixel = 0
        for (i in 0 until 160) {
            for (j in 0 until 160) {
                val `val` = intValues[pixel++]
                byteBuffer.putFloat(((`val` shr 16 and 0xFF) - 127.5f) / 127.5f)
                byteBuffer.putFloat(((`val` shr 8 and 0xFF) - 127.5f) / 127.5f)
                byteBuffer.putFloat(((`val` and 0xFF) - 127.5f) / 127.5f)
            }
        }

        return byteBuffer

    }
}