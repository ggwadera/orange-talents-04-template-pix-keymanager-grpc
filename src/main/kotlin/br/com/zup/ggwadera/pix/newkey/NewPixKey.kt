package br.com.zup.ggwadera.pix.newkey

import br.com.zup.ggwadera.pix.AccountType
import br.com.zup.ggwadera.pix.KeyType
import br.com.zup.ggwadera.pix.KeyType.RANDOM
import br.com.zup.ggwadera.pix.PixKey
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Introspected
@ValidPixKey
data class NewPixKey(
    @field:NotNull
    val clientId: UUID,

    @field:NotNull
    @field:Size(max = 77)
    val key: String,

    @field:NotNull
    val keyType: KeyType?,

    @field:NotNull
    val accountType: AccountType?
) {
    fun toModel() = PixKey(
        clientId = clientId,
        key = if (keyType == RANDOM) UUID.randomUUID().toString() else key,
        keyType = keyType!!,
        accountType = accountType!!
    )
}