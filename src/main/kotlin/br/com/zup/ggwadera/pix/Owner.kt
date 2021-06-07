package br.com.zup.ggwadera.pix

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class Owner (
    @Column(name = "owner_name", nullable = false)
    val name: String,

    @Column(name = "owner_tax_id", nullable = false, length = 11, updatable = false)
    val taxIdNumber: String
)