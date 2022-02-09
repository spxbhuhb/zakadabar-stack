# Next

This page contains the changes included in the next release.

## Breaking Changes

- account related fields added to ZkBuiltinStrings
  - may have to add override to strings.kt in applications
- value based fields are now added to the field list of the form
  - if you have added them manually, you have to remove it

## Core

**changed**

- property bound fields now use the propName of ZkFieldBase and pass null for the label

**fixed**

- documentation: AccountStatusBo to AccountStatus
- documentation: Authorizer to BusinessLogicAuthorizer
- Value fields are not added to the form's fields list #94
- ZkForm - property field - no translation #93

## Site

**fixed**

- Authorizer class names in Bender

## Cookbook

**added**

- All Fields recipe
- JVM Client recipe