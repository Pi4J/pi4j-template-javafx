![FHNW](assets/FHNW.png)

[Deutsche Beschreibung ist hier.](README_DE.md)

# Pi4J Applications with JavaFX based GUI

[![Contributors](https://img.shields.io/github/contributors/Pi4J/pi4j-template-javafx)](https://github.com/Pi4J/pi4j-template-javafx/graphs/contributors)
[![License](https://img.shields.io/github/license/Pi4J/pi4j-template-javafx)](https://github.com/Pi4J/pi4j-template-javafx/blob/master/LICENSE)

This template project contains descriptions and example code how to combine a [JavaFX](https://openjfx.io)-based Graphical-User-Interface (GUI) with sensors and actuators that are attached to the Raspberry Pi by using the [Pi4J-Library](https://www.pi4j.com).

This repository should not be cloned directly. It is a template project and one should create their own project by using the `Use this template` Button.

## Prepare Raspberry Pi and Developer Laptop

Please make sure that your Raspberry Pi and your developer laptop are prepared as described in the [Hello Pi5 Project](https://gitlab.fhnw.ch/ip_12_preparation/hellopi5.git).

## Development process
The recommended development process is also described in the [Hello Pi5 Project](https://gitlab.fhnw.ch/ip_12_preparation/hellopi5.git).

Please read the chapters _Entwicklungsprozess_ and _Applikation im Debugger starten_.

## The example applications

#### HelloFX

Used only to test if the [JavaFX](https://openjfx.io) libraries are installed correctly. Should not be used as a template for one's own JavaFX applications.

To start:

- Set `launcher.class` in `pom.xml`:
  - `<launcher.class>com.pi4j.setup.HelloFX</launcher.class>`
- With `Run Local` starts locally on the developer computer
- With `Run on Pi` starts remotely on the Raspberry Pi

Once the JavaFX setup has been tested, `HelloFX` can be deleted.

#### Wiring

The other example applications use a LED and a button. These must be wired as is shown in the following diagram:

![Wiring](assets/wiring_bb.png)


#### TemplateApp

This application shows the interaction between a [JavaFX](https://openjfx.io) based Graphical User Interface (GUI) and the Raspberry Pi connected sensors and actuators, the Physical User Interface (PUI).

This application is to be used as a template for one's own applications. This includes the existing test cases.

You should first get to know and understand the example. For your own applications, you should then copy the TemplateApp and modify it for your project, however, without violating the rules of the MVC concept, which is described below.

To start:

- Set `launcher.class` in `pom.xml`:
  - `<launcher.class>com.pi4j.mvc.templateapp.AppStarter</launcher.class>`
- With `Run Local` (or directly from the IDE) starts locally on the development computer. Useful for GUI development. The PUI is not available on the local computer. The GUI can largely be developed without the need for a Raspberry Pi.
  - in `AppStarter` a simple `PuiEmulator` can be started, so that the interaction between GUI and PUI can also be tested on the local development computer.
- With `Run on Pi` starts remotely on the Raspberry Pi (now including the PUI)

#### TemplatePUIApp

The MVC concept should also be used for applications without a GUI.

When developing PUI only applications, or when adding the GUI later, then one should use the `TemplatePUIApp` as template.

To start:

- Set `launcher.class` in `pom.xml`:
  - `<launcher.class>com.pi4j.mvc.templatepuiapp.AppStarter</launcher.class>`
- `Run Local` makes no sense for PUI only applications
- With `Run on Pi` starts remotely on the Raspberry Pi


## The MVC concept

The classic Model-View-Controller concept contains, in addition to the starter class, at least 3 more classes. The interaction is clearly defined:

![MVC Concept](assets/mvc-concept.png)

- _Model classes_

  - contain the complete state which is to be visualized, thus these classes are called _Presentation-Model_
  - are completely separate to the Controller and View classes, i.e., they may not interact with those classes
- _Controller classes_

  - define the entire functionality, i.e. the so-called actions, in the form of methods
  - manage the model classes by definition of the business logic
  - have no access to the view classes
- _View classes_

  - only calls methods on the controller, i.e. triggering actions
  - are notified of the model of state changes
    - observes the state of the model
  - never change the model directly
- _Starter class._

  - Is a subclass of `javafx.application.Application`. Instantiates the three other classes and starts the application.

In our case at least two view classes exist:

- _GUI class._ The Graphical-User-Interface. [JavaFX](https://openjfx.io) based implementation of the visualization of the UI on the screen.
- _PUI class._ The Physical-User-Interface. Pi4J based implementation of the sensors and actors. Uses `Component` classes, as is used in [Pi4J Example Components](https://github.com/Pi4J/pi4j-example-components.git).

GUI and PUI are completely separated from each other, i.e., a GUI button to turn an LED on has no direct access to the LED component of the PUI. Instead, the GUI button triggers a corresponding action in the controller which then sets the `on` state property in the model. The PUI listening on this state then turns the actual LED on or off.

GUI and PUI work with the same identical controller and thus also the same identical model.

It is important that one understands this concept and then apply the concepts to one's own project. Should you have questions, contact the Pi4j team.

In the MVC concept, every user interaction traverses the exact same cycle:

![MVC Concept](assets/mvc-interaction.png)

#### Projector Pattern

The view classes, i.e. GUI and PUI, implement the Projector-Pattern published by Prof. Dierk König [Projector Pattern](https://dierk.github.io/Home/projectorPattern/ProjectorPattern.html).

The basic tasks of the GUI and PUI are the same. When looking at the code, this is visible:
they implement the common interface `Projector` and can thus be used in the same way.

Consequences of this design:

- Additional UIs can be added, without having to change existing classes, except for the starter class
  - An example for this is the `PuiEmulator`, which can be started when necessary.
- This architecture is also useful for
  - GUI-only applications and
  - PUI-only applications (see `TemplatePUIApp`).

### Implementing the MVC concept

The base classes, required by the MVC concept, are in the package `com.pi4j.mvc.util.mvcbase`. The classes have extensive documentation.

## MultiControllerApp

A more advanced example is the `MultiControllerApp`. It shows the usage and relevancy of multiple controllers in an application.

For any controller, the following is imperative:

- each action is asynchronous and follows the sequence of actions explicitly
- for this each controller uses its own `ConcurrentTaskQueue`
- the UI is thus never blocked by an action
- if a UI triggers additional actions when an action is in execution, there this action is stored in the `ConcurrentTaskQueue` and executed after the current action has completed.

For simple applications, a single controller will suffice.

There are situations where actions are to be executed in parallel.

The `MultiControllerApp` shows such an example. It should be possible to change the counter, _while an LED is blinking_.

- With a single controller, this would not be accomplishable. The controller would execute the `Decrease-Action` only after the `Blink-Action` is complete.
- With two controllers this is simple: `LedController` and `CounterController` each have a `ConcurrentTaskQueue`. Actions which concern the LED are thus executed independent of actions which modify the counter.
- An `ApplicationController` is implemented to coordinate the other controllers, thus giving the UI only a singly visible API.

To start:

- Set `launcher.class` in `pom.xml`:
  - `<launcher.class>com.pi4j.mvc.multicontrollerapp.AppStarter</launcher.class>`
- With `Run Local` (or directly from the IDE) starts locally on the development computer
  - A rudimentary `PuiEmulator` can be started in `AppStarter`, to test the interaction of the GUI and PUI.
- With `Run on Pi` starts remotely on the Raspberry Pi

## JUnit Tests

Through the clear separation in model, view and controller, testing of large parts of the application can be automated. These tests are usually executed on the local development computer, i.e. not on the Raspberry Pi.

#### Controller Tests

The controller implements the entirety of the base functionality. It should be validated with extensive test cases.

It should be pointed out, that all changes to the model are performed asynchronously, thus validation can only be done after the asynchronous Tasks are completed.

An example can be seen in `SomeControllerTest`.

#### Presentation-Model Tests

The model is simply a collection of `ObservableValues` and doesn't offer any additional functionality, thus it does not require any additional test cases.

#### Tests for individual PUI-Components

The individual PUI-components can be tested easily using the Pi4J integrated `MockPlatform`. These tests can be executed locally on the development computer. A Raspberry Pi is not needed.


#### PUI Tests

The PUI can also be tested quite well with JUnit tests.

It should be pointed out that the actions are again executed asynchronously.

An example is the `SomePUITest`.

## LICENSE

This repository is licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
License. You may obtain a copy of the License at: http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and
limitations under the License.
