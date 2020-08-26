# Entities

# Add a New Entity Type

Adding an entity type has quite a few steps, but they are mostly cut & paste.

| To-Do | Example |
| ---- | ---- | 
| your module | [Stack](../../../src/commonMain/kotlin/zakadabar/stack/Stack.kt) |
| DTO | [FolderDto](../../../src/commonMain/kotlin/zakadabar/stack/data/FolderDto.kt) |
| table for extended data (optional) | ... |
| DAO for extended data (optional) | ... |
| backend implementation | [Backend](../../../src/jvmMain/kotlin/zakadabar/stack/backend/builtin/folder/Backend.kt) |
| backend module | [Backend](../../../src/jvmMain/kotlin/zakadabar/stack/backend/builtin/folder/Module.kt) |
| frontend support | [Module](../../../src/jsMain/kotlin/zakadabar/stack/frontend/Module.kt) |
