# Bleeding Edge

Bleeding edge releases contain all the breaking changes we plan to introduce
next month.

## Breaking Changes

- QueryBo type parameter can be anything (list is not enforced)
- `BlobBo.disposition` new property
- `zkImageStyles` renamed to `blobStyles`
- `Locale.status` new property  
- `Locales` renamed to `LocalesCrud`
- `zkFormStyles.imageDropArea` - moved to `blobStyles`
- `zkFormStyles.imageDropAreaMessage` - moved to `blobStyles`

## Core

added:

- `application.stringStores`
- `ZkStringStore.childStores`

changes:

- `ZkStringStore.merge` - also merge into child stores

## Lib: Blobs

added:

- `ZkAttachmentField` - form field to handle attachments
- `ZkAttachmentEntry` - entry in the attachment list
- filtering by of `disposition`

## Lib: Content

- new content library for basic content management