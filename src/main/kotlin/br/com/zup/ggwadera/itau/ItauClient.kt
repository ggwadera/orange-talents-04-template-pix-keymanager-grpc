package br.com.zup.ggwadera.itau

import br.com.zup.ggwadera.pix.Account
import br.com.zup.ggwadera.pix.AccountType
import br.com.zup.ggwadera.pix.Owner
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client
import java.util.*

@Client("\${services.erp}")
interface ItauClient {

    @Get("/api/v1/clientes/{clienteId}")
    fun getClientInfo(@PathVariable clienteId: UUID): ClientDataResponse?

    @Get("/api/v1/clientes/{clientId}/contas")
    fun getAccount(@PathVariable clientId: UUID, @QueryValue("tipo") accountType: AccountType): AccountResponse?
}

data class AccountResponse(
    val tipo: AccountType,
    val instituicao: BankResponse,
    val agencia: String,
    val numero: String,
    val titular: OwnerResponse
) {
    data class BankResponse(
        val nome: String,
        val ispb: String
    )

    data class OwnerResponse(
        val id: UUID,
        val nome: String,
        val cpf: String
    ) {
        fun toModel(): Owner = Owner(
            name = nome,
            taxIdNumber = cpf
        )
    }

    fun toModel(): Account = Account(
        participant = instituicao.ispb,
        branch = agencia,
        number = numero,
        type = tipo
    )
}