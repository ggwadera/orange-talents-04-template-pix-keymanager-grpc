package br.com.zup.ggwadera.itau

import io.micronaut.core.annotation.Introspected
import java.util.*

@Introspected
data class ClientDataResponse(
    val id: UUID,
    val nome: String,
    val cpf: String,
    val instituicao: Instituicao
)

@Introspected
data class Instituicao(
    val nome: String,
    val ispb: String
)
