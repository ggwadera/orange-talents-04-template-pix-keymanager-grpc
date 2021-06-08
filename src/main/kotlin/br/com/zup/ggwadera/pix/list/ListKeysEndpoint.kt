package br.com.zup.ggwadera.pix.list

import br.com.zup.ggwadera.ListKeysServiceGrpcKt
import br.com.zup.ggwadera.ListPixKeysReply
import br.com.zup.ggwadera.ListPixKeysRequest
import javax.inject.Singleton

@Singleton
@Suppress("unused")
class ListKeysEndpoint(
    private val listKeysService: ListKeysService
) : ListKeysServiceGrpcKt.ListKeysServiceCoroutineImplBase() {

    override suspend fun listPixKeys(request: ListPixKeysRequest): ListPixKeysReply =
        listKeysService.findAll(request.clientId)
            .map { it.toGrpcResponse() }
            .let { keys ->
                ListPixKeysReply.newBuilder()
                    .setClientId(request.clientId)
                    .addAllKeys(keys)
                    .build()
            }

}