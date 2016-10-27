package com.darlose.treehugger.model

import treehugger.forest._, definitions._, treehuggerDSL._

/**
  * Created by greg.rubino on 10/26/16.
  */
trait ModelType {
  def tree: Tree
}

case class ModelField(fieldName: String, fieldType: Symbol) extends ModelType {
  def tree: Tree = PARAM(fieldName, fieldType)
}

case class ModelClass(className: String, fields: Seq[ModelType]) extends ModelType {
  def tree: Tree = CASECLASSDEF(className) withParams(fields.map {
    case ModelField(fName, fType) => PARAM(fName, fType): ValDef
  })
}
