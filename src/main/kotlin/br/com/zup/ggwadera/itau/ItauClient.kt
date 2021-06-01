package br.com.zup.ggwadera.itau

import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client
import java.util.*
import javax.validation.constraints.NotBlank

@Client("\${services.erp}")
interface ItauClient {

    @Get("/api/v1/clientes/{clienteId}")
    fun getClientInfo(@PathVariable clienteId: UUID): ClientDataResponse?
}