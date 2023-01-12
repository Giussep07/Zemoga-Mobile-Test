package com.giussepr.zemoga.domain.model

open class DomainException(override val message: String = "") : Throwable(message)
