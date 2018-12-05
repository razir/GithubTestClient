package com.anton.github.domain.usecase

import io.reactivex.Single

interface BaseUseCase<T> {
    fun run(): Single<T>
}