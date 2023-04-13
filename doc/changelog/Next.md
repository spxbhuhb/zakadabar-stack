# Next

This page contains the changes included in the next release.

## Legend

Changes with marker **very low** and such are technically breaking changes. However, they are
not major modifications and in most cases they should not break anything. Notes after the marker
are the places worth to check. If you extended them you may have to apply the change to your own class also.

## Core

**added**

* `CommBase.json` field, contains the system-wide Json serializer

**changed**

* `KtorServerBuilder` now uses `CommBase.json` as the Json serializer
* `ActionCom`, `EntityComm`, `QueryComm` now uses `CommBase.json` as the Json serializer
* Json serialization now allows Double.NaN values (`allowSpecialFloatingPointValues = true`)