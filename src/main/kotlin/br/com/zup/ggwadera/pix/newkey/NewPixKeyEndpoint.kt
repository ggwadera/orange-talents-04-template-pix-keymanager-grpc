package br.com.zup.ggwadera.pix.newkey

import br.com.zup.ggwadera.AccountType.ACCOUNT_TYPE_UNSPECIFIED
import br.com.zup.ggwadera.KeyType.KEY_TYPE_UNSPECIFIED
import br.com.zup.ggwadera.NewPixKeyReply
import br.com.zup.ggwadera.NewPixKeyRequest
import br.com.zup.ggwadera.PixServiceGrpcKt
import br.com.zup.ggwadera.pix.AccountType
import br.com.zup.ggwadera.pix.KeyType
import java.util.*
import javax.inject.Singleton

@Singleton
@Suppress("unused")
class NewPixKeyEndpoint(private val newPixKeyService: NewPixKeyService) :
    PixServiceGrpcKt.PixServiceCoroutineImplBase() {

    override suspend fun addPixKey(request: NewPixKeyRequest): NewPixKeyReply {
        val pixKey = newPixKeyService.saveKey(request.toDTO())
        return NewPixKeyReply.newBuilder()
            .setPixId(pixKey.uuid.toString())
            .build()
    }

    private fun NewPixKeyRequest.toDTO() = NewPixKey(
        clientId = UUID.fromString(clientId),
        key = key,
        keyType = when (keyType) {
            KEY_TYPE_UNSPECIFIED -> null
            else -> KeyType.valueOf(keyType.name)
        },
        accountType = when (accountType) {
            ACCOUNT_TYPE_UNSPECIFIED -> null
            else -> AccountType.valueOf(accountType.name)
        }
    )
}
