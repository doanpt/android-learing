package com.ddona.jetpack.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.ddona.jetpack.R
import com.ddona.jetpack.util.Const
import com.ddona.jetpack.worker.BlurWorker
import com.ddona.jetpack.worker.CleanupWorker
import com.ddona.jetpack.worker.SaveImageToFileWorker

class BlurViewModel(val application: Application) : ViewModel() {
    private var imageUri: Uri? = null
    var outputUri: Uri? = null
    private val workManager = WorkManager.getInstance(application)
    val outputWorkInfos: LiveData<List<WorkInfo>> =
        workManager.getWorkInfosByTagLiveData(Const.TAG_OUTPUT)

    init {
        // This transformation makes sure that whenever the current work Id changes the WorkInfo
        // the UI is listening to changes
        imageUri = getImageUri(application.applicationContext)
    }

    fun cancelWork() {
        workManager.cancelUniqueWork(Const.IMAGE_MANIPULATION_WORK_NAME)
    }

    /**
     * Creates the input data bundle which includes the Uri to operate on
     * @return Data which contains the Image Uri as a String
     */
    private fun createInputDataForUri(): Data {
        val builder = Data.Builder()
        imageUri?.let {
            builder.putString(Const.KEY_IMAGE_URI, imageUri.toString())
        }
        return builder.build()
    }

    /**
     * Create the WorkRequest to apply the blur and save the resulting image
     * @param blurLevel The amount to blur the image
     */
    fun applyBlur(blurLevel: Int) {
        // Add WorkRequest to Cleanup temporary images
        var continuation = workManager
            .beginUniqueWork(
                Const.IMAGE_MANIPULATION_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(CleanupWorker::class.java)
            )

        // Add WorkRequests to blur the image the number of times requested
        for (i in 0 until blurLevel) {
            val blurBuilder = OneTimeWorkRequestBuilder<BlurWorker>()

            // Input the Uri if this is the first blur operation
            // After the first blur operation the input will be the output of previous
            // blur operations.
            if (i == 0) {
                blurBuilder.setInputData(createInputDataForUri())
            }

            continuation = continuation.then(blurBuilder.build())
        }

        // Create charging constraint
        val constraints = Constraints.Builder()
            .setRequiresCharging(false)
            .setRequiresDeviceIdle(false)
            .build()

        // Add WorkRequest to save the image to the filesystem
        val save = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
            .setConstraints(constraints)
            .addTag(Const.TAG_OUTPUT)
            .build()
        continuation = continuation.then(save)

        // Actually start the work
        continuation.enqueue()
    }

    private fun getImageUri(context: Context): Uri {
        val resources = context.resources
        //return format: android.resource://com.ddona.jetpack/drawable/android_cupcake
        return Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)//android.resource
            .authority(resources.getResourcePackageName(R.drawable.android_cupcake))//com.ddona.jetpack
            .appendPath(resources.getResourceTypeName(R.drawable.android_cupcake))//drawable
            .appendPath(resources.getResourceEntryName(R.drawable.android_cupcake))//android_cupcake
            .build()
    }

    private fun uriOrNull(uriString: String?): Uri? {
        return if (!uriString.isNullOrEmpty()) {
            Uri.parse(uriString)
        } else {
            null
        }
    }

    internal fun setOutputUri(outputImageUri: String?) {
        outputUri = uriOrNull(outputImageUri)
    }
}