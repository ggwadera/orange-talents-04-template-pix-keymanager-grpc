package br.com.zup.ggwadera.pix.newkey

import br.com.zup.ggwadera.itau.ItauClient
import br.com.zup.ggwadera.pix.KeyType
import br.com.zup.ggwadera.pix.PixKey
import br.com.zup.ggwadera.pix.PixKeyRepository
import br.com.zup.ggwadera.util.exception.AlreadyExistsException
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Singleton
import javax.validation.Valid

@Singleton
@Validated
class NewPixKeyService(
    private val pixKeyRepository: PixKeyRepository,
    private val itauClient: ItauClient
) {
    fun saveKey(@Valid newPixKey: NewPixKey): PixKey {
        validate(newPixKey)
        getClientInformation(newPixKey.clientId)

        return pixKeyRepository.save(newPixKey.toModel())
    }

    /**
     * Buscar dados do cliente no sistema ERP do banco
     */
    private fun getClientInformation(clientId: UUID) =
        itauClient.getClientInfo(clientId)
            ?: throw IllegalStateException("Cliente não encontrado no Itau")

    /**
     * Validar se a chave já existe no banco de dados
     */
    private fun validate(newPixKey: NewPixKey) {
        if (newPixKey.keyType != KeyType.RANDOM && pixKeyRepository.existsByKey(newPixKey.key))
            throw AlreadyExistsException("Chave já existente")
    }
}