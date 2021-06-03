package br.com.zup.ggwadera.pix.register

import br.com.zup.ggwadera.pix.AccountType
import br.com.zup.ggwadera.pix.KeyType
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Introspected
@ValidPixKey
data class NewPixKey(
    val clientId: UUID,

    @field:Size(max = 77)
    val key: String,

    @field:NotNull
    val keyType: KeyType?,

    @field:NotNull
    val accountType: AccountType?
)