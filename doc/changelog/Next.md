# Next

This page contains the changes included in the next release.

**NOTE** Changes with marker **very low** and such are technically breaking changes. However, they are
not major modifications and in most cases they should not break anything. Notes after the marker
are the places worth to check. If you extended them you may have to apply the change to your own class also.


## Core

**added**

- `CommConfig` class to store configuration parameters
- `config` parameter to `ActionCommInterface`, `EntityCommInterface`, `QueryCommInterface`
- `config` parameter to constructor of `ActionBoCompanion`, `EntityBoCompanion`, `QueryBoCompanion`
- 


**changed**

- `ActionCommInterface.action` replace `baseUrl` with `config` **low**
- `QueryCommInterface.query` add `config` parameter
