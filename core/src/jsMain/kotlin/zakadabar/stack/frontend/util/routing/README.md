

```kotlin
route("/1a2b3c/entities") {

   route("whatever") {
       handle {
           ShowListOfSomething(call.parameters)
       } 
   }
 
   handle {
       ShowListOfSomething(call.parameters)
   }

   handle("/new") {
       CreateSomething()
   }

   handle("/{entityId}") {
       ShowSomething(call["entityId"].toLong())
   }

   handle("/{entityId}/edit") {
       EditSomething(call["entityId"].toLong())
   }

}
```