package me.mtk.torrey.frontend.ir.gen;

import me.mtk.torrey.frontend.ast.ASTNodeVisitor;

public interface IRGenerator extends ASTNodeVisitor
{
  IRProgram gen();
}
