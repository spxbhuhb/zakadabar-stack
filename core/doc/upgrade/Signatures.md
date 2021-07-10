# Signatures

Our builds are signed with a GPG key that belongs to `build@simplexion.hu`.

To verify the signature you need a gpg program and our public key. The key of
`build@simplexion.hu` is published on [keys.opengpg.org](https://keys.opengpg.org).

To download the public key, use the site above or the command below:

```text
gpg --recv-keys DE894262C98DC23860292C8C7213DCE1CDE7FA07
```

```text
gpg --verify filename.asc filename
```

Example:

```text
gpg --verify 2021.6-to-2021.7.5.jar.asc 2021.6-to-2021.7.5.jar
```

```text
gpg: Signature made Thu Jul  8 10:43:13 2021 CEST
gpg:                using RSA key DE894262C98DC23860292C8C7213DCE1CDE7FA07
gpg: Good signature from "Simplexion Kft. <build@simplexion.hu>" [unknown]
gpg: WARNING: This key is not certified with a trusted signature!
gpg:          There is no indication that the signature belongs to the owner.
Primary key fingerprint: DE89 4262 C98D C238 6029  2C8C 7213 DCE1 CDE7 FA07
```

The warning above means that this key is not ultimately trusted which is the
default. You can change this with the `gpg --lsign-key`.
