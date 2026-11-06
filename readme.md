# Team Note-Taking Hub 📝

A responsive, multi-user note-taking platform built with **Spring Boot 3** and **Neon PostgreSQL**. The application features dual-access permissions (Private Workspace Owners vs. Public Readers), global database search across all notes and teams, and a live community feed directory.

---

## 🌟 Key Features

* **Dual-Access Permission Model**:
  * **Owner Mode**: Accessed via a secret full UUID token. Provides full write permissions (create/view notes) and workspace management.
  * **Public Reader Mode**: Accessed via a truncated 8-character SHA-256 hash token derived from the owner's UUID. Provides read-only access to team notes.
* **Unified Global Search Engine**: Scours note titles, content, author names, and team names across the entire database simultaneously, complete with direct links to visit matching team workspaces.
* **Two-Column Responsive UI**:
  * **Workspace Panel (Left)**: Renders personal/team workspace notes, creation tools (for owners), or global search query results.
  * **Directory & Registration Sidebar (Right)**: Features account registration, secret link generation, and a live public feed of all registered team workspaces.
* **Session Persistence**: Saves active workspace tokens in `localStorage` for automatic return visits.

---

## 🛠️ Tech Stack

* **Backend**: Java 17+, Spring Boot 3.2.5, Spring Data JPA, Hibernate
* **Database**: Neon PostgreSQL (Serverless)
* **Frontend**: Vanilla JavaScript, HTML5, CSS3 (Single Page App served via Spring Boot Static Resources)
* **Build Tool**: Apache Maven

---

## 🚀 Running Locally

### 1. Prerequisites

Ensure you have the following installed on your machine:
* **Java Development Kit (JDK) 17** or higher
* **Git**
* A **Neon PostgreSQL** database instance (or local PostgreSQL)

---

### 2. Clone the Repository

```bash
git clone [https://github.com/rajulj-test/note-service.git](https://github.com/rajulj-test/note-service.git)
cd note-service
