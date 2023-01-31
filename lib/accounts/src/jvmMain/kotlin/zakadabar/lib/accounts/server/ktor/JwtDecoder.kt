/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.server.ktor

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.RSAKeyProvider
import java.net.URL
import java.security.interfaces.RSAPublicKey

class JwtDecoder(jwks: URL? = null) {

    private val jwkProvider : JwkProvider? by lazy { jwks?.let { JwkProviderBuilder(it).build() } }

    fun decode(jwt: String): DecodedJWT {
        val decoded = JWT.decode(jwt)

        if (jwkProvider != null) {
            val algorithm = algorithmById(decoded.algorithm)
            val verifier = JWT.require(algorithm).acceptLeeway(71).build()
            verifier.verify(decoded)
        }

        return decoded
    }

    private fun algorithmById(id: String) : Algorithm = when(id) {
        "RS256" -> Algorithm.RSA256(rsaKeyProvider)
        "RS384" -> Algorithm.RSA384(rsaKeyProvider)
        "RS512" -> Algorithm.RSA512(rsaKeyProvider)
        else -> throw UnsupportedOperationException("Algorithm not supported: $id")
    }

    private val rsaKeyProvider by lazy {
        object : RSAKeyProvider {
            override fun getPublicKeyById(kid: String) = jwkProvider!!.get(kid).publicKey as RSAPublicKey
            override fun getPrivateKey() = null
            override fun getPrivateKeyId() = null
        }
    }

}
