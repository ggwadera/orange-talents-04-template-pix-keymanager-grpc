package br.com.zup.ggwadera.bcb

import br.com.zup.ggwadera.itau.AccountResponse
import br.com.zup.ggwadera.pix.Account
import br.com.zup.ggwadera.pix.KeyType
import br.com.zup.ggwadera.pix.PixKey
import br.com.zup.ggwadera.pix.register.NewPixKey
import io.micronaut.http.MediaType.APPLICATION_XML
import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client
import java.time.LocalDateTime
import java.util.*
import br.com.zup.ggwadera.pix.AccountType as PixAccountType
import br.com.zup.ggwadera.pix.Owner as PixOwner

@Client("\${services.bcb}")
@Consumes(APPLICATION_XML)
@Produces(APPLICATION_XML)
interface BcbClient {

    @Post("/api/v1/pix/keys")
    fun registerKey(@Body body: CreatePixKeyRequest): CreatePixKeyResponse?

    @Delete("/api/v1/pix/keys/{key}")
    fun deleteKey(@PathVariable key: String, @Body body: DeletePixKeyRequest): DeletePixKeyResponse?

}

data class CreatePixKeyRequest(
    val key: String,
    val keyType: KeyType,
    val bankAccount: BankAccount,
    val owner: Owner
) {
    constructor(pixKey: NewPixKey, account: AccountResponse) : this(
        key = pixKey.key,
        keyType = pixKey.keyType!!,
        bankAccount = BankAccount(account.toModel()),
        owner = Owner(account.titular.toModel())
    )
}

data class CreatePixKeyResponse(
    val key: String,
    val keyType: KeyType,
    val createdAt: LocalDateTime,
    val bankAccount: BankAccount,
    val owner: Owner
) {
    fun toModel(clientId: UUID): PixKey = PixKey(
        key = key,
        keyType = keyType,
        clientId = clientId,
        account = bankAccount.toModel(),
        owner = owner.toModel(),
        createdAt = createdAt
    )
}

data class DeletePixKeyRequest(
    val key: String,
    val participant: String
)

data class DeletePixKeyResponse(
    val key: String,
    val participant: String,
    val deletedAt: LocalDateTime
)

data class BankAccount(
    val participant: String,
    val branch: String,
    val accountNumber: String,
    val accountType: AccountType
) {
    fun toModel(): Account = Account(
        participant = participant,
        branch = branch,
        number = accountNumber,
        type = accountType.mapping
    )

    constructor(account: Account) : this(
        Account.ISPB_ITAU_UNIBANCO,
        account.branch,
        account.number,
        AccountType.of(account.type)
    )

    enum class AccountType(val mapping: PixAccountType) {
        CACC(PixAccountType.CONTA_CORRENTE),
        SVGS(PixAccountType.CONTA_POUPANCA);

        companion object {
            fun of(accountType: PixAccountType): AccountType =
                when (accountType) {
                    PixAccountType.CONTA_CORRENTE -> CACC
                    PixAccountType.CONTA_POUPANCA -> SVGS
                }
        }
    }
}

data class Owner(
    val type: OwnerType,
    val name: String,
    val taxIdNumber: String
) {
    fun toModel(): PixOwner = PixOwner(
        name = name,
        taxIdNumber = taxIdNumber
    )

    constructor(owner: PixOwner) : this(OwnerType.NATURAL_PERSON, owner.name, owner.taxIdNumber)

    enum class OwnerType {
        NATURAL_PERSON,
        LEGAL_PERSON
    }
}