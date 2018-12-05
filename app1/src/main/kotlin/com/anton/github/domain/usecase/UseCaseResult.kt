package com.anton.github.domain.usecase

sealed class ResultUseCase<T>

object CompletedUseCase : ResultUseCase<Unit>()

data class SuccessUseCase<T>(val result: T) : ResultUseCase<T>()

data class ErrorUseCase<T>(val msg: String? = null, val e: Throwable? = null) : ResultUseCase<T>()
