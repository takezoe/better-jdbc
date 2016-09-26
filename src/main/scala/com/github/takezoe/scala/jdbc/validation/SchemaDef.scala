package com.github.takezoe.scala.jdbc.validation

import better.files.File
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule

case class SchemaDef(tables: Seq[TableDef], conncetionDef: Option[ConnectionDef]){
  def toMap: Map[String, TableDef] = {
    tables.map { t => t.name -> t }.toMap
  }
}

case class ConnectionDef(url: String, user: String, password: String)

case class TableDef(name:String, columns: Seq[ColumnDef])

case class ColumnDef(name: String)

object SchemaDef {

  private val mapper = new ObjectMapper()
  mapper.enable(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  mapper.registerModule(DefaultScalaModule)

  def load(): Option[SchemaDef] = {
    val file = File("schema.json")
    if(file.exists){
      val json = file.contentAsString
      Some(mapper.readValue(json, classOf[SchemaDef]))
    } else {
      None
    }
  }

//  def load(): Map[String, TableDef] = {
//    val file = File("schema.json")
//    val schema: Map[String, TableDef] = if(file.exists){
//      val json = file.contentAsString
//      val schema = mapper.readValue(json, classOf[SchemaDef])
//      schema.tables.map { t => t.name -> t }.toMap
//    } else {
//      Map.empty
//    }
//    schema
//  }

}