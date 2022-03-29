# Next

This page contains the changes included in the next release.

**NOTE** Changes with marker **very low** and such are technically breaking changes. However, they are
not major modifications and in most cases they should not break anything. Notes after the marker
are the places worth to check. If you extended them you may have to apply the change to your own class also.


## Core

**added**

- option for local communication (no HTTP/Ktor) inside the same VM
- option to specify communication overrides (baseUrl, namespace, etc.) per call or per class
- `CommConfig` class to store global configuration parameters and provide helper functions
- `commConfig` property (null by default) to constructor of `ActionBoCompanion`, `EntityBoCompanion`, `QueryBoCompanion`
- `executor` and `config` optional parameter for all comm methods

**changed**

- `Executor` now contains the UUID of the account **low** (add the UUID if you manually create an executor)
- `CommBase.baseUrl` is now deprecated, replace with `CommConfig.global = CommConfig("https:/...")`
- `BlobComm.upload` now has additional parameters **very low** (pass callback as named parameter)
- `ZkFullScreenImageView` has a `deleteButton` constructor parameter **very low** (add the parameter to the call)
- `ZkImagePreview` has a `deleteButton` constructor parameter **very low** (add the parameter to the call)

## Lib:Markdown

**added**

- `shell` language