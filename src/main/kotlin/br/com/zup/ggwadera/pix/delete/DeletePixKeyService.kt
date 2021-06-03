package br.com.zup.ggwadera.pix.delete

import br.com.zup.ggwadera.bcb.BcbClient
import br.com.zup.ggwadera.bcb.DeletePixKeyRequest
import br.com.zup.ggwadera.pix.PixKey
import br.com.zup.ggwadera.pix.PixKeyRepository
import br.com.zup.ggwadera.util.exception.NotFoundException
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Singleton
import javax.validation.constraints.NotBlank

@Singleton
@Validated
class DeletePixKeyService(
    private val pixKeyRepository: PixKeyRepository,
    private val bcbClient: BcbClient
) {
    fun delete(@NotBlank clientId: String, @NotBlank pixId: String) {
        val pixKey = findKey(pixId, clientId)
        sendKeyDeleteRequest(pixKey)
        pixKeyRepository.delete(pixKey)
    }

    private fun findKey(pixId: String, clientId: String) = pixKeyRepository.findByUuidAndOwnerId(
        uuid = UUID.fromString(pixId),
        ownerId = UUID.fromString(clientId)
    ) ?: throw NotFoundException("chave não existe ou não pertence ao usuário")

    private fun sendKeyDeleteRequest(pixKey: PixKey) = with(pixKey) {
        bcbClient.deleteKey(key, DeletePixKeyRequest(key, account.participant))
            ?: throw IllegalStateException("Falha ao apagar registro no Banco Central")
    }
}