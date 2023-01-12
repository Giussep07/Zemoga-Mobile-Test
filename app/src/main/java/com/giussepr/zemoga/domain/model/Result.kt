package com.giussepr.zemoga.domain.model

sealed class Result<T> {
  data class Success<T>(val data: T) : Result<T>()
  data class Error<T>(val domainException: DomainException) : Result<T>()
}

inline fun <R, T> Result<T>.fold(
  onSuccess: (value: T) -> R,
  onFailure: (domainException: DomainException) -> R
): R = when (this) {
  is Result.Success -> onSuccess(data)
  is Result.Error -> onFailure(domainException)
}
