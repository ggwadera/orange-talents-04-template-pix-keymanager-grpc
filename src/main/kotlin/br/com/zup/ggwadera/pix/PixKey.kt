package br.com.zup.ggwadera.pix

import org.hibernate.annotations.Generated
import org.hibernate.annotations.GenerationTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class PixKey(
    @field:NotNull
    @Column(nullable = false)
    val clientId: UUID,

    @field:NotBlank
    @Column(nullable = false)
    val key: String,

    @field:NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val keyType: KeyType,

    @field:NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val accountType: AccountType
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(nullable = false, columnDefinition = "uuid default gen_random_uuid()")
    @Generated(GenerationTime.INSERT)
    lateinit var uuid: UUID
        private set
}