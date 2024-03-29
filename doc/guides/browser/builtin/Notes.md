# Notes

Toasts are small, constant messages pass important or auxiliary information to the user.

* Use [note convenience functions](/core/core/src/jsMain/kotlin/zakadabar/core/browser/note/note.kt) to display basic
  notes.
* Use [ZkNote](/core/core/src/jsMain/kotlin/zakadabar/core/browser/note/ZkNote.kt) to display fine-tuned toasts.
* Extend [zkNoteStyles](/core/core/src/jsMain/kotlin/zakadabar/core/browser/note/zkNoteStyles.kt) to override default
  styles.

For basic notes, simply call the appropriate convenience function:

```kotlin
successNote("title", "message")
```

Note convenience functions:

```kotlin
notePrimary("title", "message")
noteSecondary("title", "message")
noteSuccess("title", "message")
noteWarning("title", "message")
noteDanger("title", "message")
noteInfo("title", "message")
```

Use constructor parameters of [ZkNote](/core/core/src/jsMain/kotlin/zakadabar/core/browser/note/ZkNote.kt) to create
fine-tuned toasts.

| Parameter | Description |
| ---- | ---- |
| flavour |  Flavour of the toast, sets coloring. |
| icon |   The icon to use in the toast, overrides flavour icon. |
| iconClass | The CSS class to add to the icon, overrides flavour icon class. |
| innerClass | The CSS class to add to the inner container, overrides flavour inner class. |

To display a note built from [ZkNote](/core/core/src/jsMain/kotlin/zakadabar/core/browser/note/ZkNote.kt) use the `run`
method:

```kotlin
+ ZkNote(ZkFlavour.primary, iconSource = ZkIcons.cloudUpload).apply {
    title = "title"
    message = "message"
}
```

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="Clear">
Instead of using the clear method of the note, assign a value to the
content property. Clear destroys the internal structure of the note and
after clear it won't work.
</div>

## Built-In Notes [source code](/lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/note/NoteBasicExamples.kt)

<div data-zk-enrich="NoteBasicExamples"></div>

## Complex Note Content [source code](/lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/note/NoteFormExample.kt)

This example shows how to use a complex element (a form in this case) for a note.

<div data-zk-enrich="NoteFormExample"></div>