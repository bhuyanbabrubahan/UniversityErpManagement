# ERP Layered Module Structure

Every ERP completion module follows:

`entity -> dto -> repository -> specification -> mapper -> service -> serviceImpl -> controller`

Repositories extend JpaSpecificationExecutor for search. MapStruct mappers isolate entity/API models. Services expose DTOs rather than JPA entities. Controllers contain HTTP concerns only.

Frontend follows:

`feature/{module}/{components,models,pages,services,shared}`

Pages orchestrate navigation and services; components own presentation/forms; models isolate API contracts.
