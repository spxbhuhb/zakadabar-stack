

## Native Components

Native components are written manually, without using the compiler  plugin to
translate the original function code into reactive function code. These may 
be fully reactive but all reactions has to be coded manually.

Native components provide more flexibility than translated ones, you can do
whatever you want as long as you provide the necessary component functions.

Use cases:
- 
- define class hierarchies, thus share code easily
- provide visibility for class members, thus share data easily

One specific use case where native components are very useful is building
form toolkits where the logic of field handling is quite complex (think of
validation, feedback, two-way data binding).

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