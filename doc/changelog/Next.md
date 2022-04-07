# Next

This page contains the changes included in the next release.

**NOTE** Changes with marker **very low** and such are technically breaking changes. However, they are
not major modifications and in most cases they should not break anything. Notes after the marker
are the places worth to check. If you extended them you may have to apply the change to your own class also.

## Core

**added**

- `dropColumnIfExists` convenience method for Exposed tables

**changed**

- select field dropdown width is now aligned with field width #102
- `UUID` now uses a string serializer instead of the default

**removed**

- `zakadabar.core.util.fork` function (create a CoroutineScope and use launch)

## Lib: Schedule

**fixed**

- loading UUID from the Yaml configuration file now works for string UUID
- subscription persistence handling problems
- DB upgrade problem with the `node_address` column

## Android

**added**

- `ZkLiteDriver.useByteArrayBlob` when `true` the ResultSet returns with ByteArray instead of ZkLiteBlob