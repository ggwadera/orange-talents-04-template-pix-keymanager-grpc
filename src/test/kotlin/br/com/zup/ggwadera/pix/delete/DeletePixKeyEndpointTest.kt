package br.com.zup.ggwadera.pix.delete

import br.com.zup.ggwadera.DeleteKeyServiceGrpc
import br.com.zup.ggwadera.DeletePixKeyRequest
import br.com.zup.ggwadera.pix.AccountType
import br.com.zup.ggwadera.pix.KeyType
import br.com.zup.ggwadera.pix.PixKey
import br.com.zup.ggwadera.pix.PixKeyRepository
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class DeletePixKeyEndpointTest(
    private val pixKeyRepository: PixKeyRepository,
    private val grpcClient: DeleteKeyServiceGrpc.DeleteKeyServiceBlockingStub
) {

    companion object {
        private val clientId: UUID = UUID.randomUUID()
        private const val key = "test@email.com"
    }

    private var pixKey: PixKey = PixKey(clientId, key, KeyType.EMAIL, AccountType.CONTA_CORRENTE)

    @BeforeEach
    internal fun setUp() {
        val entity = PixKey(
            clientId = clientId,
            key = key,
            keyType = KeyType.EMAIL,
            accountType = AccountType.CONTA_CORRENTE
        )
        pixKey = pixKeyRepository.save(entity)
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
            .setClientId(clientId.toString())
            .setPixId(pixKey.uuid.toString())
            .build()

        val response = grpcClient.deletePixKey(request)

        with(response) {
            assertEquals(clientId.toString(), this.clientId)
            assertEquals(pixKey.uuid.toString(), this.pixId)
        }
        assertFalse(pixKeyRepository.existsById(pixKey.id))
    }

    @Test
    internal fun `deve retornar NOT_FOUND caso chave nao exista`() {
        val request = DeletePixKeyRequest.newBuilder()
            .setClientId(clientId.toString())
            .setPixId(UUID.randomUUID().toString())
            .build()

        val response = assertThrows<StatusRuntimeException> { grpcClient.deletePixKey(request) }
        with(response.status) {
            assertEquals(Status.NOT_FOUND.code, code)
            assertEquals("chave não existe ou não pertence ao usuário", description)
        }
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
    }



    @Test
    internal fun `deve retornar INVALID_ARGUMENT caso parametro esteja em branco`() {
        val request = DeletePixKeyRequest.newBuilder().build()

        val response = assertThrows<StatusRuntimeException> { grpcClient.deletePixKey(request) }
        with(response.status) {
            assertEquals(Status.INVALID_ARGUMENT.code, code)
        }
    }
}