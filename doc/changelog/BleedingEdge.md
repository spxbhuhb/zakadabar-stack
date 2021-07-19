# Bleeding Edge

Bleeding edge releases contain all the breaking changes we plan to introduce
next month.

## Breaking Changes

- `Authorizer<EmptyEntityBo>` has to be replaced with `Authorizer<BaseBo>`
- `object : EmptyAuthorizer<EmptyEntityBo>()` has to be replaced with `object : Authorizer<BaseBo>`
- `SimpleRoleAuthorizer<EmptyEntityBo>` has to be replaced with `SimpleRoleAuthorizer<BaseBo>`
- `object : KtorRouter<T>(this) {` has to be replaces with `object : KtorEntityRouter<T>(this)`

## Project

- move dependency versions into buildSrc:Versions.kt
- split core into core-core and core-android
- move doc out of core

## Core

added:

- `BusinessLogicCommon` class
- `schemaValidator` global variable
- `ValidatorProvider` interface
- `SchemaValidatorProvider` class
- ZkLite: JDBC driver for Android SQLite

changed:

- move global `routerProvider`, `auditorProvider`, `validatorProvider` to common as `expect`
- `ActionBo` type parameter upper bound is now `Any` instead of `BaseBo`
- update authorizer, auditor, router, validator for `ActionBo<T:Any>`
- provide truly entity-independent actions and queries
    - `ActionBusinessLogic*` now extends `BusinessLogicCommon` instead of `EntityBusinessLogicCommon`
    - `QueryBusinessLogic*` now extends `BusinessLogicCommon` instead of `EntityBusinessLogicCommon`
    - `KtorRouter` now does not handle entities, `KtorEntityRouter` does

## Cookbook

New sub-project for code examples.

added:

- Standalone Action BL For Logged-In Users
- Standalone Query BL With Public Access
- Access Server Description On the Frontend
- Use Font Files
- Database Transfer

## Site

- doc is not bundled with the site but pulled from git