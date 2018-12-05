package com.anton.github2.extensions

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

@Suppress("NOTHING_TO_INLINE")
inline fun Disposable.addTo(compositeDisposable: CompositeDisposable): Boolean = compositeDisposable.add(this)