package br.com.zup.ggwadera.pix.register

import br.com.zup.ggwadera.bcb.BcbClient
import br.com.zup.ggwadera.bcb.CreatePixKeyRequest
import br.com.zup.ggwadera.bcb.CreatePixKeyResponse
import br.com.zup.ggwadera.itau.AccountResponse
import br.com.zup.ggwadera.itau.ItauClient
import br.com.zup.ggwadera.pix.AccountType
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
class RegisterKeyService(
    private val pixKeyRepository: PixKeyRepository,
    private val itauClient: ItauClient,
    private val bcbClient: BcbClient
) {

    fun saveKey(@Valid newPixKey: NewPixKey): PixKey {
        validate(newPixKey)
        val account = getAccount(newPixKey.clientId, newPixKey.accountType!!)
        val bcbResponse = registerKey(newPixKey, account)

        return pixKeyRepository.save(bcbResponse.toModel(newPixKey.clientId))
    }

    /**
     * Registra chave no sistema do Banco Central
     */
    private fun registerKey(
        newPixKey: NewPixKey,
        account: AccountResponse
    ): CreatePixKeyResponse {
        return bcbClient.registerKey(CreatePixKeyRequest(newPixKey, account))
            ?: throw IllegalStateException("Falha ao registrar chave no Banco Central")
    }

    /**
     * Buscar dados do cliente no sistema ERP do banco
     */
    private fun getAccount(clientId: UUID, accountType: AccountType) =
        itauClient.getAccount(clientId, accountType)
            ?: throw IllegalStateException("Cliente não encontrado no Itau")

    /**
     * Validar se a chave já existe no banco de dados
     */
    private fun validate(newPixKey: NewPixKey) {
        if (newPixKey.keyType != KeyType.RANDOM && pixKeyRepository.existsByKey(newPixKey.key))
            throw AlreadyExistsException("Chave já existente")
    }
}