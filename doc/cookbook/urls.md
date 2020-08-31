```text
GET /apis/3a8627/entities/43    - json / EntityRecordDTO
        /revisions/1            - binary / snapshot 1 content
        /revisions/last         - binary / last snapshot content

POST /apis/3a8627/entities      - json / EntityRecordDTO
POST /apis/3a8627/entities/43   - binary / new snapshot content

PATCH /apis/3a8627/entities/43  - json / EntityRecordDTO, change name, parent, status

GET /apis/3a8627/resolve/hello/stuff
```