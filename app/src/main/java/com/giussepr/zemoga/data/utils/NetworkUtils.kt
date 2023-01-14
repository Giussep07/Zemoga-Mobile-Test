package com.giussepr.zemoga.data.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import javax.inject.Inject

class NetworkUtils @Inject constructor(private val context: Context) {

  fun isInternetAvailable(): Boolean {
    val connectivityManager =
      context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val capabilities: NetworkCapabilities? =
      connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

    return if (capabilities != null) {
      capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) && (
          capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
              capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
              capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    } else {
      false
    }
  }

}
