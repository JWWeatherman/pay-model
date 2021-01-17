# pay model

![CI](https://github.com/JWWeatherman/pay-model/workflows/Scala%20CI/badge.svg)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)

shared models and services for bitcoin and lightning

### Usage

To include this as a sub-project

1. add the project in build.sbt:

  ```
  lazy val paymodel = RootProject(uri(s"https://github.com/JWWeatherman/pay-model.git"))
  
  // tip: to add specify a commit or a tag append to the url
  lazy val payModelTag = "9ba3ae817789448f67df140ea9663136d90a6dee"
  lazy val paymodel = RootProject(uri(s"https://github.com/JWWeatherman/pay-model.git#$payModelTag"))
  ```
  
  and then aggregate in your root project
  ```
  lazy val root = (project in file("."))
      .aggregate(paymodel)
      .dependsOn(paymodel)
  ```
