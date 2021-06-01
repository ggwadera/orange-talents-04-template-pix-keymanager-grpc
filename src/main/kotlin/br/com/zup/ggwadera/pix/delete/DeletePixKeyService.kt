package br.com.zup.ggwadera.pix.delete

import br.com.zup.ggwadera.pix.PixKeyRepository
import br.com.zup.ggwadera.util.exception.NotFoundException
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Singleton
import javax.validation.constraints.NotBlank

@Singleton
@Validated
class DeletePixKeyService(private val pixKeyRepository: PixKeyRepository) {
    fun delete(@NotBlank clientId: String, @NotBlank pixId: String) {
        val pixKey = pixKeyRepository.findByUuidAndClientId(
            uuid = UUID.fromString(pixId),
            clientId = UUID.fromString(clientId)
        ) ?: throw NotFoundException("chave não existe ou não pertence ao usuário")
        pixKeyRepository.delete(pixKey)
    }
}