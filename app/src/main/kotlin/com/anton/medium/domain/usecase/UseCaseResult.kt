package com.anton.medium.domain.usecase

sealed class ResultUseCase

object CompletedUseCase : ResultUseCase()

data class SuccessUseCase<T>(val result: T) : ResultUseCase()

data class ErrorUseCase(val msg: String? = null, val e: Throwable? = null) : ResultUseCase()