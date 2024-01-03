package com.hk.portfolio.today_weather

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.hk.portfolio.today_weather.core.checkNotificationPermission
import com.hk.portfolio.today_weather.core.util.NotificationBuilder
import com.hk.portfolio.today_weather.presentation.screen.MainNavigationComponent
import com.hk.portfolio.today_weather.presentation.screen.MainViewModel
import com.hk.portfolio.today_weather.ui.theme.TodayWeatherPortFolioTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // Creates instance of the manager.
    lateinit var appUpdateManager: AppUpdateManager
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    val updateResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult ->
            run {
                // handle callback
                if (result.resultCode != RESULT_OK) {
//                log("Update flow failed! Result code: " + resultCode);
                    // If the update is cancelled or fails,
                    // you can request to start the update again.
                } else {
                    // When status updates are no longer needed, unregister the listener.
                    appUpdateManager.unregisterListener(listener)
                    // Displays the snackbar notification and call to action.
                    appUpdateManager.completeUpdate()
                }
            }
        }

    // Create a listener to track request state updates.
    val listener = InstallStateUpdatedListener { state ->
        // (Optional) Provide a download progress bar.
        if (state.installStatus() == InstallStatus.DOWNLOADING) {
            val bytesDownloaded = state.bytesDownloaded()
            val totalBytesToDownload = state.totalBytesToDownload()
            // Show update progress bar.
        } else if (state.installStatus() == InstallStatus.DOWNLOADED) {
            // When status updates are no longer needed, unregister the listener.
            // Displays the snackbar notification and call to action.
            appUpdateManager.completeUpdate()
        }
        // Log state or install the update.
    }

// Start an update.

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
        appUpdateManager = AppUpdateManagerFactory.create(this)
        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                // Request the update.
                appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // an activity result launcher registered via registerForActivityResult
                    updateResultLauncher,
                    // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                    // flexible updates.
                    AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE)
                        .setAllowAssetPackDeletion(true)
                        .build()
                )
            }
        }

        // Before starting an update, register a listener for updates.
        appUpdateManager.registerListener(listener)
        NotificationBuilder.createEventPushChannel(this)

        setContent {
            val viewModel = hiltViewModel<MainViewModel>()
            LaunchedEffect(Unit) {
                viewModel.checkAndUpdateWeather()
            }

            TodayWeatherPortFolioTheme {
                // A surface container using the 'background' color from the theme
                MainNavigationComponent()
            }
        }
    }
}