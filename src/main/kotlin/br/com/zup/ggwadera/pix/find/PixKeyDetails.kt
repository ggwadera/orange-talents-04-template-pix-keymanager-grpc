package br.com.zup.ggwadera.pix.find

import br.com.zup.ggwadera.PixKeyDetailsReply
import br.com.zup.ggwadera.pix.Account
import br.com.zup.ggwadera.pix.KeyType
import br.com.zup.ggwadera.pix.Owner
import br.com.zup.ggwadera.pix.PixKey
import br.com.zup.ggwadera.util.Banks
import com.google.protobuf.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import br.com.zup.ggwadera.AccountType as GrpcAccountType
import br.com.zup.ggwadera.KeyType as GrpcKeyType

data class PixKeyDetails(
    val pixId: UUID? = null,
    val clientId: UUID? = null,
    val type: KeyType,
    val key: String,
    val owner: Owner,
    val account: Account,
    val createdAt: LocalDateTime
) {
    companion object {
        fun of(pixKey: PixKey) = with(pixKey) {
            PixKeyDetails(
                pixId = uuid,
                clientId = clientId,
                type = keyType,
                key = key,
                owner = owner,
                account = account,
                createdAt = createdAt
            )
        }
    }

    fun toGrpcReply(): PixKeyDetailsReply = PixKeyDetailsReply.newBuilder()
        .setPixId(pixId?.toString() ?: "")
        .setClientId(clientId?.toString() ?: "")
        .setKey(key)
        .setKeyType(GrpcKeyType.valueOf(type.name))
        .setOwner(
            PixKeyDetailsReply.KeyOwner.newBuilder()
                .setName(owner.name)
                .setCpf(owner.taxIdNumber)
        )
        .setAccount(
            PixKeyDetailsReply.KeyAccount.newBuilder()
                .setName(Banks.nameByISPB(account.participant))
                .setBranch(account.branch)
                .setAccountNumber(account.number)
                .setAccountType(GrpcAccountType.valueOf(account.type.name))
        )
        .setCreatedAt(with(createdAt.toInstant(ZoneOffset.UTC)) {
            Timestamp.newBuilder()
                .setNanos(nano)
                .setSeconds(epochSecond)
        })
        .build()
}
