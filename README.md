# Programmieren-Testat

Ein kleines 2D-Spiel mit **LibGDX**, entwickelt im Rahmen des Programmieren‑Testats.  
Der Spieler sammelt Münzen, weicht Gegnern aus und steigt in höhere Level auf.  
Bei einer Kollision mit einem Gegner endet das Spiel mit **Game Over** – ein Restart‑Button erlaubt den Neustart.

---

## 🎮 Features
- Steuerung mit **Pfeiltasten** oder **A/D**
- Münzen sammeln → Punkte erhöhen
- Levelsystem:
  - Level 1: langsame Gegner, weniger Spawns
  - Level 2: schnellere Gegner, häufigere Spawns
- Anzeige von **Punkten** (oben links) und **Level** (oben rechts)
- **Game Over** bei Gegnerkontakt
- **Restart‑Button** zum Neustart

---

## 🛠️ Projektstruktur

core/ ├─ src/main/java/de/hsbi/programmieren/ │ ├─ Main.java        # Einstiegspunkt, Render-Loop │   
                                                ├─ Player.java      # Spieler-Logik │   
                                                ├─ Coins.java       # Münzen-Logik │   
                                                ├─ Enemy.java       # Gegner-Logik (inkl. Kollisionen) │   
assets/ui/ ├─ uiskin.json      # Skin-Definition für UI ├─ uiskin.atlas ├─ default.fnt ├─ default.png

---

## ▶️ Ausführen
1. **LibGDX herunterladen und installieren**  
   Lade dir das Framework von der offiziellen Seite herunter:  
   👉 [https://libgdx.com](https://libgdx.com)

   Dort findest du den **Setup-Tool** und die Dokumentation, um ein LibGDX‑Projekt einzurichten.

2. Stelle sicher, dass **Java 21+** installiert ist.
  - ACHRUNG!!! **Java 25** funktioniert stand jetzt (12.12.2025) noch nicht mit libGDX.

3. Projekt mit Gradle bauen:
   ```bash
   ./gradlew build


- Desktop-Version starten:
./gradlew lwjgl3:run



🎨 Eigene Skins
- Alle UI‑Elemente (Buttons, Labels etc.) nutzen ein Skin (uiskin.json).
- Du kannst eigene Skins erstellen, indem du eine neue JSON‑Datei mit eigenen Fonts und Texturen in assets/ ablegst:
Skin skin = new Skin(Gdx.files.internal("myskin.json"));



📚 Abhängigkeiten
- LibGDX Framework
- Gradle Buildsystem
- Standard‑Skin (uiskin.json) aus dem LibGDX‑Skin‑Repository

🚀 Nächsten eventuelle Schritte
- Mehr Level hinzufügen
- Soundeffekte für Münzen und Gegner
- Highscore‑System
- Eigene Grafiken für Spieler, Münzen und Gegner

👨‍💻 Autor
Projekt von Manuel Borghardt, Darnell Borghardt, Joel Jantschik und Leonid Nikkel