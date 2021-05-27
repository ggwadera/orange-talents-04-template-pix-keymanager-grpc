package br.com.zup.ggwadera

import io.micronaut.runtime.startApplication

object ApplicationKt {
    @JvmStatic
    fun main(args: Array<String>) {
        startApplication<ApplicationKt>(*args)
    }
}

