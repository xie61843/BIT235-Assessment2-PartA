# BIT235 Assessment 2 – Part A: Wiki Administrator Login

**Student:** Xinle Xie
**Student ID:** S1580929
**Unit:** BIT235 Object Oriented Programming
**Technology:** Java 11, Spring Boot 2.7.18, Spring MVC, Thymeleaf, Bean Validation, Maven

---

## 1. What this submission does

This project implements the first stage of the Wiki application: a login GUI that
authenticates a single, hard-coded wiki administrator and returns either a welcome
message or an error message. No database is used in Part A, as required by the brief.

| Requirement from the brief | How it is met |
|---|---|
| Login screen presented to the web client | `templates/login.html` with username, password and Login button |
| Error page | `templates/error.html`, used for failed authentication and for system errors |
| Check submitted values against hard-coded credentials | `AuthenticationService` validates and compares the credentials |
| Correct credentials → welcome message | Redirect to `/welcome` via the Post/Redirect/Get pattern |
| Incorrect credentials → error message | Error view with the message supplied by the service |

**Hard-coded credentials**

| Field | Value |
|---|---|
| Username | `xinle` (student's first name in lower case, per the brief) |
| Password | `123` |

The username comparison is case-insensitive and ignores surrounding spaces, so
`Xinle` and ` xinle ` also authenticate. The password must match exactly.

---

## 2. How to run

### Option A – Maven (recommended)

```bash
cd wiki-admin-login
mvn spring-boot:run
```

### Option B – Executable jar

```bash
cd wiki-admin-login
mvn clean package
java -jar target/wiki-admin-login-1.0.0.jar
```

Then open <http://localhost:8080> in a browser. The root URL redirects to `/login`.

**Manual test cases**

| # | Input | Expected result |
|---|---|---|
| 1 | `xinle` / `123` | Welcome page: “Welcome, Xinle!” |
| 2 | `xinle` / `wrong` | Error page: “Invalid username or password…” |
| 3 | `barun` / `123` | Error page (only the student's first name is accepted) |
| 4 | empty / empty | Error page: “Please enter a username.” |
| 5 | `/welcome` with no name | Redirect back to `/login` |

---

## 3. MVC structure

The project separates the three MVC responsibilities and keeps the web layer away
from the credential-checking rules.

```
src/main/java/com/mp/bit235/wiki/
├── WikiLoginApplication.java          entry point (@SpringBootApplication)
├── controller/
│   └── LoginController.java           Controller: URL mappings, form binding, view selection
├── service/
│   └── AuthenticationService.java     Service: validation rules and credential decision
├── repository/
│   └── AdministratorRepository.java   Repository: the single hard-coded administrator record
└── model/
    ├── LoginForm.java                 Model: submitted username and password (+ @NotBlank)
    ├── Administrator.java             Domain object: username and password
    └── AuthenticationResult.java      Outcome object: success flag, message, display name

src/main/resources/
├── templates/
│   ├── login.html                     View: login screen
│   ├── welcome.html                   View: success page
│   └── error.html                     View: error page (also Spring's default error view)
├── static/css/login.css               Presentation only, no external framework
└── application.properties             Port, template cache and error settings
```

### Request flow for a login attempt

1. **Browser → DispatcherServlet.** The user submits `POST /login` with the two form fields.
2. **Controller.** `LoginController.submitLogin` receives the form, binds it to `LoginForm`
   through `@ModelAttribute`, and `@Valid` triggers the `@NotBlank` rules.
3. **Validation failure.** If a field is empty, `BindingResult` holds the error and the
   controller renders `error.html` without calling the service.
4. **Service.** Otherwise `AuthenticationService.authenticate(...)` normalises the username,
   asks the repository for the administrator record and compares the password.
5. **Repository.** `AdministratorRepository.findByUsername(...)` compares the normalised
   username with `xinle` and returns an `Optional<Administrator>`.
6. **Result.** The service returns `AuthenticationResult` (success or failure) rather than
   a bare `boolean`, so the controller never re-implements business rules.
7. **View selection.** Success → `redirect:/welcome?name=Xinle` (Post/Redirect/Get),
   failure → `error.html` with the message. `welcome.html` is rendered by `GET /welcome`.
8. **Rendering.** Thymeleaf resolves `th:field`, `th:text` and `th:href` on the server and
   returns HTML; `login.css` styles the card.

### Why the layers are separated

- **Controller** knows HTTP and view names, not authentication rules. It can be reused
  for a different UI without changing the rules.
- **Service** owns validation and the decision, so both a web request and a future test
  or CLI client get identical behaviour.
- **Repository** is the only class that knows where credentials come from. Part B can
  replace the hard-coded record with a database without touching the controller.
- **Model** objects carry data between layers and give Thymeleaf something to bind to.

---

## 4. Design decisions

- **Post/Redirect/Get** prevents a refresh from resubmitting the form and duplicating a login.
- **Generic error message** (“Invalid username or password”) avoids revealing whether a
  username exists.
- **Server-side validation** is authoritative. `required` attributes are deliberately not
  used in HTML so that the Bean Validation path is always exercised and demonstrable.
- **Thymeleaf** keeps the view logic on the server, which is what the MVC rubric rewards.
- **`error.html`** also acts as Spring Boot's default error view, so a 404 or 500 shows the
  same branded page instead of the Whitelabel page.
- **Display name** is derived in the service (`xinle` → `Xinle`), keeping presentation
  formatting out of the controller.

---

## 5. Code comments and ownership

Every file begins with the student's name and ID, as required:

- `.java` files: header comment block plus a comment on each significant statement.
- `.html` files: HTML comment header plus a comment on each Thymeleaf attribute group.
- `.css` file: CSS comment header plus a comment above each rule group.
- `pom.xml` and `application.properties`: comment header and inline explanations.

---

## 6. Submission

Zip the whole `wiki-admin-login` folder (including `pom.xml`, `src` and this README)
and upload it to the “Assessment 2 – Part #1” box on Moodle.

If the GitHub option is used, the repository URL is:

<https://github.com/xie61843/BIT235-Assessment2-PartA>

The repository is public and contains the same commit history that produced
this submission zip.

---

## 7. Demonstration preparation

Be ready to explain, in your own words:

1. the path of a login request from the browser through `DispatcherServlet`,
   `LoginController`, `AuthenticationService` and `AdministratorRepository`;
2. which class is the Model, the View and the Controller, and why `login.html` is a View;
3. why the password check belongs in the service rather than the controller;
4. what `@Valid`, `BindingResult` and `th:field` each do;
5. why the success path uses a redirect instead of rendering the welcome page directly.
