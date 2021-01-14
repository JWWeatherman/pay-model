# pay model

[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)


models and services for bitcoin and lightning rpc


### Usage

To include this as a sub-project

1. add the project in build.sbt:

  ```
  // specify a commit or a tag
  lazy val payModelTag = "9ba3ae817789448f67df140ea9663136d90a6dee"

  lazy val paymodel = RootProject(uri(s"https://github.com/JWWeatherman/pay-model.git#$payModelTag"))

  ```
  latest builds for scala version:
  2.11 = 9ba3ae817789448f67df140ea9663136d90a6dee

  2.13 = a23cb864b873bb2d859dde6b0f3d476f5862490e
  
1. include the dependencies from `project/Dependencies.scala` into main project dependencies
