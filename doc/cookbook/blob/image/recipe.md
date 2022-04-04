# Upload And Download Images

```yaml
level: Beginner
targets:
  - js
  - jvm
tags:
  - blob
  - image
  - form
```

This recipe shows how to use blob fields to upload and manage images.

You can use any entity (a BO that extends EntityBo, see [Data](/doc/guides/common/Data.md)) as the
reference for the image. The referenced entity is the one you attach images to.

<div data-zk-enrich="ImagesField"></div>

## Guides

- [Blobs](/doc/guides/libraries/blobs/Introduction.md)
- [Forms](/doc/guides/browser/builtin/Forms.md)

## Code

## Code

- [ImageBlob.kt](/cookbook/src/commonMain/kotlin/zakadabar/cookbook/blob/image/ImageBlob.kt)
- [ImagesField.kt](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/blob/image/ImagesField.kt)
- [ImageBlobBl.kt](/cookbook/src/jvmMain/kotlin/zakadabar/cookbook/blob/image/ImageBlobBl.kt)
- [ImageBlobPa.kt](/cookbook/src/jvmMain/kotlin/zakadabar/cookbook/blob/image/ImageBlobPa.kt)
