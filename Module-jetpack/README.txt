WorkManager:
        // (Java only)
        implementation "androidx.work:work-runtime:$work_version"

        // Kotlin + coroutines
        implementation "androidx.work:work-runtime-ktx:$work_version"

        // optional - RxJava2 support
        implementation "androidx.work:work-rxjava2:$work_version"

        // optional - GCMNetworkManager support
        implementation "androidx.work:work-gcm:$work_version"

        // optional - Test helpers
        androidTestImplementation "androidx.work:work-testing:$work_version"