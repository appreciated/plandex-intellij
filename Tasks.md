Plandex Plugin für IntelliJ
Übersicht 📚

Das Plandex Plugin für IntelliJ bringt die Leistungsfähigkeit des zuverlässigen und entwicklerfreundlichen KI-Coding-Agenten direkt in deine Entwicklungsumgebung. Plandex ist speziell für den Einsatz in realen Szenarien entwickelt und eignet sich hervorragend für große, komplexe Aufgaben in bestehenden Codebasen.

Obwohl Plandex auf Robustheit und Zuverlässigkeit bei großen Aufgaben fokussiert ist, beschleunigt es auch neue Projekte und schnelle Einzelaufgaben.
Hauptfunktionen 🌟

    Strukturierte Aufgabenplanung: Plandex zerlegt große Aufgaben in kleinere Teilaufgaben und implementiert jede einzelne, bis das gesamte Vorhaben abgeschlossen ist. So kannst du ganze Features und Projekte mit einem strukturierten, versionskontrollierten und teamfreundlichen Ansatz der KI-gestützten Softwareentwicklung realisieren.

    Geschützte Sandbox: Änderungen werden in einer geschützten Sandbox gesammelt, sodass du sie vor der automatischen Anwendung auf deine Projektdateien überprüfen kannst. Dank der integrierten Versionskontrolle kannst du leicht zurückgehen und einen anderen Ansatz ausprobieren. Mit Branches kannst du mehrere Ansätze ausprobieren und die Ergebnisse vergleichen.

    Effizientes Kontextmanagement: Füge Dateien oder ganze Verzeichnisse einfach zum Kontext hinzu und halte sie automatisch aktuell, sodass Modelle immer den neuesten Stand deines Projekts haben.

    Modelvielfalt und Anpassungsfähigkeit: Plandex verwendet standardmäßig die OpenAI API und benötigt eine OPENAI_API_KEY-Umgebungsvariable. Es unterstützt auch eine Vielzahl anderer Modelle wie Anthropic Claude, Google Gemini, Mixtral, Llama und viele mehr über OpenRouter.ai, Together.ai oder andere OpenAI-kompatible Anbieter.

    Plattformübergreifend: Plandex unterstützt Mac, Linux, FreeBSD und Windows und läuft aus einem einzigen Binary ohne Abhängigkeiten.

Warum Plandex? 🤔

    Über Autocomplete hinaus: Erstelle komplexe Funktionalitäten mit KI.
    Weniger Mausnutzung: Vermeide das mauszentrierte Copy-Pasting bei der Arbeit mit ChatGPT.
    Effizientes Kontextmanagement: Halte die neuesten Versionen von Dateien im Kontext der KI-Modelle.
    Granulare Kontrolle: Behalte die Kontrolle darüber, was im Kontext ist und wie viele Tokens verwendet werden.
    Geschützte Experimente: Überarbeite und überprüfe in einer geschützten Sandbox, bevor du Änderungen anwendest.
    Einfaches Zurückspulen: Gehe zurück und versuche es erneut, wenn nötig.
    Branching: Erkunde mehrere Ansätze mit Branches.
    Parallele Aufgabenbearbeitung: Führe Aufgaben im Hintergrund aus oder arbeite an mehreren Aufgaben gleichzeitig.
    Modellvergleiche: Probiere verschiedene Modelle und Einstellungen aus und vergleiche die Ergebnisse.

Installation und Konfiguration

Um das Plandex Plugin für IntelliJ zu installieren, folge diesen Schritten:

    Öffne IntelliJ und gehe zu File > Settings > Plugins.
    Suche nach "Plandex" und klicke auf Install.
    Nach der Installation starte IntelliJ neu.
    Konfiguriere das Plugin unter File > Settings > Other Settings > Plandex und füge deine OPENAI_API_KEY-Umgebungsvariable hinzu.

Mit dem Plandex Plugin für IntelliJ kannst du die Kraft der KI direkt in deiner bevorzugten Entwicklungsumgebung nutzen und so die Effizienz und Qualität deiner Softwareprojekte erheblich steigern.

Hier ist ein detaillierter Plan, welche Methoden aus der Plandex API für ein hypothetisches IntelliJ-Plugin sinnvoll wären und wie sie integriert werden könnten:

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

### Modellverwaltung
Diese Methoden ermöglichen die Verwaltung und Nutzung verschiedener AI-Modelle innerhalb des Plugins.

1. **Modelle**
    - `/custom_models (GET, POST, DELETE)`
    - `/model_sets (GET, POST, DELETE)`

2. **Standardeinstellungen**
    - `/default_settings (GET, PUT)`

### Sonstige
1. **Serverstatus**
    - `/health (GET)`
    - `/version (GET)`

### Implementierungsplan

1. **Authentifizierung und Kontoverwaltung**
    - Implementieren Sie ein Authentifizierungsfenster innerhalb des Plugins, um Benutzeranmeldung und -abmeldung zu verwalten.
    - Bieten Sie Optionen zum Starten einer Testversion und zum Erstellen eines neuen Kontos an.
    - Implementieren Sie die E-Mail-Verifizierungsfunktion.

2. **Organisationsverwaltung**
    - Erstellen Sie ein Organisationsverwaltungsfenster, das es Benutzern ermöglicht, Organisationen zu erstellen, Benutzer zu verwalten und Rollen anzuzeigen.
    - Implementieren Sie die Einladungsfunktionen, um Benutzer zur Organisation einzuladen und ausstehende sowie angenommene Einladungen anzuzeigen.

3. **Projektmanagement**
    - Integrieren Sie ein Projektverwaltungsfenster, das es Benutzern ermöglicht, Projekte zu erstellen, umzubenennen und Pläne festzulegen.
    - Implementieren Sie die Möglichkeit, den aktuellen Branch eines Plans abzurufen.

4. **Plan- und Kontextverwaltung**
    - Erstellen Sie ein Planverwaltungsfenster, das es Benutzern ermöglicht, Pläne zu erstellen, zu archivieren, umzubenennen und zu löschen.
    - Integrieren Sie Funktionen zur Verwaltung des Kontextes eines Plans, einschließlich der Anzeige und Bearbeitung von Kontextdateien und -verzeichnissen.
    - Implementieren Sie Funktionen zum Anwenden, Ablehnen und Zurückspulen von Änderungen.

5. **Modellverwaltung**
    - Integrieren Sie ein Modellverwaltungsfenster, das es Benutzern ermöglicht, benutzerdefinierte Modelle und Modellpakete zu erstellen und zu löschen.
    - Implementieren Sie die Möglichkeit, Standardeinstellungen für Modelle abzurufen und zu aktualisieren.

6. **Sonstige**
    - Implementieren Sie eine Statusanzeige für den Serverzustand und die Version des Plandex-Backends.

### UI-Elemente

Durch die Implementierung dieser Methoden und Funktionen können Sie sicherstellen, dass das IntelliJ-Plugin die Stärken von Plandex optimal nutzt und gleichzeitig eine nahtlose Integration in die Entwicklungsumgebung bietet.

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

Durch diese Anpassungen wird die Plan- und Kontextverwaltung effizienter gestaltet und der Dialog "Änderungen überprüfen und zurückspulen" bietet eine zentrale Stelle zur Überprüfung und Rücknahme von Änderungen.