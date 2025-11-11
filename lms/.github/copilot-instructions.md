# Copilot instructions for this repository (LMS Spring Boot)

This file gives concise, actionable guidance to AI coding agents working on this Spring Boot LMS project.

- Project type: Java Spring Boot 3.x (parent 3.5.6), Java 17, Maven build (`pom.xml`). Main class: `com.lmsProject.lms.LmsApplication`.
- Run locally: `./mvnw spring-boot:run` (Windows: `mvnw.cmd spring-boot:run`) from repository root. Unit tests: `./mvnw test`.

Key code layout and important files:
- `src/main/java/com/lmsProject/lms` — main package (scanBasePackages set to this package in `LmsApplication`).
  - `controller/` — MVC controllers returning Thymeleaf views for the UI and some REST endpoints (e.g., `StudentController`, `CourseController`, `StudentDeactivationController`).
  - `service/` and `service/impl/` — business logic. Prefer calling service layer from controllers.
  - `repository/` — Spring Data JPA repositories (note: some repository names contain typos, e.g. `StudentRepositoy` instead of `StudentRepository`). Use existing names when referencing types.
  - `entity/` — JPA entities (e.g., `Student`, `Course`, `StudentDeactivation`). Entities use Jakarta Persistence and Lombok.
- `src/main/resources/templates/` — Thymeleaf view templates (e.g., `deactivate-student.html`, `add-student.html`). Most forms submit via JS fetch or server-side form posts.
- `src/main/resources/application.properties` — MySQL datasource settings and JPA properties. DB: MySQL (learningManagement) and hibernate ddl-auto=update in this repo.

Patterns & conventions discovered (do not invent):
- Mix of server-rendered Thymeleaf pages and JSON endpoints. Controllers are annotated with `@Controller` (view rendering) and return `ResponseEntity` for AJAX/REST responses.
- Form handling: Some endpoints expect `@ModelAttribute` (traditional HTML form post), others expect JSON in the request body (JS fetch). Example: `CourseController.addCourse(@ModelAttribute Course)` vs `StudentController.addStudent(@RequestBody Student)`.
- Repositories use method-name queries and occasional `@Query` for counts. Example: `StudentRepositoy#getCountOfEmail(String email)`.
- Entities use Lombok; some relationships are commented out (e.g., Student.enrollments). Respect current model shape when editing DB or templates.
- Deactivation pattern: Deactivations are recorded in separate `*Deactivation` entities with controllers under `/inactive` (e.g., `/inactive/add/{studentId}`). Deactivation forms (templates) POST JSON via JS fetch.

Developer workflows (essential commands):
- Build & run (Windows PowerShell):
  - Start app: `.\mvnw.cmd spring-boot:run`
  - Package: `.\mvnw.cmd package`
  - Run tests: `.\mvnw.cmd test`
- Database: project expects a MySQL server accessible at `jdbc:mysql://localhost:3306/learningManagement`. If unavailable, tests or run will fail; consider using an in-memory DB for local tests if modifying tests.

Common pitfalls and codebase quirks:
- Typo in repository class name `StudentRepositoy` — do not rename lightly; many files reference it. If you rename, update all usages and imports, and run a full build.
- Controllers mix `@ResponseBody/ResponseEntity` and view rendering — when adding new endpoints, match the style used by nearby controllers (use `@Controller` for views and `@RestController` for pure JSON APIs).
- Templates rely on JS fetch for some actions (see `deactivate-student.html`) — updating endpoints used by those templates requires ensuring the response status and redirect behavior matches existing client-side code.
- Application.properties contains plaintext DB credentials. Avoid committing secrets in future changes; for local development, use environment variables or Spring profiles.

What to change and how to verify:
- When changing a controller method signature, update the corresponding Thymeleaf template and any JS fetch calls.
- When adding repository methods, prefer method-name queries (Spring Data JPA) used elsewhere, or add `@Query` if necessary.
- Run `.\mvnw.cmd -DskipTests=false test` after changes to validate compilation and basic tests.

Examples to copy from code:
- Deactivate student (client -> controller): template `templates/deactivate-student.html` posts JSON to `/inactive/add/{studentId}`; controller `StudentDeactivationController.deactivateStudent` handles it and delegates to `StudentDeactivationService`.
- Search pattern: controllers accept optional `keyword` param and call service.search* methods; e.g., `CourseController.viewCourses` and `StudentController.viewStudents`.

If additional context is needed:
- Ask for DB access details or whether to switch tests to an in-memory DB.
- Ask if repository typos (e.g., `StudentRepositoy`) should be corrected globally.

If you want, I can also:
- Add short unit test examples for services.
- Convert a controller to `@RestController` + frontend API if you plan to separate UI and API.

Please review and tell me if you'd like more detail on any area (endpoints, templates, or build/test matrix).