package com.giussepr.zemoga.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UiPost(
  val userId: Int,
  val id: Int,
  val title: String,
  val body: String,
  val isFavorite: Boolean
): Parcelable
