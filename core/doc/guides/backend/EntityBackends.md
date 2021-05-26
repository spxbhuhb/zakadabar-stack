# Entity Backends

Entity backends consist of two parts: business logic (BL) and persistence API (PA).
See [Introduction](../Introduction.md) for the definition of these terms.

### Cache Control

Entity backend routing calls the `apiCacheControl` method of the BL to add a
`Cache-Control` header to responses created by `read`, `all`, `blobMetaList` and
`blobMetaRead`.

* `apiCacheControl` calls `server.apiCacheControl` by default
* `server.apiCacheControl` sets the header to the value of `apiCacheControl` in the server settings
* default of `apiCacheControl` is `no-cache, no-store`

So, you can change cache control handling:

* globally, by setting the value in the configuration file
* globally, by extending [Server](/src/jvmMain/kotlin/zakadabar/stack/backend/Server.kt)
* locally, by overriding the method in the BL 