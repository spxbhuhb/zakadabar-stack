/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.jwt

import com.auth0.jwt.interfaces.DecodedJWT

val DecodedJWT.upn : String?
    get() = getClaim("upn").asString()

val DecodedJWT.name : String?
    get() = getClaim("name").asString()

val DecodedJWT.email : String?
    get() = getClaim("email").asString()

val DecodedJWT.roles : Set<String>?
    get() = getClaim("roles").asArray(String::class.java)?.toSet()
