# Next

This page contains the changes included in the next release.

## Major Breaking Changes

- rename `zakadabar.stack` to `zakadabar.core`
- move packages from `zakadabar.core.backend` to `zakadabar.core`
- rename business login plugin classes:
    - `Auditor` to `BusinessLogicAuditor`
    - `Authorizer` to `BusinessLogicAuthorizer`
    - `Router` to `BusinessLogicRouter`
    - `Validator` to `BusinessLogicValidator`
    

## Core

added:

- optional module dependencies
- basic alarm support interface and fields to BusinessLogicCommon
- `LogAlarmSupport` and `LogAlarmSupport` provider  
- `Router.prepareAction function (convert strings to action function and parameter)
- `zkLayoutStyles` styles `grid1`, `grid2`, `grid3`
- `ZkTable.oneClick` open details with one click
- `ZkTable` now implements ZkFieldBackend

changed:

- move RoutedModule from jvmMain to commonMain
- fields and methods of tab container classes are now open
- `ZkFieldBase` now has no type parameter
- `ZkFieldBase` uses ZkFieldBackend instead the form itself  
- `ZkFieldBase.readOnly` is not an abstract field

## Lib: Markdown

- grow source code sections only when needed
- move copy icon out of the source code section


## Cookbook

added:

- `RecipeParser`
- automatically add platform before source code links in code sections
- guide for cookbook and recipe for recipe

changed:

- `Recipe` move from site into cookbook
- move `recipe.yaml` content into `recipie.md`

