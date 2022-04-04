# Upload And Download Attachments

```yaml
level: Beginner
targets:
  - js
  - jvm
tags:
  - blob
  - attachment
  - form
```

This recipe shows how to use blob fields to upload and download attachments.

You can use any entity (a BO that extends EntityBo, see [Data](/doc/guides/common/Data.md)) as the
reference for the attachment. The referenced entity is the one you attach files to.

<div data-zk-enrich="AttachmentsField"></div>

## Guides

- [Blobs](/doc/guides/libraries/blobs/Introduction.md)
- [Forms](/doc/guides/browser/builtin/Forms.md)

## Code

- [AttachmentBlob.kt](/cookbook/src/commonMain/kotlin/zakadabar/cookbook/blob/attachment/AttachmentBlob.kt)
- [AttachmentsField.kt](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/blob/attachment/AttachmentsField.kt)
- [AttachmentBlobBl.kt](/cookbook/src/jvmMain/kotlin/zakadabar/cookbook/blob/attachment/AttachmentBlobBl.kt)
- [AttachmentBlobPa.kt](/cookbook/src/jvmMain/kotlin/zakadabar/cookbook/blob/attachment/AttachmentBlobPa.kt)

