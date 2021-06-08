package br.com.zup.ggwadera.pix.list

import br.com.zup.ggwadera.pix.PixKeyRepository
import br.com.zup.ggwadera.util.extensions.toUUID
import br.com.zup.ggwadera.util.validation.ValidUUID
import io.micronaut.validation.Validated
import javax.inject.Singleton
import javax.validation.constraints.NotBlank

@Singleton
@Validated
@Suppress("unused")
class ListKeysService(
    private val pixKeyRepository: PixKeyRepository
) {

    fun findAll(@NotBlank @ValidUUID clientId: String) = pixKeyRepository.findByClientId(clientId.toUUID())

}