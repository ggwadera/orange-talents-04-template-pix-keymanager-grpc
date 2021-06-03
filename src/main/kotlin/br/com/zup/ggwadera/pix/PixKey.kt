package br.com.zup.ggwadera.pix

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(
    indexes = [
        Index(name = "idx_uuid", columnList = "uuid", unique = true),
        Index(name = "idx_uuid_ownerId", columnList = "uuid, owner_id")
    ]
)
class PixKey(
    @field:NotBlank
    @Column(nullable = false)
    val key: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val keyType: KeyType,

    @Embedded
    val account: Account,

    @Embedded
    val owner: Owner,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(nullable = false, updatable = false)
    val uuid: UUID = UUID.randomUUID()
}