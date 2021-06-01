package br.com.zup.ggwadera.pix

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class KeyTypeTest {

    @ParameterizedTest
    @CsvSource(
        "48642244046   , true",
        "B8YdgdsY      , false",
        "              , false",
        "''            , false",
        "01234567891   , false",
        "729.376.120-16, false"
    )
    internal fun `deve validar CPF corretamente`(value: String?, expected: Boolean) {
        assertEquals(expected, KeyType.CPF.validate(value))
    }

    @ParameterizedTest
    @CsvSource(
        "+5511987654321, true",
        "11987654321   , false",
        "4aFyTB        , false",
        "              , false",
        "''            , false"
    )
    internal fun `deve validar numero de celular corretamente`(value: String?, expected: Boolean) {
        assertEquals(expected, KeyType.PHONE.validate(value))
    }

    @ParameterizedTest
    @CsvSource(
        "valid@email.com   , true",
        "notvalid.email.com, false",
        "also not valid    , false",
        "                  , false",
        "''                , false"
    )
    internal fun `deve validar email corretamente`(value: String?, expected: Boolean) {
        assertEquals(expected, KeyType.EMAIL.validate(value))
    }

    @ParameterizedTest
    @CsvSource(
        "                                    , true",
        "''                                  , true",
        "jcfRQ                               , false",
        "52291b90-2cfc-40c3-be42-e341a57c406c, false",
    )
    internal fun `deve validar se chave aleatoria e vazia`(value: String?, expected: Boolean) {
        assertEquals(expected, KeyType.RANDOM.validate(value))
    }
}