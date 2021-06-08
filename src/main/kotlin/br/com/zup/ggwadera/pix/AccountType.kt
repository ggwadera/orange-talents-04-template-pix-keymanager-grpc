package br.com.zup.ggwadera.pix

import br.com.zup.ggwadera.AccountType

enum class AccountType {
    CONTA_CORRENTE,
    CONTA_POUPANCA;

    fun toGrpc() = AccountType.valueOf(this.name)
}