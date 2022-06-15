## Inter-dependent Selects

Implementing selects that depend on the value of each other is usually hard 
whit imperative code. However, with reactive code there is almost nothing to
do, the interdependency works automatically assuming that you use the value
of the first select in the query of the second. 

When `bo.a1` changes, the field that belongs to `bo.a2` is patched (as it uses
`bo.a1`). As a result, the query is re-run and the field will show the values 
filtered by `bo.a1`.

```kotlin
+ bo::a1 query { /* ... */ }
+ bo::a2 query { /* ... */ bo.a1 /* ... */ }
```