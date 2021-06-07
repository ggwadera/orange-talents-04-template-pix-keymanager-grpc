package br.com.zup.ggwadera.pix.find

import br.com.zup.ggwadera.FindKeyServiceGrpc
import br.com.zup.ggwadera.FindPixKeyRequest
import br.com.zup.ggwadera.PixKeyDetailsReply
import br.com.zup.ggwadera.bcb.BankAccount
import br.com.zup.ggwadera.bcb.BcbClient
import br.com.zup.ggwadera.bcb.PixKeyDetailsResponse
import br.com.zup.ggwadera.pix.*
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class FindKeyEndpointTest(
    private val pixKeyRepository: PixKeyRepository,
    private val grpcClient: FindKeyServiceGrpc.FindKeyServiceBlockingStub
) {

    @Inject
    private lateinit var bcbClient: BcbClient

    @MockBean(BcbClient::class)
    internal fun mockBcbClient() = mock<BcbClient>()

    @Factory
    class Client {
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): FindKeyServiceGrpc.FindKeyServiceBlockingStub {
            return FindKeyServiceGrpc.newBlockingStub(channel)
        }
    }

    private var pixKey: PixKey = pixKeyRepository.save(PixKey(
        key = UUID.randomUUID().toString(),
        keyType = KeyType.RANDOM,
        clientId = UUID.randomUUID(),
        account = Account(
            participant = Account.ISPB_ITAU_UNIBANCO,
            branch = "0123",
            number = "123456",
            type = AccountType.CONTA_CORRENTE
        ),
        owner = Owner(
            name = "Bill Gates",
            taxIdNumber = "12345678901"
        ),
        createdAt = LocalDateTime.parse("2021-06-07T10:15:30")
    ))

    private var expectedResult: PixKeyDetailsReply = PixKeyDetails.of(pixKey).toGrpcReply()

    @Test
    internal fun `deve buscar pela chave`() {
        val reply = grpcClient.findPixKey(FindPixKeyRequest.newBuilder().setKey(pixKey.key).build())
        assertEquals(expectedResult, reply)
        verify(bcbClient, never()).getKey(any())
    }

    @Test
    internal fun `deve buscar no banco central caso chave nao existir`() {
        val key = UUID.randomUUID().toString()
        val pixKeyDetailsResponse = PixKeyDetailsResponse(
            key = key,
            keyType = KeyType.RANDOM,
            createdAt = LocalDateTime.now(),
            bankAccount = BankAccount(pixKey.account),
            owner = br.com.zup.ggwadera.bcb.Owner(pixKey.owner)
        )
        whenever(bcbClient.getKey(key)).thenReturn(pixKeyDetailsResponse)
        val reply = grpcClient.findPixKey(FindPixKeyRequest.newBuilder().setKey(key).build())
        assertEquals(pixKeyDetailsResponse.toModel().toGrpcReply(), reply)
    }

    @Test
    internal fun `deve buscar pelo id da chave e do cliente`() {
        val reply = grpcClient.findPixKey(FindPixKeyRequest.newBuilder()
            .setIds(FindPixKeyRequest.ById.newBuilder()
                .setPixId(pixKey.uuid.toString())
                .setClientId(pixKey.clientId.toString())
                .build())
            .build())
        assertEquals(expectedResult, reply)
        verify(bcbClient, never()).getKey(any())
    }

    @Test
    internal fun `deve retornar erro se nao encontrar a chave`() {
        val reply = assertThrows<StatusRuntimeException> {
            grpcClient.findPixKey(
                FindPixKeyRequest.newBuilder()
                    .setIds(
                        FindPixKeyRequest.ById.newBuilder()
                            .setPixId(UUID.randomUUID().toString())
                            .setClientId(pixKey.clientId.toString())
                            .build()
                    )
                    .build()
            )
        }
        with(reply.status) {
            assertEquals(Status.Code.NOT_FOUND, code)
            assertEquals("Chave não encontrada ou não pertence ao usuário", description)
        }
    }

    @Test
    internal fun `deve retornar erro se nao encontrar a chave no banco central`() {
        val key = "0f5397b5-c58e-4153-ae18-4fcdc21711f7"
        whenever(bcbClient.getKey(key)).thenReturn(null)
        val reply = assertThrows<StatusRuntimeException> {
            grpcClient.findPixKey(
                FindPixKeyRequest.newBuilder()
                    .setKey(key)
                    .build()
            )
        }
        with(reply.status) {
            assertEquals(Status.Code.FAILED_PRECONDITION, code)
            assertEquals("Falha ao buscar chave no BACEN ou chave não foi registrada", description)
        }
    }

    @Test
    internal fun `deve retornar erro se chave nao pertencer ao usuario`() {
        val reply = assertThrows<StatusRuntimeException> {
            grpcClient.findPixKey(
                FindPixKeyRequest.newBuilder()
                    .setIds(
                        FindPixKeyRequest.ById.newBuilder()
                            .setPixId(pixKey.uuid.toString())
                            .setClientId(UUID.randomUUID().toString())
                            .build()
                    )
                    .build()
            )
        }
        with(reply.status) {
            assertEquals(Status.Code.NOT_FOUND, code)
            assertEquals("Chave não encontrada ou não pertence ao usuário", description)
        }
    }

    @Test
    internal fun `deve retornar erro se nao passar id ou chave`() {
        val reply = assertThrows<StatusRuntimeException> {
            grpcClient.findPixKey(FindPixKeyRequest.newBuilder().build())
        }
        with(reply.status) {
            assertEquals(Status.Code.INVALID_ARGUMENT, code)
            assertEquals("Deve especificar a chave ou id", description)
        }
    }
}