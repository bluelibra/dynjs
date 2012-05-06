/**
 *  Copyright 2012 Douglas Campos, and individual contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dynjs.parser;

import me.qmx.jitescript.CodeBlock;
import org.antlr.runtime.tree.CommonTree;
import org.dynjs.compiler.DynJSCompiler;
import org.dynjs.exception.DynJSException;
import org.dynjs.parser.statement.*;
import org.dynjs.runtime.DynThreadContext;
import org.dynjs.runtime.RT;

import java.util.List;

import static me.qmx.jitescript.util.CodegenUtils.p;
import static me.qmx.jitescript.util.CodegenUtils.sig;

public class Executor {

    private final DynThreadContext context;

    public Executor(DynThreadContext context) {
        this.context = context;
    }

    public DynThreadContext getContext() {
        return context;
    }

    public List<Statement> program(final List<Statement> blockContent) {
        return blockContent;
    }

    public Statement block(final List<Statement> blockContent) {
        return new BlockStatement(blockContent);
    }

    public Statement printStatement(final Statement expr) {
        return new PrintStatement(expr);
    }

    public Statement returnStatement(final Statement expr) {
        return new ReturnStatement(expr);
    }

    public Statement declareVar(final CommonTree id) {
        return declareVar(id, new UndefinedValueStatement());
    }

    public Statement declareVar(final CommonTree id, final Statement expr) {
        return declareVar(id.getText(), expr);
    }

    public Statement declareVar(final String id, final Statement expr) {
        return new DeclareVarStatement(expr, id);
    }

    public Statement defineAddOp(final Statement l, final Statement r) {
        return defineNumOp("add", l, r);
    }

    public Statement defineSubOp(final Statement l, final Statement r) {
        return defineNumOp("sub", l, r);
    }

    public Statement defineMulOp(final Statement l, final Statement r) {
        return defineNumOp("mul", l, r);
    }

    public Statement defineNumOp(final String op, final Statement l, final Statement r) {
        return new DefineNumOpStatement(op, l, r);
    }

    public Statement defineStringLiteral(final String literal) {
        return new StringLiteralStatement(literal);
    }

    public Statement resolveIdentifier(final CommonTree id) {
        return new ResolveIdentifierStatement(id.getText());
    }

    public Statement defineNumberLiteral(final String value, final int radix) {
        return new NumberLiteralStatement(value, radix);
    }

    public Statement defineFunction(final String identifier, final List<String> args, final Statement block) {
        return new FunctionStatement(getContext(), identifier, args, block);
    }

    public Statement defineShlOp(Statement l, Statement r) {
        throw new DynJSException("not implemented yet");
    }

    public Statement defineShrOp(Statement l, Statement r) {
        throw new DynJSException("not implemented yet");
    }

    public Statement defineShuOp(Statement l, Statement r) {
        throw new DynJSException("not implemented yet");
    }

    public Statement defineDivOp(Statement l, Statement r) {
        return defineNumOp("div", l, r);
    }

    public Statement defineModOp(Statement l, Statement r) {
        return defineNumOp("mod", l, r);
    }

    public Statement defineDeleteOp(Statement expression) {
        throw new DynJSException("not implemented yet");
    }

    public Statement defineVoidOp(final Statement expression) {
        return new Statement() {
            @Override
            public CodeBlock getCodeBlock() {
                return new CodeBlock(){{
                    append(expression.getCodeBlock());
                    append(new UndefinedValueStatement().getCodeBlock());
                }};
            }
        };
    }

    public Statement defineTypeOfOp(final Statement expression) {
        return new Statement() {
            @Override
            public CodeBlock getCodeBlock() {
                return new CodeBlock() {{
                    append(expression.getCodeBlock());
                    invokedynamic("typeof", sig(String.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
                }};
            }
        };
    }

    public Statement defineIncOp(Statement expression) {
        return new PreIncrementStatement(expression);
    }

    public Statement defineDecOp(Statement expression) {
        return new PreDecrementStatement(expression);
    }

    public Statement definePosOp(Statement expression) {
        throw new DynJSException("not implemented yet");
    }

    public Statement defineNegOp(Statement expression) {
        throw new DynJSException("not implemented yet");
    }

    public Statement defineInvOp(Statement expression) {
        throw new DynJSException("not implemented yet");
    }

    public Statement defineNotOp(Statement expression) {
        return new NotOperationStatement(expression);
    }

    public Statement definePIncOp(Statement expression) {
        return new PostIncrementStatement(expression);
    }

    public Statement definePDecOp(Statement expression) {
        return new PostDecrementStatement(expression);
    }

    public Statement defineLtRelOp(Statement l, Statement r) {
        return new RelationalOperationStatement("lt", l, r);
    }

    public Statement defineGtRelOp(final Statement l, final Statement r) {
        return new RelationalOperationStatement("gt", l, r);
    }

    public Statement defineLteRelOp(Statement l, Statement r) {
        return new RelationalOperationStatement("le", l, r);
    }

    public Statement defineGteRelOp(Statement l, Statement r) {
        return new RelationalOperationStatement("ge", l, r);
    }

    public Statement defineInstanceOfRelOp(Statement l, Statement r) {
        throw new DynJSException("not implemented yet");
    }

    public Statement defineInRelOp(Statement l, Statement r) {
        throw new DynJSException("not implemented yet");
    }

    public Statement defineLorOp(final Statement l, final Statement r) {
        return new LogicalOperationStatement("lor", l, r);
    }

    public Statement defineLandOp(Statement l, Statement r) {
        return new LogicalOperationStatement("land", l, r);
    }

    public Statement defineAndBitOp(Statement l, Statement r) {
        throw new DynJSException("not implemented yet");
    }

    public Statement defineOrBitOp(Statement l, Statement r) {
        throw new DynJSException("not implemented yet");
    }

    public Statement defineXorBitOp(Statement l, Statement r) {
        throw new DynJSException("not implemented yet");
    }

    public Statement defineEqOp(final Statement l, final Statement r) {
        return new EqualsOperationStatement(l, r);
    }

    public Statement defineNEqOp(final Statement l, final Statement r) {
        return new NotEqualsOperationStatement(l, r);
    }

    public Statement defineSameOp(Statement l, Statement r) {
        return new EqualsOperationStatement(l, r);
    }

    public Statement defineNSameOp(Statement l, Statement r) {
        return new NotEqualsOperationStatement(l, r);
    }

    public Statement defineAssOp(final Statement l, final Statement r) {
        return new AssignmentOperationStatement(l, r);
    }

    public Statement defineMulAssOp(Statement l, Statement r) {
        return new OperationAssignmentStatement("mul", l, r);
    }

    public Statement defineDivAssOp(Statement l, Statement r) {
        return new OperationAssignmentStatement("div", l, r);
    }

    public Statement defineModAssOp(Statement l, Statement r) {
        return new OperationAssignmentStatement("mod", l, r);
    }

    public Statement defineAddAssOp(final Statement l, final Statement r) {
        return new OperationAssignmentStatement("add", l, r);
    }

    public Statement defineSubAssOp(Statement l, Statement r) {
        return new OperationAssignmentStatement("sub", l, r);
    }

    public Statement defineShlAssOp(Statement l, Statement r) {
        throw new DynJSException("not implemented yet");
    }

    public Statement defineShrAssOp(Statement l, Statement r) {
        throw new DynJSException("not implemented yet");
    }

    public Statement defineShuAssOp(Statement l, Statement r) {
        throw new DynJSException("not implemented yet");
    }

    public Statement defineAndAssOp(Statement l, Statement r) {
        throw new DynJSException("not implemented yet");
    }

    public Statement defineXorAssOp(Statement l, Statement r) {
        throw new DynJSException("not implemented yet");
    }

    public Statement defineOrAssOp(Statement l, Statement r) {
        throw new DynJSException("not implemented yet");
    }

    public Statement defineQueOp(Statement ex1, Statement ex2, Statement ex3) {
        return new IfStatement(context, ex1, ex2, ex3);
    }

    public Statement defineThisLiteral() {
        throw new DynJSException("not implemented yet");
    }

    public Statement defineNullLiteral() {
        return new NullLiteralStatement();
    }

    public Statement defineRegExLiteral(String s) {
        throw new DynJSException("not implemented yet");
    }

    public Statement defineTrueLiteral() {
        return new BooleanLiteralStatement("TRUE");
    }

    public Statement defineFalseLiteral() {
        return new BooleanLiteralStatement("FALSE");
    }

    public Statement executeNew(final Statement statement) {
        return new Statement() {
            @Override
            public CodeBlock getCodeBlock() {
                return CodeBlock.newCodeBlock()
                        .aload(DynJSCompiler.Arities.CONTEXT)
                        .append(statement.getCodeBlock())
                        .invokedynamic("new", sig(Object.class, DynThreadContext.class, Object.class), RT.BOOTSTRAP, RT.BOOTSTRAP_ARGS);
            }
        };
    }

    public Statement resolveByField(final Statement lhs, final String field) {
        return new ResolveByIndexStatement(lhs, field);
    }

    public Statement resolveByIndex(final Statement lhs, final Statement index) {
        return new ResolveByIndexStatement(lhs, index);
    }

    public Statement ifStatement(Statement vbool, Statement vthen, Statement velse) {
        return new IfStatement(getContext(), vbool, vthen, velse);
    }

    public Statement doStatement(Statement vbool, Statement vloop) {
        return new DoWhileStatement(vbool, vloop);
    }

    public Statement whileStatement(final Statement vbool, final Statement vloop) {
        return new WhileStatement(vbool, vloop);
    }

    public Statement forStepVar(Statement varDef, Statement expr1, Statement expr2, Statement statement) {
        return new ForStepVarStatement(varDef, expr1, expr2, statement);
    }

    public Statement forStepExpr(Statement expr1, Statement expr2, Statement expr3, Statement statement) {
        throw new DynJSException("not implemented yet");
    }

    public Statement forIterVar(Statement varDef, Statement expr1, Statement statement) {
        throw new DynJSException("not implemented yet");
    }

    public Statement forIterExpr(Statement expr1, Statement expr2, Statement statement) {
        throw new DynJSException("not implemented yet");
    }

    public Statement continueStatement(String id) {
        throw new DynJSException("not implemented yet");
    }

    public Statement breakStatement(String id) {
        throw new DynJSException("not implemented yet");
    }

    public Statement exprListStatement(final List<Statement> exprList) {
        return new Statement() {
            @Override
            public CodeBlock getCodeBlock() {
                return new CodeBlock() {{
                    for (Statement statement : exprList) {
                        append(statement.getCodeBlock());
                    }
                }};
            }
        };
    }

    public Statement resolveCallExpr(Statement lhs, List<Statement> args) {
        return new CallStatement(getContext(), lhs, args);
    }

    public Statement switchStatement(Statement expr, Statement _default, List<Statement> cases) {
        throw new DynJSException("not implemented yet");
    }

    public Statement switchCaseClause(Statement expr, List<Statement> statements) {
        throw new DynJSException("not implemented yet");
    }

    public Statement switchDefaultClause(List<Statement> statements) {
        throw new DynJSException("not implemented yet");
    }

    public Statement throwStatement(Statement expression) {
        return new Statement() {
            @Override
            public CodeBlock getCodeBlock() {
                return CodeBlock.newCodeBlock()
                        .newobj(p(RuntimeException.class))
                        .dup()
                        .invokespecial(p(RuntimeException.class), "<init>", sig(void.class))
                        .athrow();
            }
        };
    }

    public Statement tryStatement(Statement block, Statement _catch, Statement _finally) {
        throw new DynJSException("not implemented yet");
    }

    public Statement tryCatchClause(String id, Statement block) {
        throw new DynJSException("not implemented yet");
    }

    public Statement tryFinallyClause(Statement block) {
        throw new DynJSException("not implemented yet");
    }

    public Statement withStatement(Statement expression, Statement statement) {
        throw new DynJSException("not implemented yet");
    }

    public Statement labelledStatement(String label, Statement statement) {
        throw new DynJSException("not implemented yet");
    }

    public Statement objectValue(List<Statement> namedValues) {
        return new ObjectLiteralStatement(namedValues);
    }

    public Statement propertyNameId(String id) {
        return new StringLiteralStatement(id);
    }

    public Statement propertyNameString(String string) {
        return new StringLiteralStatement(string);
    }

    public Statement propertyNameNumeric(Statement numericLiteral) {
        throw new DynJSException("not implemented yet");
    }

    public Statement namedValue(final Statement propertyName, final Statement expr) {
        return new NamedValueStatement(propertyName, expr);
    }

    public Statement arrayLiteral(final List<Statement> exprs) {
        return new ArrayLiteralStatement(exprs);
    }

}
