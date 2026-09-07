# UMS ERP V2 — Future Implementation Prompt

You are working on a production Angular 19 university ERP. Do not copy legacy page styling. Preserve business behavior and API contracts, but use the UMS enterprise design system.

## Architecture
- Put cross-cutting infrastructure in `core`.
- Put reusable UI, models, enums and validators in `shared`.
- Keep each business domain inside `features/<domain>`.
- Keep pages thin; business behavior belongs in services.
- Prefer typed interfaces and readonly constants over `any` and duplicated literals.
- Never hard-code API hosts in components or services.

## UI
- Angular Material 19 is the base component library.
- Use the global tokens in `src/styles.scss`.
- Component SCSS should contain only page-specific layout; never duplicate global card, field, button or table styling.
- Every page needs a clear page header, content hierarchy, loading state, empty state and error state where applicable.
- Tables scroll inside their own region on small screens; the document must never horizontally overflow.
- Forms use responsive grids: 4/3/2 columns on desktop as appropriate, 1 column on mobile.
- Touch targets must remain comfortable on phones.
- Do not introduce a new color palette for a single page.

## Responsive breakpoints
- Mobile: <= 768px.
- Tablet: 769px–1200px.
- Desktop: > 1200px.

## Production behavior
- Preserve existing CRUD events, service calls, validation and routes.
- Add confirmation and user feedback for destructive actions.
- Avoid subscriptions that can leak; prefer Angular signals/async patterns where practical.
- Keep role authorization in guards and server-side authorization as the source of truth.
- Do not invent fake API behavior for modules whose backend contract does not exist.

## Quality gate
Before considering a feature complete:
1. TypeScript imports resolve.
2. `npm run build` passes.
3. No hard-coded backend URL remains.
4. No duplicated common SCSS remains.
5. Desktop, tablet and mobile layouts are checked.
6. Existing business behavior remains intact.
