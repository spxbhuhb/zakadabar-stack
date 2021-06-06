# Snapshot

## Core

added:

- AuthorizerProvider, SimpleRoleAuthorizerProvider, see [Authorizers](/doc/guides/backend/Authorizer.md)

## Lib: Blobs

fixed:

- blob list by reference now works, read access is checked on the reference id
- ZkImagesField: image drop works in create mode