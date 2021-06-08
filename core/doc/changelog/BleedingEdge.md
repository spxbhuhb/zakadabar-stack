# Bleeding Edge

Bleeding edge releases contain all the breaking changes we plan to introduce
next month.

## Breaking Changes

**core**

- `QueryBo` type parameter can be anything (not a list by default)
- `QueryBoCompanion` has no type parameter
- `ActionBoCompanion` has no type parameter  

**lib: blobs**

- `BlobBo.disposition` new property
- `zkImageStyles` rename to `blobStyles`
- `zkFormStyles.imageDropArea` - move to `blobStyles`
- `zkFormStyles.imageDropAreaMessage` - move to `blobStyles`

**lib: i18n**

- `Locale.status` new property
- `Locales` rename to `LocaleCrud`
- `Translations` rename to `TranslationCrud`

## Migration

Use search & replace **with regex** on the whole project and replace:

`QueryBoCompanion<[^>]+>` with `QueryBoCompanion`

---

Use search & replace **with regex** on the whole project and replace:

`ActionBoCompanion<[a-zA-Z0-9]+>` with `ActionBoCompanion`

---

Search for `QueryBo<T>` and replace it with `QueryBo<List<T>>` (manually).

---

Search for `QueryBusinessLogicBase` and add `List` to second parameter

`class SimpleStandaloneQueryBl : QueryBusinessLogicBase<SimpleStandaloneQuery, List<SimpleQueryResult>>(`

---

## Core

added:

- `application.stringStores`
- `ZkStringStore.childStores`

changed:

- `QueryBo` type parameter is not a list
- `QueryBoCompanion` has no type parameter
- `ActionBoCompanion` has no type parameter  
- `Router.query` change type parameter RQ from Any to QueryBo<RS> to ensure type safety
- `ZkStringStore.merge` - also merge into child stores

## Lib: Blobs

added:

- `ZkAttachmentField` - form field to handle attachments
- `ZkAttachmentEntry` - entry in the attachment list
- filtering by of `disposition`

## Lib: Content

- new content library for basic content management