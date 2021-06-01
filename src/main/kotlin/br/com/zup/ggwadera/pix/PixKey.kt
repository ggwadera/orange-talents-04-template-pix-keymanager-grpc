package br.com.zup.ggwadera.pix

import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(
    indexes = [
        Index(name = "idx_uuid", columnList = "uuid", unique = true),
        Index(name = "idx_uuid_clientId", columnList = "uuid, clientId")
    ]
)
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

    @Column(nullable = false)
    val uuid: UUID = UUID.randomUUID()
}