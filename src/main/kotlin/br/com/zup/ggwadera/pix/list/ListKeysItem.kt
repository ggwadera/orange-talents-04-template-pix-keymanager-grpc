package br.com.zup.ggwadera.pix.list

import br.com.zup.ggwadera.ListPixKeysReply
import br.com.zup.ggwadera.pix.Account
import br.com.zup.ggwadera.pix.KeyType
import com.google.protobuf.Timestamp
import io.micronaut.core.annotation.Introspected
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@Introspected
data class ListKeysItem(
    val uuid: UUID,
    val clientId: UUID,
    val keyType: KeyType,
    val key: String,
    val account: Account,
    val createdAt: LocalDateTime
) {

    fun toGrpcResponse(): ListPixKeysReply.PixKey = ListPixKeysReply.PixKey.newBuilder()
        .setPixId(uuid.toString())
        .setKeyType(keyType.toGrpc())
        .setKey(key)
        .setAccountType(account.type.toGrpc())
        .setCreatedAt(with(createdAt.toInstant(ZoneOffset.UTC)) {
            Timestamp.newBuilder().setNanos(nano).setSeconds(epochSecond).build()
        })
        .build()
}
