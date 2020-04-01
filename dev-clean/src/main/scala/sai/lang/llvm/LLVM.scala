package sai.lang.llvm

import org.antlr.v4.runtime._
import scala.collection.JavaConverters._

package IR {
  abstract class LAST

  case class Module(es: List[TopLevelEntity]) extends LAST

  abstract class TopLevelEntityList extends LAST {
    def toList: List[TopLevelEntity]
  }
  case class Cons(e: TopLevelEntity, es: TopLevelEntityList) extends TopLevelEntityList {
    def toList: List[TopLevelEntity] = { es.toList ++ e.toList }
  }

  abstract class TopLevelEntity extends TopLevelEntityList {
    def toList: List[TopLevelEntity] = List(this)
  }
  case object DummyEntity extends TopLevelEntity
  case class SourceFilename(name: String) extends TopLevelEntity
  case class TargetDefinition(ty: String, value: String) extends TopLevelEntity
  case class ModuleAsm(ctx: LLVMParser.ModuleAsmContext) extends TopLevelEntity
  case class TypeDef(ctx: LLVMParser.TypeDefContext) extends TopLevelEntity
  case class ComdatDef(ctx: LLVMParser.ComdatDefContext) extends TopLevelEntity
  case class GlobalDecl(ctx: LLVMParser.GlobalDeclContext) extends TopLevelEntity
  case class GlobalDef(ctx: LLVMParser.GlobalDefContext) extends TopLevelEntity
  case class IndirectSymbolDef(ctx: LLVMParser.IndirectSymbolDefContext) extends TopLevelEntity
  case class FunctionDecl(ctx: LLVMParser.FunctionDeclContext) extends TopLevelEntity
  case class FunctionDef(ctx: LLVMParser.FunctionDefContext) extends TopLevelEntity
  case class AttrGroupDef(ctx: LLVMParser.AttrGroupDefContext) extends TopLevelEntity
  case class NamedMetadataDef(ctx: LLVMParser.NamedMetadataDefContext) extends TopLevelEntity
  case class MetadataDef(ctx: LLVMParser.MetadataDefContext) extends TopLevelEntity
  case class UseListOrder(ctx: LLVMParser.UseListOrderContext) extends TopLevelEntity
  case class UseListOrderBB(ctx: LLVMParser.UseListOrderBBContext) extends TopLevelEntity
}

import IR._

class MyVisitor extends LLVMParserBaseVisitor[LAST] {
  override def visitModule(ctx: LLVMParser.ModuleContext): LAST = {
    val es = visit(ctx.topLevelEntities).asInstanceOf[TopLevelEntityList].toList
    Module(es)
  }
  
  override def visitTopLevelEntityList(ctx: LLVMParser.TopLevelEntityListContext): LAST = {
    if (ctx.topLevelEntityList == null) {
      visit(ctx.topLevelEntity)
    } else {
      val e = visit(ctx.topLevelEntity).asInstanceOf[TopLevelEntity]
      val es = visit(ctx.topLevelEntityList).asInstanceOf[TopLevelEntityList]
      Cons(e, es)
    }
  }
  
  override def visitSourceFilename(ctx: LLVMParser.SourceFilenameContext): LAST = {
    SourceFilename(ctx.stringLit.STRING_LIT.getText)
  }

  override def visitTargetDefinition(ctx: LLVMParser.TargetDefinitionContext): LAST = { 
    if (ctx.DATALAYOUT != null) {
      TargetDefinition(ctx.DATALAYOUT.getText, ctx.stringLit.STRING_LIT.getText) 
    } else if (ctx.TRIPLE != null) {
      TargetDefinition(ctx.TRIPLE.getText, ctx.stringLit.STRING_LIT.getText) 
    } else { ??? }
  }

  override def visitModuleAsm(ctx: LLVMParser.ModuleAsmContext): LAST = { ModuleAsm(ctx) }

  override def visitTypeDef(ctx: LLVMParser.TypeDefContext): LAST = { TypeDef(ctx) }

  override def visitComdatDef(ctx: LLVMParser.ComdatDefContext): LAST = { ComdatDef(ctx) }

  override def visitGlobalDecl(ctx: LLVMParser.GlobalDeclContext): LAST = { GlobalDecl(ctx) }

  override def visitGlobalDef(ctx: LLVMParser.GlobalDefContext): LAST =  { GlobalDef(ctx) }

  override def visitIndirectSymbolDef(ctx: LLVMParser.IndirectSymbolDefContext): LAST = { IndirectSymbolDef(ctx) }

  override def visitFunctionDecl(ctx: LLVMParser.FunctionDeclContext): LAST = { FunctionDecl(ctx) }

  override def visitFunctionDef(ctx: LLVMParser.FunctionDefContext): LAST = { FunctionDef(ctx) }

  override def visitAttrGroupDef(ctx: LLVMParser.AttrGroupDefContext): LAST = { AttrGroupDef(ctx) }

  override def visitNamedMetadataDef(ctx: LLVMParser.NamedMetadataDefContext): LAST = { NamedMetadataDef(ctx) }

  override def visitMetadataDef(ctx: LLVMParser.MetadataDefContext): LAST = { MetadataDef(ctx) }

  override def visitUseListOrder(ctx: LLVMParser.UseListOrderContext): LAST = { UseListOrder(ctx) }

  override def visitUseListOrderBB(ctx: LLVMParser.UseListOrderBBContext): LAST = { UseListOrderBB(ctx) }
}

object LLVMTest {
  def parse(input: String) = {
    val charStream = new ANTLRInputStream(input)
    val lexer = new LLVMLexer(charStream)
    val tokens = new CommonTokenStream(lexer)
    val parser = new LLVMParser(tokens)

    val visitor = new MyVisitor()
    val res = visitor.visit(parser.module())
    println(res)
  }

  def main(args: Array[String]): Unit = {
    //val testInput = scala.io.Source.fromFile("test.ll").mkString
    val testInput = scala.io.Source.fromFile("maze.ll").mkString
    println(testInput)
    parse(testInput)
  }
}
