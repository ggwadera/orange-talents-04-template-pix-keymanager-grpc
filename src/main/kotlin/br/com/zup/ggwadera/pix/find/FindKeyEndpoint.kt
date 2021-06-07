package br.com.zup.ggwadera.pix.find

import br.com.zup.ggwadera.FindKeyServiceGrpcKt
import br.com.zup.ggwadera.FindPixKeyRequest
import br.com.zup.ggwadera.FindPixKeyRequest.OptionCase.IDS
import br.com.zup.ggwadera.FindPixKeyRequest.OptionCase.KEY
import br.com.zup.ggwadera.PixKeyDetailsReply
import javax.inject.Singleton

@Singleton
@Suppress("unused")
class FindKeyEndpoint(
    private val findKeyService: FindKeyService
) : FindKeyServiceGrpcKt.FindKeyServiceCoroutineImplBase() {
    override suspend fun findPixKey(request: FindPixKeyRequest): PixKeyDetailsReply = with(request) {
        when (optionCase) {
            IDS -> findKeyService.findById(ids.pixId, ids.clientId)
            KEY -> findKeyService.findByKey(key)
            else -> throw IllegalArgumentException("Deve especificar a chave ou id")
        }
    }.toGrpcReply()
}