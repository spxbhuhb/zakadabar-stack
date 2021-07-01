# Bleeding Edge

Bleeding edge releases contain all the breaking changes we plan to introduce
next month.

## Breaking Changes

**core**

- `QueryBo` type parameter can be anything (not a list by default)
- `QueryBoCompanion` has no type parameter
- `ActionBoCompanion` has no type parameter
- `BoSchema` does not accept empty `EntityId` by default
- `ValidityReport` now stores BOs instead of implementations
- `zkFormStyles.buttons` added `marginBlockStart` and `marginBlockEnd`
- `ZkCssStyleRule` values now are plain strings (instead of any)

**lib: blobs**

- `BlobBo` now has two type parameters
- `BlobBo.disposition` new property
- `zkImageStyles` rename to `blobStyles`
- `zkFormStyles.imageDropArea` - move to `blobStyles`
- `zkFormStyles.imageDropAreaMessage` - move to `blobStyles`
- `ZkImageField` - constructor parameter changes

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

Search for `ZkCssStyleSheet` and fix errors:

- add unit numbers, see [Size Values](/doc/guides/browser/structure/ThemesCss.md#Size-Values)
- add `.0` for double values (flexGrow)

## Core

added:

- `BoSchema.custom` - new function signature
- `CustomBoConstraint` - new constraint class  
- `ListBoSchemaEntry` - basic support for lists in schemas
- `ListBoProperty` - property class for lists
- `BoSchema.validate` - `allowEmptyId` parameter to allow empty `EntityId` 
- `ValidityReport.allowEmptyId` - when true, empty `EntityId` is allowed even for mandatory fields
- CSS value postfixes: `.px`, `.em`, see [Size Values](/doc/guides/browser/structure/ThemesCss.md#Size-Values)
- CSS convenience classes, see [Value Shorthands](/doc/guides/browser/structure/ThemesCss.md#Value-Shorthands)
- `application.stringStores`
- `ZkStringStore.childStores`
- `zkScrollBarStyles.hideScrollBar` - style to hide the scrollbar completely
- `decodeURIComponent` import from JavaScript
- `ZkCrudTarget.onBeforeAddedCreate` - last minute customization of the editor for create
- `ZkCrudTarget.onBeforeAddedRead` - last minute customization of the editor for read
- `ZkCrudTarget.onBeforeAddedUpdate` - last minute customization of the editor for update
- `ZkCrudTarget.onBeforeAddedDelete` - last minute customization of the editor for delete
- `withConfirm`  -  convenience for executing a code block after user confirmation

changed:

- `QueryBo` type parameter is not a list
- `QueryBoCompanion` has no type parameter
- `ActionBoCompanion` has no type parameter  
- `ValidityReport` now stores BOs instead of implementations  
- `Router.query` change type parameter RQ from Any to QueryBo<RS> to ensure type safety
- `ZkStringStore.merge` - also merge into child stores
- `ZkAppication` - `window.path.location` is now decoded with `decodeUriComponent` before routing
- `ZkBuiltinStrings.confirmDelete` - default text is "Are you sure you want to delete?"
- `ZkBuiltinStrings.confirmation` - default text is "Please Confirm"
- `ZkStringField` - do not show mandatory mark when empty value is allowed
- `ZkTextArea` - do not show mandatory mark when empty value is allowed
- `ZkTextArea` - remove hard-coded `flexGrow = 1` and `resize = "none"`
- `zkFormStyles.buttons` - added margin before and after

deprecated:

- `BoSchema.custom` old function with a function parameter 
- `BuiltinStrings.cannotAttachMoreImage` replaced by `cannotAddMore`

## Lib : Accounts

changes:

- `CheckName` is now a query
- `CheckNameResult` contains `AccountPublicBo` instead of `AccountPrivateBo`

## Lib: Blobs

added:

- `BlobBo` - type parameter for the referenced BO
- `BlobBo` - `disposition` field
- `BlobBoCompanion` - `byReference` method, replaces `listByReference`  
- `ZkBlobField` - base class for blob fields
- `ZkBlobEntry` - base entry class for blob field entries
- `ZkAttachmentField` - form field to handle attachments
- `ZkAttachmentEntry` - entry in the attachment list
- filtering by `disposition`
- unit tests
- `BlobBlBase` sets HTTP content type to the `mimeType` of the blob

changed:

- `zkImageStyles` rename to `blobStyles`
- `zkFormStyles.imageDropArea` - move to `blobStyles`
- `zkFormStyles.imageDropAreaMessage` - move to `blobStyles`
- `ZkImageField` - constructor parameter changes
- `BlobExposedPa` - `listByReference` renames to `byReference`
- `byReference` - filtering by disposition

deprecated:

- `BlobBoCompanion.listByReference`  -  rename to `byReference`

## Lib: Content

added :

- new content library for basic content management