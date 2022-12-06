# Next

This page contains the changes included in the next release.

## Legend

Changes with marker **very low** and such are technically breaking changes. However, they are
not major modifications and in most cases they should not break anything. Notes after the marker
are the places worth to check. If you extended them you may have to apply the change to your own class also.

## Core

**changed**

If `styles.useNativeDateInput` is true, using date time picker in the following fields:

- ZkPropLocalDateTimeField
- ZkPropOptLocalDateTimeField
- ZkValueLocalDateTimeField
- ZkValueOptLocalDateTimeField
