
![FHNW](assets/FHNW.png)

# Pi4J Applikationen mit JavaFX-basiertem GUI

[![Contributors](https://img.shields.io/github/contributors/DieterHolz/RaspPiFX-Template-Project)](https://github.com/DieterHolz/RaspPiFX-Template-Project/graphs/contributors)
[![License](https://img.shields.io/github/license/DieterHolz/RaspPiFX-Template-Project)](https://github.com/DieterHolz/RaspPiFX-Template-Project/blob/master/LICENSE)


Dieses Template Projekt wird für die Programmierausbildung in den IP12-Projekten an der [Fachhochschule Nordwestschweiz](https://www.fhnw.ch/en/degree-programmes/engineering/icompetence) (FHNW) eingesetzt.

Es enthält Beschreibungen und Beispiele wie ein [JavaFX](https://openjfx.io)-basiertes Graphical-User-Interface (GUI) mit mittels der [Pi4J-Library](https://www.pi4j.com) an den Raspberry Pi angeschlossenen Aktoren und Sensoren kombiniert werden können.

Insbesondere sind Template-Projekte enthalten, die als Startpunkt für eigene Projekte dienen.


## Setup von Raspberry Pi und Entwickler-Laptop

Bitte stellen Sie sicher, dass ihr Laptop und der von Ihnen verwendete Raspberry Pi 5 wie im [Hello Pi5 Projekt](https://gitlab.fhnw.ch/ip_12_preparation/hellopi5.git) beschrieben vorbereitet ist.


## Entwicklungsprozess

Unser Entwicklungsprozess für die IP12-Projekte ist ebenfalls im Projekt [Hello Pi5 Projekt](https://gitlab.fhnw.ch/ip_12_preparation/hellopi5.git) beschrieben.

Lesen Sie insbesondere die Kapitel _Entwicklungsprozess_ und _Applikation im Debugger starten_.


## Die enthaltenen Beispiel-Programme

#### HelloFX
Dient ausschliesslich der Überprüfung der [JavaFX](https://openjfx.io)-Basis-Installation. Auf keinen Fall als Vorlage für die eigenen [JavaFX](https://openjfx.io)-Applikationen verwenden.

Zum Starten:
- `launcher.class` im `pom.xml` auswählen
  - `<launcher.class>com.pi4j.mvc/com.pi4j.setup.HelloFX</launcher.class>`
- mit `Run local` auf dem Laptop starten
- mit `Run on Pi` auf dem RaspPi starten

Sobald der JavaFX-Setup überprüft ist, kann HelloFX gelöscht werden.

#### Wiring
Die anderen Beispielprogramme verwenden eine LED und einen Button. Diese müssen folgendermassen verdrahtet werden:

![Wiring](assets/wiring_bb.png)


#### TemplateApp

`TemplateApp` zeigt das Zusammenspiel eines [JavaFX](https://openjfx.io)-basierten Graphical-User-Interfaces (GUI) mit an den RaspPi angeschlossenen Sensoren und Aktuatoren, dem Physical-User-Interface (PUI).

Es dient als Vorlage für Ihre eigene Applikation. Das umfasst auch die enthaltenen TestCases.

Sie sollten zunächst das Beispiel kennenlernen und verstehen. Für Ihre eigene Applikation sollten Sie anschliessend die `TemplateApp` kopieren und entsprechend abändern, ohne dabei die Grundregeln des MVC-Konzepts zu verletzen (s.u.). 

Zum Starten:
- `launcher.class` im `pom.xml` auswählen
  - `<launcher.class>com.pi4j.mvc/com.pi4j.mvc.templateapp.AppStarter</launcher.class>`
- mit `Run local` (oder direkt aus der IDE heraus) auf dem Laptop starten. Sinnvoll für die GUI-Entwicklung. Das PUI steht auf dem Laptop nicht zur Verfügung. Das GUI kann jedoch weitgehend ohne Einsatz des RaspPis entwickelt werden 
    - in `AppStarter` kann zusätzlich noch ein rudimentärer PuiEmulator gestartet werden, so dass das Zusammenspiel zwischen GUI und PUI auch auf dem Laptop überprüft werden kann.
- mit `Run on Pi` auf dem RaspPi starten (jetzt natürlich inklusive "echtem" PUI)


#### TemplatePUIApp

Das MVC-Konzept sollte auch für Applikationen ohne GUI verwendet werden.

Falls Sie eine reine PUI-Applikation entwickeln oder erst später ein GUI hinzufügen wollen, sollten Sie die `TemplatePUIApp` als Vorlage nehmen.

Zum Starten:
- `launcher.class` im `pom.xml` auswählen
  - `<launcher.class>com.pi4j.mvc/com.pi4j.mvc.templatepuiapp.AppStarter</launcher.class>`
- `Run local` ist bei reinen PUI-Applikationen nicht sinnvoll
- mit `Run on Pi` auf dem RaspPi starten


## Das MVC-Konzept

Beim klassischen Model-View-Controller-Konzept sind neben der Starter-Klasse mindestens 3 Klassen beteiligt. Das Zusammenspiel dieser Klassen ist klar geregelt:

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

- _GUI Klasse._ Das Graphical-User-Interface. [JavaFX](https://openjfx.io)-basierte Implementierung des auf dem Bildschirm angezeigten UIs.
- _PUI Klasse._ Das Physical-User-Interface. Pi4J-basierte Implementierung der Sensoren und Aktuatoren. Verwendet Component-Klassen, wie Sie sie aus dem sogenannten [Pi4J Component Catalogue](https://github.com/Pi4J/pi4j-example-components.git) kennen.

GUI und PUI sind komplett voneinander getrennt, z.B. hat der GUI-Button zum Anschalten der LED keinen direkten Zugriff auf die LED-Component des PUIs. Stattdessen triggert der GUI-Button lediglich eine entsprechende Action im Controller, der wiederum die on-Property im Model auf den neuen Wert setzt. In einem separaten Schritt reagiert die LED-Component des PUIs auf diese Wertänderung und schaltet die LED an bzw. aus.

GUI und PUI arbeiten mit dem identischen Controller und damit auch mit dem identischen Model. 

Es ist wichtig, dass Sie dieses Konzept verstehen und für Ihr Projekt anwenden können. Gehen Sie bei Fragen auf die Fachcoaches oder OOP-Dozierenden zu.

Jede Benutzer-Interaktion durchläuft im MVC-Konzept den immer gleichen Kreislauf:

![MVC Concept](assets/mvc-interaction.png)

#### Projector Pattern
Unsere View-Klassen, also GUI und PUI, setzen das von Prof. Dierk König veröffentlichte [Projector Pattern](https://dierk.github.io/Home/projectorPattern/ProjectorPattern.html) um. 

Die grundlegenden Aufgaben von GUI und PUI sind gleich. Auf Code-Ebene ist dies erkennbar:
sie implementieren das gemeinsames Interface `Projector`, können also auf die gleiche Weise verwendet werden.

Weitere Konsequenzen
- Es können weitere UIs hinzugefügt werden, ohne dass es Code-Änderungen bei den bestehenden Klassen (ausser der Starter-Klasse) nach sich zieht.
  - Ein Beispiel dafür ist der `PuiEmulator`, der bei Bedarf zusätzlich gestartet werden kann.
- Diese Architektur ist auch geeignet für 
  - reine GUI-Applikationen und
  - reine PUI-Applikationen (siehe `TemplatePUIApp`).


### Implementierung des MVC-Konzepts

Die Basis-Klassen, die für die Implementierung des MVC-Konzepts notwendig sind, sind im Package `com.pi4j.mvc.util.mvcbase`. Die Klassen sind im Code ausführlich dokumentiert. 

## MultiControllerApp
Ein etwas fortgeschritteneres Beispiel ist die `MultiControllerApp`. Sie zeigt den Einsatz und die Notwendigkeit von mehreren Controllern in einer Applikation.

Für einen einzelnen Controller gilt:
- jede Action wird asynchron und reihenfolgetreu ausgeführt 
- dafür hat jeder Controller eine eigene `ConcurrentTaskQueue` integriert
- das UI wird dadurch während der Ausführung einer Action _nicht_ blockiert
- werden vom UI weitere Actions getriggert während eine Action gerade in Bearbeitung ist, werden diese in der `ConcurrentTaskQueue` aufgesammelt und ausgeführt, sobald die vorherigen Actions abgearbeitet sind.

Für einfache Applikationen reicht ein einzelner Controller meist aus.

Es gibt aber Situationen, bei denen Actions ausgeführt werden sollen, während eine andere Action noch läuft.

Die `MultiControllerApp` zeigt so ein Beispiel. Es soll möglich sein, den Counter zu verändern _während die LED blinkt_ . 
- Mit einem einzigen Controller ist das nicht umsetzbar. Der Controller würde beispielsweise die 'Decrease-Action' erst ausführen, nachdem die 'Blink-Action' abgeschlossen ist.
- Bei zwei Controllern ist es jedoch einfach: `LedController` und `CounterController` haben jeder eine `ConcurrentTaskQueue`. Actions, die die LED betreffen, werden also unabhängig von den Actions, die den Counter verändern, ausgeführt.
- Es sollte zusätzlich ein `ApplicationController` implementiert werden, der die anderen Controller koordiniert und das für das UI sichtbare API zur Verfügung stellt.

Zum Starten:
- `launcher.class` im `pom.xml` auswählen
  - `<launcher.class>com.pi4j.mvc/com.pi4j.mvc.multicontrollerapp.AppStarter</launcher.class>`
- mit `Run local` (oder direkt aus der IDE heraus) auf dem Laptop starten.  
    - in `AppStarter` kann zusätzlich noch ein rudimentärer PuiEmulator gestartet werden, so dass das Zusammenspiel zwischen GUI und PUI auch auf dem Laptop überprüft werden kann.
- mit `Run on Pi` auf dem RaspPi starten


## JUnit Tests

Durch die klare Trennung in Model, View und Controller können grosse Teile der Applikation mittels einfachen JUnit-Tests automatisiert getestet werden. Diese Tests werden in der Regel auf dem Laptop, also nicht auf dem RaspPi, ausgeführt.

#### Controller Tests

Der Controller implementiert die gesamte zur Verfügung stehende Grund-Funktionalität. Er sollte mit ausführlichen TestCases automatisch überprüft werden.

Dabei gilt es zu beachten, dass der Controller alle Veränderungen auf dem Model asynchron ausführt. Eine Überprüfung der Resultate ist also erst möglich, wenn die asynchrone Task beendet ist.

Ein Beispiel sehen Sie in `SomeControllerTest`.

#### Presentation-Model Tests

Das Model ist lediglich eine Ansammlung von `ObservableValues` und bietet darüber hinaus keine weitere Funktionalität. Daher sind normalerweise auch keine weiteren TestCases notwendig.

#### Tests für einzelne PUI-Components

Die einzelnen PUI-Components können sehr gut via der in Pi4J integrierten `MockPlatform` getestet werden. Diese Tests werden auf dem Laptop ausgeführt. Ein RaspPi ist nicht notwendig.

Beispiele für solche Component-Tests sehen Sie im [CrowPi-Tutorial](https://fhnw-ip5-ip6.github.io/CrowPiGoesJavaTutorial/de/) und in diesem Projekt im Package `com.pi4j.mvc.templateapp.view.pui.components`.

#### PUI Tests
Das PUI ihrer Applikation kann ebenfalls gut mittels JUnit getestet werden.

Auch hier müssen die Tests berücksichtigen, dass die Actions asynchron ausgeführt werden.

Ein Beispiel ist `SomePUITest`.


## LICENSE

This repository is licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
License. You may obtain a copy of the License at: http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and
limitations under the License.




