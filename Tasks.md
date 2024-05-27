### Füge die folgenden UI Elemente zum Plugin hinzu

Hier ist die Liste der Einträge im Kontextmenü im Editor und in der Projektansicht (Project View) von IntelliJ für das Plandex Plugin:
Kontextmenü-Einträge im Editor

Plandex > Aufgabe planen
Funktionalität: Öffnet einen Dialog zur Planung einer neuen Aufgabe im aktuellen Kontext. Nach Abschluss der Aufgabe zeigt es die Änderungen an, die in der geschützten Sandbox vorgenommen wurden, bevor sie auf das Projekt angewendet werden. Es bietet auch die Möglichkeit, Änderungen zurückzuspulen.
Vorbedingung: Überprüft, ob der OpenAI API-Schlüssel konfiguriert ist. Falls nicht, öffnet sich ein Konfigurationsdialog.
Dialoge:
Neue Aufgabe planen
Titel: "Neue Aufgabe planen"
Eingabefelder:
- Plan-Auswahl (Dropdown, inklusive Archivierungsoption und Möglichkeit zur Erstellung neuer Pläne)
- Aufgabenname
- Beschreibung
- Priorität
- Tags
- Kontext-Dateien (Liste der im Kontext enthaltenen Dateien mit Möglichkeit zur Verwaltung: Hinzufügen/Entfernen von Dateien und Verzeichnissen)
Buttons: "Erstellen", "Abbrechen"
Änderungen überprüfen und zurückspulen
Titel: "Änderungen überprüfen und zurückspulen"
Liste der Änderungen: Dateien und Änderungen mit Optionen zur Detailansicht
Buttons: "Anwenden", "Ablehnen", "Zurückspulen", "Zurück"

Kontextmenü-Einträge in der Projektansicht (Project View)

Plandex > Plan archivieren
Funktionalität: Archiviert den ausgewählten Plan.
Vorbedingung: Überprüft, ob der OpenAI API-Schlüssel konfiguriert ist. Falls nicht, öffnet sich ein Konfigurationsdialog.
Dialog:
Titel: "Plan archivieren"
Bestätigung: "Möchten Sie diesen Plan wirklich archivieren?"
Buttons: "Archivieren", "Abbrechen"

Plandex > Kontext hinzufügen
Funktionalität: Fügt die ausgewählte Datei oder das Verzeichnis zum Kontext des aktuellen Plans hinzu.
Vorbedingung: Überprüft, ob der OpenAI API-Schlüssel konfiguriert ist. Falls nicht, öffnet sich ein Konfigurationsdialog.
Dialog:
Titel: "Kontext hinzufügen"
Eingabefelder: Dateiname, Option zur Auswahl von spezifischen Verzeichnissen
Buttons: "Hinzufügen", "Abbrechen"

Weitere Dialoge und Fenster

Plandex Einstellungen
Zugriff: Über die IntelliJ-Einstellungen (File > Settings > Other Settings > Plandex)
Dialog:
Titel: "Plandex Einstellungen"
Tabs:
Allgemein: API-Schlüssel, Standardmodell
Projekte: Liste der verwalteten Projekte, Optionen zur Bearbeitung
Modelle: Hinzufügen/Entfernen von Modellen, Standardeinstellungen
Modell auswählen: Dropdown zur Auswahl eines KI-Modells (OpenAI, Claude, Gemini, etc.)
Buttons: "Speichern", "Abbrechen"

Dialog zur Konfiguration des OpenAI API-Schlüssels
Titel: "OpenAI API-Schlüssel konfigurieren"
Nachricht: "Es scheint, dass Ihr OpenAI API-Schlüssel noch nicht konfiguriert ist. Bitte geben Sie Ihren Schlüssel ein, um fortzufahren."
Eingabefelder: OpenAI API-Schlüssel
Buttons: "Speichern", "Abbrechen"

## Verwende die folgende API: 
### Authentifizierungs- und Kontoverwaltung
Diese Methoden sind wichtig für die Verwaltung von Benutzerkonten und Sitzungen innerhalb des Plugins.

1. **Benutzeranmeldung und -abmeldung**
   - `/accounts/sign_in (POST)`
   - `/accounts/sign_out (POST)`

2. **Kontoverwaltung**
   - `/accounts/start_trial (POST)`
   - `/accounts/convert_trial (POST)`
   - `/accounts/create_account (POST)`

3. **E-Mail-Verifizierung**
   - `/accounts/email_verifications (POST)`

### Organisationsverwaltung
Diese Methoden sind relevant für die Verwaltung von Organisationen und Benutzern innerhalb des Plugins.

1. **Organisationen und Sitzungen**
   - `/orgs/session (GET)`
   - `/orgs (GET, POST)`

2. **Benutzerverwaltung**
   - `/orgs/users/{userId} (DELETE)`
   - `/orgs/roles (GET)`

3. **Einladungen**
   - `/invites (POST)`
   - `/invites/pending (GET)`
   - `/invites/accepted (GET)`
   - `/invites/all (GET)`
   - `/invites/{inviteId} (DELETE)`

### Projektmanagement
Diese Methoden sind entscheidend für die Verwaltung von Projekten und Plänen innerhalb der IDE.

1. **Projekte**
   - `/projects (GET, POST)`
   - `/projects/{projectId}/set_plan (PUT)`
   - `/projects/{projectId}/rename (PUT)`
   - `/projects/{projectId}/plans/current_branches (POST)`

### Plan- und Kontextverwaltung
Diese Methoden ermöglichen die Verwaltung von Plänen, deren Kontext und den damit verbundenen Dateien.

1. **Pläne**
   - `/plans (GET)`
   - `/plans/archive (GET)`
   - `/plans/ps (GET)`
   - `/projects/{projectId}/plans (POST, DELETE)`
   - `/plans/{planId} (GET, DELETE)`
   - `/plans/{planId}/archive (PATCH)`
   - `/plans/{planId}/unarchive (PATCH)`
   - `/plans/{planId}/rename (PATCH)`

2. **Planaktionen**
   - `/plans/{planId}/{branch}/tell (POST)`
   - `/plans/{planId}/{branch}/respond_missing_file (POST)`
   - `/plans/{planId}/{branch}/build (PATCH)`
   - `/plans/{planId}/{branch}/connect (PATCH)`
   - `/plans/{planId}/{branch}/stop (DELETE)`
   - `/plans/{planId}/{branch}/apply (PATCH)`
   - `/plans/{planId}/{branch}/reject_all (PATCH)`
   - `/plans/{planId}/{branch}/reject_file (PATCH)`
   - `/plans/{planId}/{branch}/rewind (PATCH)`

3. **Kontextverwaltung**
   - `/plans/{planId}/{branch}/context (GET, POST, PUT, DELETE)`
   - `/plans/{planId}/{branch}/diffs (GET)`
   - `/plans/{planId}/{branch}/convo (GET)`
   - `/plans/{planId}/{branch}/logs (GET)`
   - `/plans/{planId}/branches (GET, POST)`
   - `/plans/{planId}/branches/{branch} (DELETE)`
   - `/plans/{planId}/{branch}/settings (GET, PUT)`
   - `/plans/{planId}/{branch}/status (GET)`
