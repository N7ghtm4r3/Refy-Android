package com.tecknobit.refy.helpers

import android.app.Activity
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory

class ReviewHelper(
    val activity: Activity
) {

    /**
     * *reviewManager* -> the manager used to allow the user to review the application in app
     */
    private var reviewManager: ReviewManager = ReviewManagerFactory.create(activity)

    /**
     * Function to launch the review in-app API
     *
     * @param flowAction: the action to execute when the review in-app finished
     */
    fun reviewInApp(
        flowAction: () -> Unit
    ) {
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val flow = reviewManager.launchReviewFlow(activity, task.result)
                flow.addOnCompleteListener {
                    flowAction.invoke()
                }
                flow.addOnCanceledListener {
                    flowAction.invoke()
                }
            } else
                flowAction.invoke()
        }
        request.addOnFailureListener {
            flowAction.invoke()
        }
    }

}