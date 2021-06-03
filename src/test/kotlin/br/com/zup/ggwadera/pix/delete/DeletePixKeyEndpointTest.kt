package br.com.zup.ggwadera.pix.delete

import br.com.zup.ggwadera.DeleteKeyServiceGrpc
import br.com.zup.ggwadera.DeletePixKeyRequest
import br.com.zup.ggwadera.bcb.BcbClient
import br.com.zup.ggwadera.bcb.DeletePixKeyResponse
import br.com.zup.ggwadera.pix.*
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import br.com.zup.ggwadera.bcb.DeletePixKeyRequest as BcbDeletePixKeyRequest

@MicronautTest(transactional = false)
internal class DeletePixKeyEndpointTest(
    private val pixKeyRepository: PixKeyRepository,
    private val grpcClient: DeleteKeyServiceGrpc.DeleteKeyServiceBlockingStub
) {

    @Inject
    private lateinit var bcbClient: BcbClient

    private lateinit var pixKey: PixKey

    @MockBean(BcbClient::class)
    fun mockBcbClient() = mock<BcbClient>() {
        on { deleteKey(any(), any()) } doAnswer { mockInvocation ->
            DeletePixKeyResponse(
                mockInvocation.arguments[0] as String,
                Account.ISPB_ITAU_UNIBANCO,
                LocalDateTime.now()
            )
        }
    }

    @BeforeEach
    internal fun setUp() {
        pixKey = pixKeyRepository.save(
            PixKey(
                key = "test@email.com",
                keyType = KeyType.EMAIL,
                account = Account(
                    participant = Account.ISPB_ITAU_UNIBANCO,
                    branch = "0123",
                    number = "012345",
                    type = AccountType.CONTA_CORRENTE
                ),
                owner = Owner(
                    id = UUID.randomUUID(),
                    name = "Bill Gates",
                    taxIdNumber = "12345678901"
                ),
                createdAt = LocalDateTime.now()
            )
        )
    }

    @AfterEach
    internal fun tearDown() {
        pixKeyRepository.deleteAll()
    }

    @Factory
    class Client {
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): DeleteKeyServiceGrpc.DeleteKeyServiceBlockingStub {
            return DeleteKeyServiceGrpc.newBlockingStub(channel)
        }
    }

    @Test
    internal fun `deve remover chave com sucesso`() {
        val request = DeletePixKeyRequest.newBuilder()
            .setClientId(pixKey.owner.id.toString())
            .setPixId(pixKey.uuid.toString())
            .build()

        val response = grpcClient.deletePixKey(request)

        with(response) {
            assertEquals(pixKey.owner.id.toString(), this.clientId)
            assertEquals(pixKey.uuid.toString(), this.pixId)
        }
        assertFalse(pixKeyRepository.existsById(pixKey.id))
        verify(bcbClient).deleteKey(pixKey.key, BcbDeletePixKeyRequest(pixKey.key, pixKey.account.participant))
    }

    @Test
    internal fun `deve retornar NOT_FOUND caso chave nao exista`() {
        val request = DeletePixKeyRequest.newBuilder()
            .setClientId(pixKey.owner.id.toString())
            .setPixId(UUID.randomUUID().toString())
            .build()

        val response = assertThrows<StatusRuntimeException> { grpcClient.deletePixKey(request) }
        with(response.status) {
            assertEquals(Status.NOT_FOUND.code, code)
            assertEquals("chave não existe ou não pertence ao usuário", description)
        }
        verify(bcbClient, never()).deleteKey(any(), any())
    }

    @Test
    internal fun `deve retornar NOT_FOUND caso chave nao pertenca ao cliente`() {
        val request = DeletePixKeyRequest.newBuilder()
            .setClientId(UUID.randomUUID().toString())
            .setPixId(pixKey.uuid.toString())
            .build()

        val response = assertThrows<StatusRuntimeException> { grpcClient.deletePixKey(request) }
        with(response.status) {
            assertEquals(Status.NOT_FOUND.code, code)
            assertEquals("chave não existe ou não pertence ao usuário", description)
        }
        verify(bcbClient, never()).deleteKey(any(), any())
    }


    @Test
    internal fun `deve retornar INVALID_ARGUMENT caso parametro esteja em branco`() {
        val request = DeletePixKeyRequest.newBuilder().build()

        val response = assertThrows<StatusRuntimeException> { grpcClient.deletePixKey(request) }
        with(response.status) {
            assertEquals(Status.INVALID_ARGUMENT.code, code)
        }
        verify(bcbClient, never()).deleteKey(any(), any())
    }

    @Test
    internal fun `deve retornar erro FAILED_PRECONDITION caso falhe ao deletar chave no BCB`() {
        val request = DeletePixKeyRequest.newBuilder()
            .setClientId(pixKey.owner.id.toString())
            .setPixId(pixKey.uuid.toString())
            .build()

        whenever(
            bcbClient.deleteKey(
                pixKey.key,
                BcbDeletePixKeyRequest(pixKey.key, pixKey.account.participant)
            )
        ).thenReturn(null)

        val response = assertThrows<StatusRuntimeException> { grpcClient.deletePixKey(request) }
        with(response.status) {
            assertEquals(Status.FAILED_PRECONDITION.code, code)
            assertEquals("Falha ao apagar registro no Banco Central", description)
        }
        // chave não deveria ter sido deletada
        assertTrue(pixKeyRepository.existsById(pixKey.id))
    }
}