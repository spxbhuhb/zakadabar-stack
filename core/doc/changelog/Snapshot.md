# Snapshot

## Frontend: Browser

### Added

- `ZkElement.h1`: builder
- `ZkElement.h2`: builder
- `ZkElement.h3`: builder
- `ZkElement.h4`: builder
- form: `ZkFieldBase.labelText` to store label text, can be changed any time
- form: `ZkElement.label` to customize labels easily
- form: `ZkElement.newSecret` to set `autocomplete="new-password"` easily
- form: `withoutFieldGrid` function to store the fields in the section when fieldGrid = false  
- form: documentation

### Changed

### Deprecated

- `ZkFieldBase.label` - use `labelText` instead
- `ZkCrudTarget.pageClass` - use `editorClass` instead

## Lib:Bender

### Added

- add documentation and changelog link to the header
- reorganize buttons to be more logical
- add include instructions to the result

### Changed

- package names are set according convention

### Fixed

- deleted fields are in the generated code
- `id` field is not added to the schema
