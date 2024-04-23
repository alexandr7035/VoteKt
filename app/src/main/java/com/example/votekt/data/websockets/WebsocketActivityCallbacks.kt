package com.example.votekt.data.websockets

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log
import com.example.votekt.domain.repository.WebsocketManager

class WebsocketActivityCallbacks(
    private val websocketManager: WebsocketManager,
): ActivityLifecycleCallbacks {

    override fun onActivityStarted(activity: Activity) {
        Log.d(TAG, "app is going in foreground")
        websocketManager.connect()
    }

    override fun onActivityStopped(activity: Activity) {
        Log.d(TAG, "app is going in background")
        websocketManager.disconnect()
    }

    override fun onActivityCreated(activity: Activity, p1: Bundle?) = Unit

    override fun onActivityResumed(activity: Activity) = Unit

    override fun onActivityPaused(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) = Unit

    override fun onActivityDestroyed(activity: Activity) = Unit

    companion object {
        private const val TAG = "LIFECYCLE_TAG"
    }
}