package br.com.zup.ggwadera.pix

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface PixKeyRepository: JpaRepository<PixKey, Long> {
    fun existsByKey(key: String): Boolean
    fun findByUuid(uuid: UUID): PixKey?
}