# Markdown View

[ZkElement](../../core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/ZkElement.kt) to
display [Markdown](https://daringfireball.net/projects/markdown/) content.

Uses [intellij-markdown](https://github.com/JetBrains/markdown) to parse the source.

Uses [highlight.js](https://highlightjs.org/usage/) for syntax highlight.

The source is parsed and then transformed into tree of ZkElement components.

Check the [components](src/jsMain/kotlin/zakadabar/lib/frontend/markdown/components) directory for the components.

## Dependency

```kotlin
 sourceSets["commonMain"].dependencies {
    implementation("hu.simplexion.zakadabar:lib-markdown:2021.4.6-SNAPSHOT")
}
```

For syntax highlighting you have to add the CSS to your index.html.

```html
<link href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/10.7.2/styles/default.min.css" rel="stylesheet">
```

## Usage

```kotlin
object Page : ZkPage() {

    override fun onCreate() {
        style {
            overflowY = "scroll"
        }
        
        io {
            val source = window.fetch("https://raw.githubusercontent.com/spxbhuhb/zakadabar-stack/master/README.md").await().text().await()
            + MarkdownView(source)
        }
    }
    
}
```

## Markdown to Component Mapping

Check the `lib` property of [MarkdownView](src/jsMain/kotlin/zakadabar/lib/frontend/markdown/MarkdownView.kt)'s
companion.

To change the mapping globally, replace the content of `lib` in the companion.

To change for one instance, pass a different one in the `lib` parameter
of [MarkdownView](src/jsMain/kotlin/zakadabar/lib/frontend/markdown/MarkdownView.kt)'s constructor.

To get the parsed tree for debugging use `view.dump("", view.parsedTree)`.

## Styles

The components use `defaultStyles` property
of [MarkdownView](src/jsMain/kotlin/zakadabar/lib/frontend/markdown/MarkdownView.kt)'s companion.

To change styles globally, replace `defaultStyles` in the companion.

To change for one instance, pass a different one in the `defaultStlyes` parameter
of [MarkdownView](src/jsMain/kotlin/zakadabar/lib/frontend/markdown/MarkdownView.kt)'s constructor.
