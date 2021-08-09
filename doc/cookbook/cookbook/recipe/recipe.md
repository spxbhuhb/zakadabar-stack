# Cookbook Recipe

```yaml
level: Beginner
targets:
  - common
tags:
  - cookbook
  - recipe
  - documentation
```

## Write a Recipe

Create a directory in `/doc/cookbook` and put a `recipe.md` file into it.

If your recipe has code examples, create the same directories in `/cookbook/src`
and put the source code there.

Try to avoid putting code into the recipe itself. The build process compiles
the actual source codes, so recipe errors has to be fixed for a successful
build. This helps keeping the recipes up-to-date and error free.

## Code

- [recipe.md](/doc/cookbook/cookbook/recipe/recipe.md)