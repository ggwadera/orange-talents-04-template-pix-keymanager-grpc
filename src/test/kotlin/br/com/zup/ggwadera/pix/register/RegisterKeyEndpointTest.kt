package br.com.zup.ggwadera.pix.register

import br.com.zup.ggwadera.NewPixKeyRequest
import br.com.zup.ggwadera.RegisterKeyServiceGrpc
import br.com.zup.ggwadera.bcb.BankAccount
import br.com.zup.ggwadera.bcb.BcbClient
import br.com.zup.ggwadera.bcb.CreatePixKeyRequest
import br.com.zup.ggwadera.bcb.CreatePixKeyResponse
import br.com.zup.ggwadera.itau.AccountResponse
import br.com.zup.ggwadera.itau.ItauClient
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
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.kotlin.*
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Singleton
import br.com.zup.ggwadera.AccountType as GrpcAccountType
import br.com.zup.ggwadera.KeyType as GrpcKeyType

@MicronautTest(transactional = false)
@Suppress("unused")
internal class RegisterKeyEndpointTest(
    private val pixKeyRepository: PixKeyRepository,
    private val grpcClient: RegisterKeyServiceGrpc.RegisterKeyServiceBlockingStub,
) {

    @Inject
    private lateinit var itauClient: ItauClient

    @Inject
    private lateinit var bcbClient: BcbClient

    @AfterEach
    internal fun tearDown() {
        pixKeyRepository.deleteAll()
    }

    @MockBean(ItauClient::class)
    internal fun mockItauClient() = mock<ItauClient>()

    @MockBean(BcbClient::class)
    internal fun mockBcbClient() = mock<BcbClient>()

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

        whenever(itauClient.getAccount(clientId, AccountType.CONTA_CORRENTE)).thenReturn(
            AccountResponse(
                tipo = AccountType.CONTA_CORRENTE,
                instituicao = AccountResponse.BankResponse(
                    nome = "Itaú Unibanco",
                    ispb = Account.ISPB_ITAU_UNIBANCO
                ),
                agencia = "0123",
                numero = "012345",
                titular = AccountResponse.OwnerResponse(
                    id = clientId,
                    nome = "Bill Gates",
                    cpf = "12345678901"
                )
            )
        )

        whenever(
            bcbClient.registerKey(
                CreatePixKeyRequest(
                    key = key,
                    keyType = KeyType.valueOf(keyType.name),
                    bankAccount = BankAccount(
                        participant = Account.ISPB_ITAU_UNIBANCO,
                        branch = "0123",
                        accountNumber = "012345",
                        accountType = BankAccount.AccountType.CACC
                    ),
                    owner = br.com.zup.ggwadera.bcb.Owner(
                        type = br.com.zup.ggwadera.bcb.Owner.OwnerType.NATURAL_PERSON,
                        name = "Bill Gates",
                        taxIdNumber = "12345678901"
                    )
                )
            )
        ).thenReturn(
            CreatePixKeyResponse(
                key = if (keyType != GrpcKeyType.RANDOM) key else UUID.randomUUID().toString(),
                keyType = KeyType.valueOf(keyType.name),
                createdAt = LocalDateTime.now(),
                bankAccount = BankAccount(
                    participant = Account.ISPB_ITAU_UNIBANCO,
                    branch = "0123",
                    accountNumber = "012345",
                    accountType = BankAccount.AccountType.CACC
                ),
                owner = br.com.zup.ggwadera.bcb.Owner(
                    type = br.com.zup.ggwadera.bcb.Owner.OwnerType.NATURAL_PERSON,
                    name = "Bill Gates",
                    taxIdNumber = "12345678901"
                )
            )
        )

        val response = grpcClient.registerPixKey(request)
        assertFalse(response.pixId.isNullOrBlank())
        val created = pixKeyRepository.findByUuidAndClientId(UUID.fromString(response.pixId), clientId)
        assertNotNull(created)
        with(created!!) {
            assertEquals(clientId, this.clientId)
            assertEquals(KeyType.valueOf(keyType.name), this.keyType)
            assertEquals(AccountType.CONTA_CORRENTE, account.type)
            if (keyType != GrpcKeyType.RANDOM) {
                assertEquals(key, this.key)
            }
        }
    }

    @ParameterizedTest
    @MethodSource("invalidKeysProvider")
    internal fun `deve retornar erro para chave invalida`(
        key: String,
        keyType: GrpcKeyType,
        accountType: GrpcAccountType
    ) {
        val clientId = UUID.randomUUID()
        val request = NewPixKeyRequest.newBuilder()
            .setClientId(clientId.toString())
            .setKey(key)
            .setKeyType(keyType)
            .setAccountType(accountType)
            .build()

        val response = assertThrows<StatusRuntimeException> { grpcClient.registerPixKey(request) }
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
                key = "test@email.com",
                keyType = KeyType.EMAIL,
                clientId = clientId,
                account = Account(
                    participant = Account.ISPB_ITAU_UNIBANCO,
                    branch = "0123",
                    number = "012345",
                    type = AccountType.CONTA_CORRENTE
                ),
                owner = Owner(
                    name = "Bill Gates",
                    taxIdNumber = "12345678901"
                ),
                createdAt = LocalDateTime.now()
            )
        )
        val response = assertThrows<StatusRuntimeException> {
            grpcClient.registerPixKey(
                NewPixKeyRequest.newBuilder()
                    .setClientId(clientId.toString())
                    .setKey(key)
                    .setKeyType(GrpcKeyType.EMAIL)
                    .setAccountType(GrpcAccountType.CONTA_CORRENTE)
                    .build()
            )
        }
        verify(itauClient, never()).getClientInfo(clientId)
        verify(bcbClient, never()).registerKey(any())
        assertEquals(1, pixKeyRepository.count())
        assertEquals(Status.ALREADY_EXISTS.code, response.status.code)
    }

    @Test
    internal fun `deve retornar erro caso cliente nao seja encontrado no sistema itau`() {
        val clientId = UUID.randomUUID()
        whenever(itauClient.getAccount(clientId, AccountType.CONTA_CORRENTE)).thenReturn(null)

        val response = assertThrows<StatusRuntimeException> {
            grpcClient.registerPixKey(
                NewPixKeyRequest.newBuilder()
                    .setClientId(clientId.toString())
                    .setKey("test@email.com")
                    .setKeyType(GrpcKeyType.EMAIL)
                    .setAccountType(GrpcAccountType.CONTA_CORRENTE)
                    .build()
            )
        }

        verify(itauClient).getAccount(clientId, AccountType.CONTA_CORRENTE)
        assertEquals(0, pixKeyRepository.count())
        assertEquals(Status.FAILED_PRECONDITION.code, response.status.code)
    }

    @Test
    internal fun `nao deve salvar chave caso falhe registro no BCB`() {
        val clientId = UUID.randomUUID()
        val request = NewPixKeyRequest.newBuilder()
            .setClientId(clientId.toString())
            .setKeyType(GrpcKeyType.RANDOM)
            .setAccountType(GrpcAccountType.CONTA_CORRENTE)
            .build()

        whenever(itauClient.getAccount(clientId, AccountType.CONTA_CORRENTE)).thenReturn(
            AccountResponse(
                tipo = AccountType.CONTA_CORRENTE,
                instituicao = AccountResponse.BankResponse(
                    nome = "Itaú Unibanco",
                    ispb = Account.ISPB_ITAU_UNIBANCO
                ),
                agencia = "0123",
                numero = "012345",
                titular = AccountResponse.OwnerResponse(
                    id = clientId,
                    nome = "Bill Gates",
                    cpf = "12345678901"
                )
            )
        )

        whenever(bcbClient.registerKey(any())).thenReturn(null)

        val response = assertThrows<StatusRuntimeException> { grpcClient.registerPixKey(request) }
        with(response.status) {
            assertEquals(Status.FAILED_PRECONDITION.code, code)
            assertEquals("Falha ao registrar chave no Banco Central", description)
        }
        assertTrue(pixKeyRepository.count() == 0L)
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
                Arguments.of(
                    "92d0e0a9-849a-4503-9a77-b730e44ce6da",
                    GrpcKeyType.RANDOM,
                    GrpcAccountType.CONTA_CORRENTE
                ),
                Arguments.of("", GrpcKeyType.RANDOM, GrpcAccountType.ACCOUNT_TYPE_UNSPECIFIED),
                Arguments.of("", GrpcKeyType.KEY_TYPE_UNSPECIFIED, GrpcAccountType.CONTA_CORRENTE)
            )
        }
    }

    @Factory
    class Client {
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): RegisterKeyServiceGrpc.RegisterKeyServiceBlockingStub {
            return RegisterKeyServiceGrpc.newBlockingStub(channel)
        }
    }
}