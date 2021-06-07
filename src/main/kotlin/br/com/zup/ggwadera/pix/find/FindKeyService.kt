package br.com.zup.ggwadera.pix.find

import br.com.zup.ggwadera.bcb.BcbClient
import br.com.zup.ggwadera.pix.PixKey
import br.com.zup.ggwadera.pix.PixKeyRepository
import br.com.zup.ggwadera.util.exception.NotFoundException
import br.com.zup.ggwadera.util.extensions.toUUID
import br.com.zup.ggwadera.util.validation.ValidUUID
import io.micronaut.validation.Validated
import javax.inject.Singleton
import javax.validation.constraints.NotBlank

@Singleton
@Validated
class FindKeyService(
    private val pixKeyRepository: PixKeyRepository,
    private val bcbClient: BcbClient
) {

    fun findById(@NotBlank @ValidUUID pixId: String, @NotBlank @ValidUUID clientId: String): PixKeyDetails =
        pixKeyRepository.findByUuidAndClientId(
            uuid = pixId.toUUID(),
            clientId = clientId.toUUID()
        )?.toResponse() ?: throw NotFoundException("Chave não encontrada ou não pertence ao usuário")

    fun findByKey(@NotBlank key: String): PixKeyDetails =
        pixKeyRepository.findByKey(key)?.toResponse() ?: requestKey(key)

    fun requestKey(@NotBlank key: String) = bcbClient.getKey(key)?.toModel()
        ?: throw IllegalStateException("Falha ao buscar chave no BACEN ou chave não foi registrada")

    private fun PixKey.toResponse() = PixKeyDetails.of(this)
}