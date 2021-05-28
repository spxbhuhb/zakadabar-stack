# Auditor

Auditors are responsible for writing the audit trail of requests. They are
quite different from database logs because:

- they know which account sent the request,
- they see the request BO data which may be very far from the database.

<div data-zk-enrich="Note" data-zk-flavour="Danger" data-zk-title="Endpoint Only">

The automatic call of auditors works for public endpoints only (access from outside).
If you use cross-BL calls, you have to audit manually or call the `wrapper` version
of the functions to kick off full processing.

</div>

## Built-in Auditors

[LogAuditor](/src/jvmMain/kotlin/zakadabar/stack/backend/audit/LogAuditor.kt) - writes the audit records to the standard log

## Write an Auditor

Three options to write auditors:

- write a re-usable one by implementing the [Auditor](/src/jvmMain/kotlin/zakadabar/stack/backend/audit/Auditor.kt) interface,
- write a re-usable one by extending one,
- write an anonymous one specific to one business logic class.

The first two are trivial, the third is also simple. The example below extends the LogAuditor to suppress audit of 
and queries.

```kotlin
class SimpleExampleBl : EntityBusinessLogicBase<SimpleExampleBo>(
    boClass = SimpleExampleBo::class
) {

    override val pa = SimpleExampleBoExposedPaGen()

    override val auditor = object : LogAuditor<SimpleExampleBo> {
        override fun auditQuery(executor: Executor, bo: QueryBo<*>) {
            // skip query audits
        }
    }
    // ... other codes of BL ...
}
```

## Use an Auditor

Most cases you don't have to worry about using an auditor. Once it is assigned
to a business logic, it will be called automatically whenever the endpoints
offered by the BL receive a request.