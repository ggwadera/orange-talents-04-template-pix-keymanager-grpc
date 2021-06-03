package br.com.zup.ggwadera.pix

import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
data class Account(
    @Column(name = "account_participant", nullable = false)
    val participant: String,

    @Column(name = "account_branch", nullable = false, length = 4)
    val branch: String,

    @Column(name = "account_number", nullable = false, length = 6)
    val number: String,

    @Column(name = "account_type", nullable = false)
    @Enumerated(EnumType.STRING)
    val type: AccountType
) {
    companion object {
        const val ISPB_ITAU_UNIBANCO: String = "60701190"
    }
}