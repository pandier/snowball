package io.github.pandier.snowball.impl.adapter

import java.util.function.Supplier

interface SnowballAdapterHolder<T : SnowballAdapter> : Supplier<T> {
    class Lazy<T : SnowballAdapter>(supplier: () -> T) : SnowballAdapterHolder<T> {
        private val value: T by lazy(supplier)
        override fun get(): T = value
    }
}
