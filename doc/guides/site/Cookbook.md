# Cookbook

The cookbook contains recipes, examples that show how to do a specific
task. A recipe typically has:

- a markdown file called `recipe.md` somewhere under `/doc/cookbook`
- metadata in `recipe.md`
- example source codes somewhere in the `cookbook` sub-project

Recipes have some structural rules:

1. First line is the title: `# Title of the Recipe`.
1. After the title a `yaml` block contains the metadata of the recipe.
1. Sections with title `Code` contain only lists of code references.

## Difference Between Recipes and Guides

Recipes are short and on point. They tell the reader how to do something, but 
do not explain why it works that way.

In contrasts, guides should contain explanations, in-depth information about
the topic.

The idea is that the programmer goes to the cookbook first, and tries to find
an example that is ready to go. If the example needs some customization, he/she
goes to the guides to check how to do the customization.

Guides are long and boring, recipes are short and should not require much reading.

## Internals

### RecipeParser

[RecipeParser](/cookbook/src/jvmMain/kotlin/zakadabar/cookbook/RecipeParser.kt):

- parses the markdown of the recipe
- creates a [Recipe](/cookbook/src/commonMain/kotlin/zakadabar/cookbook/Recipe.kt)
  instance that contains the metadata
- adds badge in front of code list entries to indicate target