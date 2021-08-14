/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.util

@PublicApi
external fun decodeURIComponent(encodedURI: String): String

/**
 * This is a javascript standard function.
 * TODO IIRC there is a Ktor Client implementation for this that is MPP
 */
@PublicApi
external fun encodeURIComponent(encodedURI: String): String