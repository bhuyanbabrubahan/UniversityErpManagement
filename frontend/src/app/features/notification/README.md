# Notification Module — Full Redesign

Angular standalone notification module matching the supplied Spring Boot APIs.

## Included
- Paginated list using `GET /api/erp/notification?page=&size=`
- Server-side search using `POST /api/erp/notification/search`
- Create / update / view / delete
- Direct send page using `POST /api/v1/notifications/send`
- Responsive SCSS for desktop, tablet and mobile
- Compact small-font ERP UI
- Reactive-form validation
- Loading, empty and error states
- Status/channel/role badges and pagination

## Expected existing project
- Angular standalone components
- `environment.apiUrl`
- `provideHttpClient()` configured in the application
- Existing Spring Security/authentication interceptor if required by the API

## Route
Mount `NOTIFICATION_ROUTES` under the desired notification parent route.
