package br.com.zup.ggwadera.util.extensions

import java.util.*

fun String.toUUID(): UUID = UUID.fromString(this)