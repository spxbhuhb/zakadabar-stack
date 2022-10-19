# Next

This page contains the changes included in the next release.

## Legend

Changes with marker **very low** and such are technically breaking changes. However, they are
not major modifications and in most cases they should not break anything. Notes after the marker
are the places worth to check. If you extended them you may have to apply the change to your own class also.

## Lib: Accounts

**fixed**

- SQL transaction conflict of multiple logins on the same account at the same time

## Core

**added**

- `KtorSettingsBo.engine` Ktor engine setting. Passed to ktor embeddedServer. Default is "io.ktor.server.netty.Netty"
- `ZK_KTOR_ENGINE` environment variable to set KtorSettingsBo.engine 
