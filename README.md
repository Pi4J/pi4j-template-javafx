![FHNW](assets/FHNW.png)

# Pi4J Applikationen mit JavaFX-basiertem GUI

[![Contributors](https://img.shields.io/github/contributors/DieterHolz/RaspPiFX-Template-Project)](https://github.com/DieterHolz/RaspPiFX-Template-Project/graphs/contributors)
[![License](https://img.shields.io/github/license/DieterHolz/RaspPiFX-Template-Project)](https://github.com/DieterHolz/RaspPiFX-Template-Project/blob/master/LICENSE)

Dieses Template Projekt wird für die Programmierausbildung an der [Fachhochschule Nordwestschweiz](https://www.fhnw.ch/en/degree-programmes/engineering/icompetence) (FHNW) eingesetzt. 

Natürlich ist es auch für Programmierprojekte ausserhalb der FHNW geeignet. Einige Beschreibungen sind jedoch FHNW-spezifisch.


## Eigenes Repository anlegen
Sie sollten für Ihr Programmier-Projekt auf keinen Fall direkt auf diesem Repository arbeiten. Stattdessen sollten Sie ein eigenes Repository anlegen, entweder unter Ihrem FHNW GitLab- oder GitHub-Account und dieses Template-Respository als "Quelle" importieren.

#### FHNW GitLab

Auf Ihrem GitLab-Account via

`New Project -> Import Project -> Repo by URL`

das Formular ausfüllen, mit `Git repository URL` :

`https://github.com/DieterHolz/RaspPiFX-Template-Project.git`


#### GitHub

`Use this template`-Button dieses Repositories verwenden.

## Raspberry Pi vorbereiten
Verwenden Sie das vom [Pi4J-Team](https://github.com/Pi4J/pi4j-os) vorbereitete CrowPi-Image.
- [Download CrowPi Image](https://pi4j-download.com/latest.php?flavor=crowpi)
- zip-File auspacken, mit [Raspberry Pi Imager](https://www.raspberrypi.org/blog/raspberry-pi-imager-imaging-utility/) eine SD-Card bespielen und damit den Raspberry Pi starten

Das CrowPi-Image enthält bereits alle notwendige Installationen für JavaFX/Pi4J-Applikationen.


## Installationen auf dem Entwickler-Laptop

* [Java 17](https://adoptium.net/?variant=openjdk17&jvmVariant=hotspot). Im CrowPi-Image ist JDK17 installiert. Daher verwenden wir diesen JDK auch auf dem Laptop. Hinweis für Mac-Benutzer: Die Verwendung von SDKMAN (s.u.) für die Installation und die Verwaltung von JDKs ist sehr empfehlenswert.

* [IntelliJ IDEA 2021.2](https://www.jetbrains.com/idea/download/). Es ist wichtig, diese neueste Version zu verwenden. Am besten via [JetBrains Toolbox](https://www.jetbrains.com/toolbox-app/) installieren. Empfehlenswert ist die Verwendung der Ultimate Edition. Studierende erhalten, nach Anmeldung, eine kostenlose Lizenz. Registrieren Sie sich unter [https://www.jetbrains.com/student/](https://www.jetbrains.com/student/) mit Ihrer FHNW E-Mail-Adresse. Für die Community-Edition benötigt man keine Lizenz.

* [Git](https://git-scm.com/downloads). Als Sourcecode-Repository verwenden wir git. 

* [GitLab](https://gitlab.fhnw.ch/) oder [GitHub-Account](https://github.com/). Verwenden Sie ihren FHNW Gitlab oder GitHub Account. 

* `ssh`. Die Verbindung zum Raspberry Pi wird mit `ssh` hergestellt. Ist normalerweise auf allen Laptops vorinstalliert.

* [VNC Viewer](https://www.realvnc.com/en/connect/download/viewer/). Ermöglicht ein komfortables Arbeiten auf dem Raspberry Pi vom Laptop aus. Dadurch spart man sich das Anschliessen von Monitor, Tastatur und Maus direkt am Raspberry Pi.


## Empfehlung zur Installation des JDK für MacOS (und Linux)

Für MacOs und Linux gibt es ein sehr empfehlenswertes Tool zur Verwaltung unterschiedlicher Software Development Kits: [SDKMAN](https://sdkman.io)

Insbesondere wenn, wie üblich, mehrere Java JDKs verwendet werden sollen, hilft SDKMAN.

#### Installation von SDKMAN
Folgenden Befehl in einem Terminal eingeben:
 ```shell
 export SDKMAN_DIR="$HOME/sdkman" && curl -s "https://get.sdkman.io" | bash
 ```

Falls Sie SDKMAN bereits früher installiert haben, müssen Sie SDKMAN auf den neuesten Stand bringen:

```shell
sdk update
```

#### Installation des JDK 
In einem neuen Terminal-Window diesen Befehl eingeben:

```shell
sdk install java 17.0.1-tem
```

Danach liegt der JDK in ihrer Home-Directory im Folder `sdkman/candidates/java`. Von dort können Sie es dann in IntelliJ als neuen SDK anlegen und im Projekt verwenden.

Mit: 

```shell
sdk ls java
```

können Sie sich auflisten lassen welche anderen JDKs zur Installation zur Verfügung stehen.


## Verbindung zum Raspberry Pi herstellen
Der Laptop und der Raspberry Pi müssen das gleiche WLAN verwenden.

Eine einfache Variante dies sicherzustellen ist das Aufsetzen eines Hotspots auf einem Smartphone, idealerweise mit diesen Parametern:

- ssid: `Pi4J-Spot`
- password: `MayTheSourceBeWithYou!`

Auf diesen Hotspot connected sich der RaspPi mit dem CrowPi-Image automatisch und zeigt die IP-Nummer im Hintergrundbild an.

Den Laptop ebenfalls mit dem Pi4J-Spot verbinden.

#### Verbindung via SSH

In einem Terminal-Window des Laptops:

```shell
ssh pi@<ip.number>
Passwort: 'crowpi'
```

z.B.

```shell
ssh pi@192.168.183.86
Passwort: 'crowpi'
```

#### Verbindung via VNC

Mit derselben IP-Nummer kann auch via VNC auf den RaspPi zugegriffen werden. Man erhält auf dem Laptop ein Fenster, das den kompletten Desktop des Raspberry Pis anzeigt. 

Das Ganze sieht dann so aus (mit der gestarteten ExampleApp)

![VNC Viewer](assets/VNC_Viewer.png)


## Build System

Dieses Projekt verwendet Maven, um die Applikationen zu bauen und entweder lokal auf dem Laptop oder auf dem Raspberry Pi auszuführen.

Die Artefakte werden dabei auf dem Laptop gebaut, anschliessend auf den Raspberry Pi kopiert und dort gestartet. Die Entwicklung direkt auf dem Raspberry Pi ist zwar ebenfalls möglich, wird aber nicht empfohlen. Besser ist es, die Applikation auf dem Laptop zu entwickeln und sie auf dem Raspberry Pi lediglich ausführen zu lassen.

Dazu müssen nur wenige Konfigurationen verändert werden.

#### Einstellungen im `pom.xml`

- `launcher.class` **(required):** gibt an, welche Applikation gestartet werden soll. Im `pom.xml` ist bereits eine Liste von Kandidaten enthalten. Man muss nur bei der jeweils gewünschte Applikation die Kommentare entfernen.
- `pi.ipnumber` **(optional):** Die aktuelle IP-Nummer des Raspberry Pi, z.B. `192.168.1.2`, wird für SCP/SSH benötigt. 

Mit diesen Einstellungen kann die Applikation mittels Maven-Befehl auf dem Raspberry Pi gestartet werden. Besser ist es jedoch, die Run-Konfigurationen von IntelliJ zu verwenden.

#### Einstellungen in den Run-Konfigurationen

Im Projekt sind zwei Run-Konfigurationen vordefiniert:
- `Run Local` startet das Programm, das in `launcher.class` eingestellt wurde, auf dem Laptop. Wird vor allem während der GUI-Entwicklung gebraucht (also noch ohne die Verwendung von an den Raspberry Pi angeschlossenen Sensoren und Aktuatoren).
- `Run on Pi` startet das Programm auf dem Rasberry Pi.

In `Run on Pi` muss die IP-Adresse des RaspPi eingestellt werden. Dazu  `Edit Configurations` wählen. 

![Edit Configurations ...](assets/edit-configurations.png)

Im nun geöffneten Dialog den Tab `Runner` öffnen und `pi.ipnumber` doppelklicken. Danach öffnet sich das Dialogfenster zur Eingabe der IP-Adresse. 

![Einstellungen für Run Konfigurationen](assets/run-configurations.png)


## Die enthaltenen Beispiel-Programme

#### HelloFX
Dient ausschliesslich der Überprüfung der JavaFX-Basis-Installation. Auf keinen Fall als Vorlage für die eigenen JavaFX-Applikationen verwenden.

Zum Starten:
- `launcher.class` im `pom.xml` auswählen
  - `<launcher.class>com.pi4j.fxgl/com.pi4j.jfx.jfxplain.HelloFX</launcher.class>`
- mit `Run local` auf dem Laptop starten
- mit `Run on Pi` auf dem RaspPi starten


#### Wiring
Die beiden anderen Beispielprogramme verwenden eine LED und einen Button. Diese müssen folgendermassen verdrahtet werden:

![Wiring](assets/led-button_bb.png)


#### MinimalPi4J
Ist eine reine Pi4J-Applikation ohne GUI. Dient ebenfalls ausschliesslich der Überprüfung des Setups.

Zum Starten:
- `launcher.class` im `pom.xml` auswählen
  - `<launcher.class>com.pi4j.fxgl/com.pi4j.pi4jplain.MinimalPi4J</launcher.class> `
- `Run local` macht für dieses Beispiel keinen Sinn. An den Laptop sind weder Button noch LED angeschlossen
- mit `Run on Pi` auf dem RaspPi starten

Wenn der Button gedrückt wird, wird eine entsprechende Meldung auf dem Bildschirm ausgegeben.

#### ExampleApp

Zeigt das Zusammenspiel eines JavaFX-basiertes Graphical-User-Interfaces (GUI) mit an den RaspPi angeschlossenen Sensoren und Aktuatoren, dem Physical-User-Interface (PUI).

Es ist gleichzeitig ein konkretes Beispiel und eine Vorlage für Ihre eigene Applikation. Das umfasst auch die enthaltenen TestCases.

Sie sollten zunächst das Beispiel kennenlernen und verstehen. Für Ihre eigene Applikation sollten Sie anschliessend die ExampleApp kopieren und entsprechend abändern, ohne dabei die Grundregeln des MVC-Konzepts zu verletzen (s.u.). 

Zum Starten:
- `launcher.class` im `pom.xml` auswählen
  - `<launcher.class>com.pi4j.fxgl/com.pi4j.jfx.exampleapp.AppStarter</launcher.class>`
- mit `Run local` auf dem Laptop starten. Sinnvoll für die GUI-Entwicklung. Das PUI steht auf dem Laptop nicht zur Verfügung. Das GUI kann jedoch weitgehend ohne Einsatz des RaspPis entwickelt werden 
    - in `AppStarter` kann zusätzlich noch ein rudimentärer PuiEmulator gestartet werden, so dass das Zusammenspiel zwischen GUI und PUI auch auf dem Laptop überprüft werden kann.
- mit `Run on Pi` auf dem RaspPi starten (jetzt natürlich inklusive PUI)


## Das MVC-Konzept

Beim klassischen Model-View-Controller-Konzept sind, neben der Starter-Klasse, mindestens 3 Klassen beteiligt. Das Zusammenspiel dieser Klassen ist klar geregelt:

![MVC Concept](assets/mvc-concept.png)

- _Model Klassen_
  - enthalten den gesamten zu visualisierenden Zustand. Wir nennen diese Klassen daher _Presentation-Model_
  - sind komplett unabhängig von Controller und View
  
- _Controller Klassen_
  - stellen die gesamte Funktionalität, die sogenannten Actions, in Form von Methoden zur Verfügung
  - verwalten die Model-Klassen gemäss der zugrundeliegenden Business-Logik
  - haben keinen Zugriff auf die View-Klassen 
  
- _View Klassen_
  - rufen ausschliesslich Methoden auf dem Controller auf, sie "triggern Actions"
  - werden vom Model über Zustandsänderungen notifiziert
    - observieren den Status des Models 
  - ändern das Model nie direkt

- _Starter Klasse._ Ist eine Subklasse von `javafx.application.Application`. Instanziiert die drei anderen Klassen und startet die Applikation. 

In unserem Fall gibt es mindestens zwei View-Klassen

- _GUI Klasse._ Das Graphical-User-Interface. JavaFX-basierte Implementierung des auf dem Bildschirm angezeigten UIs.
- _PUI Klasse._ Das Physical-User-Interface. Pi4J-basierte Implementierung der Sensoren und Aktuatoren. Verwendet  Component-Klassen, wie Sie sie aus dem [CrowPi-Tutorial](https://fhnw-ip5-ip6.github.io/CrowPiGoesJavaTutorial/de/) kennen.

GUI und PUI sind komplett voneinander getrennt, z.B. hat der GUI-Button zum Anschalten der LED keinen direkten Zugriff auf die LED-Component des PUIs. Stattdessen triggert der GUI-Button lediglich eine entsprechende Action im Controller, der wiederum die on-Property im Model auf den neuen Wert setzt. In einem separaten Schritt reagiert die LED-Component des PUIs auf diese Wertänderung und schaltet die LED an- bzw. aus.

Es ist wichtig, dass Sie dieses Konzept verstehen und für Ihr Projekt anwenden können. Gehen Sie bei Fragen auf die Fachcoaches oder OOP-Dozierenden zu.

Jede Benutzer-Interaktion durchläuft im MVC-Konzept den immer gleichen Kreislauf:

![MVC Concept](assets/mvc-interaction.png)

#### Projector Pattern
Unsere View-Klassen, also GUI und PUI, setzen das von Dierk König veröffentlichte [Projector Pattern](https://jaxenter.de/effiziente-oberflaechen-mit-dem-projektor-pattern-42119) um. 

Die grundlegenden Aufgaben von GUI und PUI sind gleich. Auf Code-Ebene ist dies erkennbar:
sie implementieren das gemeinsames Interface `Projector`, können also auf die gleiche Weise verwendet werden.

Weitere Konsequenzen
- Es können weitere UIs hinzugefügt werden, ohne dass das Code-Änderungen bei den bestehenden Klassen (ausser der Starter-Klasse) nach sich zieht. Ein Beispiel dafür ist der `PuiEmulator`, der bei Bedarf zusätzlich gestartet werden kann.
- Diese Architektur ist auch geeignet für 
  - reine GUI-Applikationen und
  - reine PUI-Applikationen.


## Junit Tests

todo: fehlt noch

#### Für das Presentation-Model

todo: fehlt noch

#### Für die Components

todo: fehlt noch

#### Für das PUI
todo: fehlt noch


## LICENSE

This repository is licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
License. You may obtain a copy of the License at: http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and
limitations under the License.




