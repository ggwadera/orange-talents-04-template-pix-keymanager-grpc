package br.com.zup.ggwadera.pix.delete

import br.com.zup.ggwadera.DeleteKeyServiceGrpcKt
import br.com.zup.ggwadera.DeletePixKeyReply
import br.com.zup.ggwadera.DeletePixKeyRequest
import javax.inject.Singleton

@Singleton
@Suppress("unused")
class DeletePixKeyEndpoint(private val deletePixKeyService: DeletePixKeyService) :
    DeleteKeyServiceGrpcKt.DeleteKeyServiceCoroutineImplBase() {
    override suspend fun deletePixKey(request: DeletePixKeyRequest): DeletePixKeyReply {
        return with(request) {
            deletePixKeyService.delete(clientId, pixId)
            DeletePixKeyReply.newBuilder()
                .setClientId(clientId)
                .setPixId(pixId)
                .build()
        }
    }
}