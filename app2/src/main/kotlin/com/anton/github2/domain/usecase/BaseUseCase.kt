package com.anton.github2.domain.usecase

import io.reactivex.Single

interface BaseUseCase<T> {
    fun run(): Single<T>
}