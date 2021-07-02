# Bleeding Edge

Bleeding edge releases contain all the breaking changes we plan to introduce
next month.

## Breaking Changes

**core**

- `QueryBo` - type parameter can be anything (not a list by default)
- `QueryBoCompanion` - has no type parameter
- `ActionBoCompanion` - has no type parameter
- `StackRoles` - change name to `appRoles` and move into `.authorize` package
- `RolesBase` - change name to `AppRolesBase` and move into `.authorize` package  
- `StackRoles.siteMember` - replace with `SimpleRoleAuthorizer.Companion.LOGGED_IN`
- `StackRoles.siteAdmin` - remove  
- `StackRoles.anonymous` - remove
- `BoSchema` does not accept empty `EntityId` by default
- `ValidityReport` now stores BOs instead of implementations
- `zkFormStyles.buttons` added `marginBlockStart` and `marginBlockEnd`
- `ZkCssStyleRule` values now are plain strings (instead of any)

**lib: accounts**

- `RolesByAccount` contains `AccountPublicBo` instead of `AccountPrivateBo`
- `KtorSessionBl` now checks for `ModuleSettings.loginActionRole` instead of `appRoles.siteMember`
- `RoleGrantExposedTable` is now a class instead of an object, use `RoleGrantExposedTableCommon` instead
- `AccountPrivateExposedTable` is now a class instead of object, use `AccountPrivateExposedTableCommon` instead
- `AccountbyRole` is now called `AccountsByRole`

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
- `TranslationBl` now requires security officer for translation changes

## Migration

Use search & replace **with regex** on the whole project and replace:

`QueryBoCompanion<[^>]+>` with `QueryBoCompanion`

`ActionBoCompanion<[a-zA-Z0-9]+>` with `ActionBoCompanion`

`import zakadabar\.stack\.StackRoles` with `import zakadabar.stack.authorize.appRoles`

`StackRoles` with `appRoles`

`import zakadabar\.stack\.RolesBase` with `import zakadabar.stack.authorize.AppRolesBase`

`StackRoles\.siteMember` with `LOGGED_IN` + add import manually

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
- Make server description available without a session #32  
- `ServerDescriptionQuery` - reads `ServerDescriptionBo` from the server
- `ServerDescriptionBl` - handles `ServerDescriptionQuery`, added automatically
- `ZkForm.onCreateSuccess`

changed:

- Kotlin 1.5.20
- Ktor 1.6.1
- serialization 1.2.1
- coroutines 1.5.0
- datetime 0.2.1  
- `QueryBo` type parameter is not a list
- `QueryBoCompanion` has no type parameter
- `ActionBoCompanion` has no type parameter  
- `ValidityReport` now stores BOs instead of implementations
- `EntityBusinessLogicBase.pa` is now public
- `SimpleRoleAuthorizer` supports `LOGGED_IN` special value  
- `Router.query` change type parameter RQ from Any to QueryBo<RS> to ensure type safety
- `ZkStringStore.merge` - also merge into child stores
- `ZkAppication` - `window.path.location` is now decoded with `decodeUriComponent` before routing
- `ZkBuiltinStrings.confirmDelete` - default text is "Are you sure you want to delete?"
- `ZkBuiltinStrings.confirmation` - default text is "Please Confirm"
- `ZkStringField` - do not show mandatory mark when empty value is allowed
- `ZkTextArea` - do not show mandatory mark when empty value is allowed
- `ZkTextArea` - remove hard-coded `flexGrow = 1` and `resize = "none"`
- `zkFormStyles.buttons` - added margin before and after
- `EmptySessionManager` - reads server description with `ServerDescriptionQuery`

deprecated:

- `BoSchema.custom` old function with a function parameter 
- `BuiltinStrings.cannotAttachMoreImage` replaced by `cannotAddMore`

removed:

- `stdlib-jdk8` gradle dependency

fixed:

- The server parameters (stack version and project name) are incorrect #51
- The GitHub task number is misleading in release note 2021.6.16. #50
- all form fields support readOnly
- can chain form field decorator functions (readOnly, label, etc.)

## Lib : Accounts

changed:

- `CheckName` is now a query
- `CheckNameResult` contains `AccountPublicBo` instead of `AccountPrivateBo`
- `RolesByAccount` contains `AccountPublicBo` instead of `AccountPrivateBo`
- `AccountPrivateExposedTable` is now a class instead of object
- `KtorSessionBl` now checks for `ModuleSettings.loginActionRole` instead of `appRoles.siteMember`
- `install` now accepts `accountPrivateBl` as parameter
- `RoleBl` constructor now requires a persistence api
- `RoleGrantExposedTable` fields are now public
- `AccountPrivateTable` fields are now public

fixed:

- Additional roles are added only when the database is empty #48

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