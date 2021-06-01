package br.com.zup.ggwadera.pix.newkey

import br.com.zup.ggwadera.NewPixKeyRequest
import br.com.zup.ggwadera.PixServiceGrpc
import br.com.zup.ggwadera.itau.ClientDataResponse
import br.com.zup.ggwadera.itau.Instituicao
import br.com.zup.ggwadera.itau.ItauClient
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
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito.*
import java.util.*
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Singleton
import br.com.zup.ggwadera.AccountType as GrpcAccountType
import br.com.zup.ggwadera.KeyType as GrpcKeyType

@MicronautTest(transactional = false)
internal class NewPixKeyEndpointTest {

    @Inject
    lateinit var grpcClient: PixServiceGrpc.PixServiceBlockingStub

    @Inject
    lateinit var pixKeyRepository: PixKeyRepository

    @Inject
    lateinit var itauClient: ItauClient

    @AfterEach
    internal fun tearDown() {
        pixKeyRepository.deleteAll()
    }

    @MockBean(ItauClient::class)
    internal fun mockItauClient(): ItauClient {
        return mock(ItauClient::class.java)
    }

    @ParameterizedTest
    @MethodSource("validKeysProvider")
    internal fun `deve cadastrar chave valida`(key: String, keyType: GrpcKeyType) {
        val clientId = UUID.randomUUID()
        val request = NewPixKeyRequest.newBuilder()
            .setClientId(clientId.toString())
            .setKey(key)
            .setKeyType(keyType)
            .setAccountType(GrpcAccountType.CONTA_CORRENTE)
            .build()

        `when`(itauClient.getClientInfo(clientId)).thenReturn(
            ClientDataResponse(
                id = clientId,
                nome = "Bruce Wayne",
                cpf = "80349185042",
                instituicao = Instituicao(nome = "Itaú", ispb = "Er3Z746")
            )
        )

        val response = grpcClient.addPixKey(request)
        assertFalse(response.pixId.isNullOrBlank())
        val created = pixKeyRepository.findByUuidAndClientId(UUID.fromString(response.pixId), clientId)
        assertNotNull(created)
        with(created!!) {
            assertEquals(clientId, this.clientId)
            assertEquals(KeyType.valueOf(keyType.name), this.keyType)
            assertEquals(AccountType.CONTA_CORRENTE, this.accountType)
            if (keyType != GrpcKeyType.RANDOM) {
                assertEquals(key, this.key)
            }
        }
    }

    @ParameterizedTest
    @MethodSource("invalidKeysProvider")
    internal fun `deve retornar erro para chave invalida`(key: String, keyType: GrpcKeyType, accountType: GrpcAccountType) {
        val clientId = UUID.randomUUID()
        val request = NewPixKeyRequest.newBuilder()
            .setClientId(clientId.toString())
            .setKey(key)
            .setKeyType(keyType)
            .setAccountType(accountType)
            .build()

        val response = assertThrows<StatusRuntimeException> { grpcClient.addPixKey(request) }
        verify(itauClient, never()).getClientInfo(clientId)
        assertEquals(0, pixKeyRepository.count())
        assertEquals(Status.INVALID_ARGUMENT.code, response.status.code)
    }

    @Test
    internal fun `deve retornar erro caso chave ja exista`() {
        val clientId = UUID.randomUUID()
        val key = "test@email.com"
        pixKeyRepository.save(
            PixKey(
                clientId = clientId,
                key = key,
                keyType = KeyType.EMAIL,
                accountType = AccountType.CONTA_CORRENTE
            )
        )
        val response = assertThrows<StatusRuntimeException> {
            grpcClient.addPixKey(
                NewPixKeyRequest.newBuilder()
                    .setClientId(clientId.toString())
                    .setKey(key)
                    .setKeyType(GrpcKeyType.EMAIL)
                    .setAccountType(GrpcAccountType.CONTA_CORRENTE)
                    .build()
            )
        }
        verify(itauClient, never()).getClientInfo(clientId)
        assertEquals(1, pixKeyRepository.count())
        assertEquals(Status.ALREADY_EXISTS.code, response.status.code)
    }

    @Test
    internal fun `deve retornar erro caso cliente nao seja encontrado no sistema itau`() {
        val clientId = UUID.randomUUID()
        `when`(itauClient.getClientInfo(clientId)).thenReturn(null)

        val response = assertThrows<StatusRuntimeException> {
            grpcClient.addPixKey(
                NewPixKeyRequest.newBuilder()
                    .setClientId(clientId.toString())
                    .setKey("test@email.com")
                    .setKeyType(GrpcKeyType.EMAIL)
                    .setAccountType(GrpcAccountType.CONTA_CORRENTE)
                    .build()
            )
        }

        verify(itauClient).getClientInfo(clientId)
        assertEquals(0, pixKeyRepository.count())
        assertEquals(Status.FAILED_PRECONDITION.code, response.status.code)
    }

    companion object {
        @JvmStatic
        fun validKeysProvider(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("80349185042", GrpcKeyType.CPF),
                Arguments.of("+5511987654321", GrpcKeyType.PHONE),
                Arguments.of("email@test.com", GrpcKeyType.EMAIL),
                Arguments.of("", GrpcKeyType.RANDOM)
            )
        }

        @JvmStatic
        fun invalidKeysProvider(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("12345678901", GrpcKeyType.CPF, GrpcAccountType.CONTA_CORRENTE),
                Arguments.of("", GrpcKeyType.CPF, GrpcAccountType.CONTA_CORRENTE),
                Arguments.of("11987654321", GrpcKeyType.PHONE, GrpcAccountType.CONTA_CORRENTE),
                Arguments.of("", GrpcKeyType.PHONE, GrpcAccountType.CONTA_CORRENTE),
                Arguments.of("emailtest.com", GrpcKeyType.EMAIL, GrpcAccountType.CONTA_CORRENTE),
                Arguments.of("", GrpcKeyType.EMAIL, GrpcAccountType.CONTA_CORRENTE),
                Arguments.of("92d0e0a9-849a-4503-9a77-b730e44ce6da", GrpcKeyType.RANDOM, GrpcAccountType.CONTA_CORRENTE),
                Arguments.of("", GrpcKeyType.RANDOM, GrpcAccountType.ACCOUNT_TYPE_UNSPECIFIED),
                Arguments.of("", GrpcKeyType.KEY_TYPE_UNSPECIFIED, GrpcAccountType.CONTA_CORRENTE)
            )
        }
    }

    @Factory
    class Client {
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): PixServiceGrpc.PixServiceBlockingStub {
            return PixServiceGrpc.newBlockingStub(channel)
        }
    }
}