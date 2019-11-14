package com.dinominator.kotlin_awesome_app.platform.works

import android.app.Application
import androidx.work.*
import com.dinominator.data.storage.AppSharedPreference
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Andy Ha on 5/4/2019.
 */

class DummyWorker @Inject constructor(
    val context: Application,
    private val workerParams: WorkerParameters,
    private val prefs: AppSharedPreference
) : RxWorker(context, workerParams) {
    private var isFirst: Boolean = false

    override fun createWork(): Single<Result> {
        return Single.just(Result.success())
    }

    companion object {
        fun enqueue(delaytime: Long) {
            val workManager = WorkManager.getInstance()
            val request = OneTimeWorkRequest.Builder(DummyWorker::class.java)
                .addTag("DummyTag")
                .setInitialDelay(delaytime, TimeUnit.MILLISECONDS)
            workManager.enqueueUniqueWork("DummyTag", ExistingWorkPolicy.REPLACE, request.build())
        }
    }
}