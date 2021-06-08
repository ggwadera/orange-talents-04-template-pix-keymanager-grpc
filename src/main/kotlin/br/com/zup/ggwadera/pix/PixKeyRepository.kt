package br.com.zup.ggwadera.pix

import br.com.zup.ggwadera.pix.list.ListKeysItem
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface PixKeyRepository : JpaRepository<PixKey, Long> {
    fun existsByKey(key: String): Boolean
    fun findByUuidAndClientId(uuid: UUID, clientId: UUID): PixKey?
    fun findByKey(key: String): PixKey?
    fun findByClientId(clientId: UUID): List<ListKeysItem>
}