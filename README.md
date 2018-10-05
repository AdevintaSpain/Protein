<!--
  Title: Protein - Kotlin code generator for Retrofit2 and RxJava2 based on Swagger
  Description: Protein is a plugin for intelliJ that automates the build of "Agents" for Android in Kotlin. It generates the code for Retrofit based on a Swagger endpoint.
  Author: SchibstedSpain
  -->

<img src="media/protein_logo.png" align="left" height="128px" />
<img align="left" width="0" height="128px" hspace="10" />

<div style="display:block; height: 168px;">
* <i>Kotlin code generator for Retrofit2 and RxJava2 based on Swagger</i> *

[![Version](https://img.shields.io/jetbrains/plugin/v/10206.svg)](https://plugins.jetbrains.com/plugin/10206-protein--android-component-builder) [![Downloads](https://img.shields.io/jetbrains/plugin/d/10206.svg)](https://plugins.jetbrains.com/plugin/10206-protein--android-component-builder) [ ![Protein Trending](http://starveller.sigsev.io/api/repos/SchibstedSpain/Protein/badge) ](http://starveller.sigsev.io/SchibstedSpain/Protein)

Protein is a plugin for intelliJ that automates the build of "Agents" for Android in Kotlin. It generates the code for Retrofit based on a Swagger endpoint.

<br/>
<p align="center">
<b><a href="#background">Background</a></b>
|
<b><a href="#features">Features</a></b>
|
<b><a href="#download">Download</a></b>
|
<b><a href="#who-made-this">Who Made This</a></b>
|
<b><a href="#apps-using-protein">Apps using Protein</a></b>
|
<b><a href="#contribute">Contribute</a></b>
|
<b><a href="#bugs-and-feedback">Bugs and Feedback</a></b>
|
<b><a href="#license">License</a></b>
</p>
<br/>

### Background

> *Protein:* A large molecule composed of one or more chains of amino acids in a specific order; the order is determined by the base sequence of nucleotides in the gene coding for the protein. Proteins are required for the structure, function, and regulation of the body’s cells, tissues, and organs, and each protein has unique functions. Examples are hormones, enzymes, and antibodies.


This scientific definition defines quite good the objective of this Plugin in an abstract way.
For many time at Schibsted Spain we struggled with the idea of making a good and maintainable architecture for Android.
It's been some time until we got one that fits with our needs but we still have a lot of errors and issues that human beings do every time we make manual work.
That's were we came to the idea of automatization and make new components with all we need including tests, data sources even use cases and Presenters.

Protein is our approach to work better, be more productive and reliable.

### Features

* Easy interface
* Agent, Mapper, Model and DataSource auto-generated
* Swagger integration: create Data Sources based on swagger documentation
* IntelliJ and Android Studio compatible

### Download

#### RELEASE

You can download it through the intelliJ/AndroidStudio plugin interface
or you can check: [https://plugins.jetbrains.com/plugin/10206-protein--android-component-builder](https://plugins.jetbrains.com/plugin/10206-protein--android-component-builder)

#### SNAPSHOT

Alternatively, you can download a SNAPSHOT version with the latest features being developed.
[https://bintray.com/schibstedspain/maven/protein#files/com/schibstedspain/protein](https://bintray.com/schibstedspain/maven/protein#files/com/schibstedspain/protein)

### Build it yourself!

1. If you want to build it locally you need download the latest version of IntelliJ Community
[https://www.jetbrains.com/idea/download/#section=mac](https://www.jetbrains.com/idea/download/#section=mac)
2. Clone this repository (git@github.com:SchibstedSpain/protein.git)
3. Execute "RunIdea" gradle task

Who made this
--------------

| <a href="https://github.com/ferranpons"><img src="https://avatars2.githubusercontent.com/u/1225463?v=3&s=460" alt="Ferran Pons" align="left" height="100" width="100" /></a>
|---
| [Ferran Pons](https://github.com/ferranpons)


Apps using Protein
------------------

The following is a list of some of the public apps using Protein and are published on the Google Play Store.

Want to add your app? Found an app that no longer works or no longer uses Protein? Please submit a pull request on GitHub to update this page!

| <a href="https://play.google.com/store/apps/details?id=com.anuntis.segundamano"><img src="media/vibbo_logo.png" align="left" width="68px" height="68px"/></a> | <a href="https://play.google.com/store/apps/details?id=com.anuntis.fotocasa"><img src="media/fotocasa_logo.png" align="left" width="68px" height="68px"/></a>
|---|---
| [vibbo](https://play.google.com/store/apps/details?id=com.anuntis.segundamano) | [Fotocasa](https://play.google.com/store/apps/details?id=com.anuntis.fotocasa)


Contribute
----------

1. Create an issue to discuss about your idea
2. [Fork it] (https://github.com/SchibstedSpain/protein/fork)
3. Create your feature branch (`git checkout -b my-new-feature`)
4. Commit your changes (`git commit -am 'Add some feature'`)
5. Push to the branch (`git push origin my-new-feature`)
6. Create a new Pull Request
7. Profit! :white_check_mark:


Bugs and Feedback
-----------------

For bugs, questions and discussions please use the [Github Issues](https://github.com/SchibstedSpain/protein/issues).


License
-------

Copyright 2018 Schibsted Classified Media Spain S.L.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
