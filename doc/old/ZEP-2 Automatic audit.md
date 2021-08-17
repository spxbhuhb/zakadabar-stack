Component: Backend
Importance: Medium

Add automatic audit function to the backend with operating modes:

* no audit
* audit data changes
* audit all requests
* audit requests and results

Option to audit failed request as well.

These audit logs should be available from the web interface for
inspection.

An audit record should contain:

* timestamp
* session id
* account id
* request result (response code)
* request data
* response data
