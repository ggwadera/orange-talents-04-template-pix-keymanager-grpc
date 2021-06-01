package br.com.zup.ggwadera.pix

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface PixKeyRepository: JpaRepository<PixKey, Long> {
    fun existsByKey(key: String): Boolean
}