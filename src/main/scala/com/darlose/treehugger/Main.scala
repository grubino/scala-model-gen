package com.darlose.treehugger

import java.io.File

import com.darlose.treehugger.model.{ModelClass, ModelField, ModelType}
import treehugger.forest._
import definitions._
import io.swagger.models.{Model, Swagger}
import io.swagger.parser.SwaggerParser
import treehuggerDSL._

import collection.JavaConverters._
import scala.io.Source
/**
  * Created by greg.rubino on 10/26/16.
  */
object Main extends App {

  def newModel(modelName: String, fields: Seq[(String, Symbol)]): ModelType =
    ModelClass(modelName, fields.map((ModelField.apply _).tupled))

  val swaggerPath =
    if (args.length > 0) args(0)
    else "https://raw.githubusercontent.com/swagger-api/swagger-parser/master/modules/swagger-parser/src/test/resources/thing.json"
  val swagger: Swagger = new SwaggerParser().read(swaggerPath)

  object SymbolTable {
    lazy val symbols = {
      swagger.getDefinitions.asScala.map {
        case (name, model) => (name, RootClass.newClass(name))
      }
    }
  }

  val scalaModels = swagger.getDefinitions.asScala.map {
    case (name, model) =>
      newModel(name, model.getProperties.asScala.map {
      case (fieldName, field) =>
        field.getType match {
          case symbol if SymbolTable.symbols.contains(symbol) => (fieldName, SymbolTable.symbols(symbol))
          case "integer" => (fieldName, IntClass)
          case "number" => (fieldName, DoubleClass)
          case "string" => (fieldName, StringClass)
          case "boolean" => (fieldName, BooleanClass)
          case _ => (fieldName, ObjectClass)
      }
    }.toSeq)
  }
  val compilationUnit = BLOCK(scalaModels.map(_.tree)) inPackage("com.darlose.model")

  println(treeToString(compilationUnit))

}
