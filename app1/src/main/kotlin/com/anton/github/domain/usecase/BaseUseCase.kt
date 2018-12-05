package com.anton.github.domain.usecase

interface BaseUseCase<T> {
    suspend fun run(): ResultUseCase<T>
}