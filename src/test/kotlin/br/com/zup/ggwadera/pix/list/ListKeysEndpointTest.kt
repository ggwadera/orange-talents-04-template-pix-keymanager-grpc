package br.com.zup.ggwadera.pix.list

import br.com.zup.ggwadera.ListKeysServiceGrpc
import br.com.zup.ggwadera.ListPixKeysRequest
import br.com.zup.ggwadera.pix.*
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.LocalDateTime
import java.util.*
import javax.inject.Singleton

@MicronautTest
internal class ListKeysEndpointTest(
    private val pixKeyRepository: PixKeyRepository,
    private val grpcClient: ListKeysServiceGrpc.ListKeysServiceBlockingStub
) {
    @Factory
    class Client {
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): ListKeysServiceGrpc.ListKeysServiceBlockingStub =
            ListKeysServiceGrpc.newBlockingStub(channel)
    }

    val clientId = UUID.randomUUID()
    val keys = pixKeyRepository.saveAll(
        listOf(
            newKey("12345678901", KeyType.CPF),
            newKey(UUID.randomUUID().toString(), KeyType.RANDOM)
        )
    )

    private fun newKey(key: String, keyType: KeyType): PixKey = PixKey(
        key = key,
        keyType = keyType,
        clientId = clientId,
        account = Account(
            participant = Account.ISPB_ITAU_UNIBANCO,
            branch = "1234",
            number = "123456",
            type = AccountType.CONTA_CORRENTE
        ),
        owner = Owner(
            name = "Bill Gates",
            taxIdNumber = "12345678901"
        ),
        createdAt = LocalDateTime.now()
    )

    @Test
    internal fun `deve listar chaves do usuario`() {
        val reply =
            grpcClient.listPixKeys(ListPixKeysRequest.newBuilder().setClientId(clientId.toString()).build())
        assertEquals(2, reply.keysCount)
        assertArrayEquals(keys.map { it.key }.toTypedArray(), reply.keysList.map { it.key }.toTypedArray())
    }

    @Test
    internal fun `deve retornar lista vazia caso nao encontrar nenhuma chave`() {
        val reply =
            grpcClient.listPixKeys(ListPixKeysRequest.newBuilder().setClientId(UUID.randomUUID().toString()).build())
        assertEquals(0, reply.keysCount)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "abcdef", "0e3c8209-7094-4329-90af-473017"])
    internal fun `deve retornar erro se clientId for invalido`(clientId: String) {
        assertThrows<StatusRuntimeException> {
            grpcClient.listPixKeys(ListPixKeysRequest.newBuilder().setClientId(clientId).build())
        }.run { assertEquals(Status.Code.INVALID_ARGUMENT, status.code) }
    }
}