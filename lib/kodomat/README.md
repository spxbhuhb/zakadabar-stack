Simple code generator utility for Zakadabar.

Common

| Class | Description |
| --- | --- |
| ClassGenerator | Generates source code for classes. |
| PropertyGenerator | Generate code for one given property. |
| ValidationRuleGenerator | Generate code for a validation rule. |

Frontend

| Class | Description |
| --- | --- |
| Kodomat | Top-level editor, calls generator. |
| EditorEntry | Entry for one field. |
| SchemaParameter | Schema parameters editor for a given data type. |

## UI structure

* Kodomat
  * Editor
    * descriptorDto
    * list of EditorEntry 
       * name
       * type
       * optional
       * schemaParameter  

## Generation

Editor.onPause:

  * set descriptorDto fields
  * build property list with EditorEntry.generator (PropertyGenerator)
    * 
      

