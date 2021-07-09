# Next

This page contains the changes included in the next release.

## Core

added:

- `SimpleRoleAuthorizer.LOGGED_IN` - instance variable to avoid need for import
- `SimpleRoleAuthorizer.PUBLIC` - instance variable to avoid need for import
- `SelectOptionProvider` - interface to make form select definition easier

changed:

- most business logic code moved from `jvmMain` to `commonMain` (should not break anything)
- `ValidityReport` is now a BaseBo
- `BadRequest` sends validity report
- `SchemaValidator` throws `BadRequest` with validity report

fixed:

- errors in upgrade documentation 

## Lib: Accounts

- `AccountList.asSelectProvider` - convenience method to easily build selects for accounts

## Lib: Markdown

- code blocks and tables now use up to 90% of the screen