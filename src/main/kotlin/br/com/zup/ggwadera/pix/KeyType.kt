package br.com.zup.ggwadera.pix

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator

enum class KeyType {

    CPF {
        override fun validate(key: String?): Boolean =
            !key.isNullOrBlank() && key.matches(CPF_REGEX) && CPFValidator().run {
                initialize(null)
                isValid(key, null)
            }
    },
    PHONE {
        override fun validate(key: String?): Boolean =
            !key.isNullOrBlank() && key.matches(PHONE_REGEX)
    },
    EMAIL {
        override fun validate(key: String?): Boolean =
            !key.isNullOrBlank() && EmailValidator().run {
                initialize(null)
                isValid(key, null)
            }
    },
    RANDOM {
        override fun validate(key: String?): Boolean = key.isNullOrBlank()
    };

    abstract fun validate(key: String?): Boolean

    companion object {
        private const val CPF_PATTERN = "^[0-9]{11}\$"
        private val CPF_REGEX = CPF_PATTERN.toRegex()

        private const val PHONE_PATTERN = "^\\+[1-9][0-9]\\d{1,14}\$"
        private val PHONE_REGEX = PHONE_PATTERN.toRegex()
    }
}