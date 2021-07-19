# New Property Type

To add a new property type Xx:

* schema
    * DtoSchema.kt
        * `unaryPlus`
    * entries
        * `XxBoSchemaEntry`
        * `OptXxBoSchemaEntry`
    * descriptor
        * property.kt
            * `XxBoProperty`
* form
    * fields
        * `ZkXxField`
        * `ZkOptXxField`
    * ZkForm.kt
        * `unaryPlus`
* table
    * columns
        * `ZkXxColumn`
        * `ZkOptXxColumn`
* bender
    * propertyConstraintEditors.kt
        * `XxPropertyConstraintsEditor`
    * KtToBoDescriptor.kt
        * add to `buildProperty`
    * BoPropertyEditor
        * add to `typeMap`
    * documentation 
* examples
    * BuiltinBo
        * add fields
        * form
        * table
        * BL
        * PA