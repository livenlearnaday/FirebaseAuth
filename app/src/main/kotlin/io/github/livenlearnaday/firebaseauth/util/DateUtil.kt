package io.github.livenlearnaday.firebaseauth.util

import timber.log.Timber
import java.time.Instant

//time difference is +17353 , +17742 or about 17 seconds, added about 17.5s, firebase timestamp is faster than time from IDE
fun Long.isWithinPast(minutes: Int): Boolean {
    Timber.d("log time from firebase server $this")
    val nowInUTCMilliSeconds = Instant.now().toEpochMilli() + 17500
    Timber.d("log nowInUTCSeconds $nowInUTCMilliSeconds")
    val timeXMinBefore = nowInUTCMilliSeconds - (60 * minutes * 1000)
    Timber.d("log timeXMinBefore $timeXMinBefore")
    val range = timeXMinBefore..nowInUTCMilliSeconds
    return range.contains(this)
}