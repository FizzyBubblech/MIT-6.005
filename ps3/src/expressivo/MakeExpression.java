package expressivo;

import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import expressivo.parser.ExpressionListener;
import expressivo.parser.ExpressionParser;

/** Make an Expression value from a parse tree. */
public class MakeExpression implements ExpressionListener {
    private Stack<Expression> stack = new Stack<>();
    
    // Invariant: stack contains the Expression value of each parse subtree 
    // that has been fully-walked so far, but whose parent has not yet been 
    // exited by the walk. The stack is ordered by recency of visit, so that
    // the top of the stack is the IntegerExpression for the most recently
    // walked subtree.
    //
    // At the start of the walk, the stack is empty, because no subtrees have
    // been fully walked.
    //
    // Whenever a node is exited by the walk, the Expression values of its
    // children are on top of the stack, in order with the last child on top. 
    // To preserve the invariant, we must pop those child Expression values 
    // from the stack, combine them with the appropriate Expression producer, 
    // and push back an Expression value representing the entire subtree under 
    // the node.
    //
    // At the end of the walk, after all subtrees have been walked and the the
    // root has been exited, only the entire tree satisfies the invariant's
    // "fully walked but parent not yet exited" property, so the top of the stack
    // is the Expression of the entire parse tree.
    
    /**
     * Returns the expression constructed by this listener object.
     * Requires that this listener has completely walked over an Expression
     * parse tree using ParseTreeWalker.
     * 
     * @return Expression for the parse tree that was walked
     */
    public Expression getExpression() {
        return stack.get(0);
    }
    
    @Override public void exitRoot(ExpressionParser.RootContext context) {
        // do nothing, root has only one child so its value is 
        // already on top of the stack
    }

    @Override public void exitSum(ExpressionParser.SumContext context) {  
        // matched the product ('+' product)* rule
        List<ExpressionParser.ProductContext> addends = context.product();
        assert stack.size() >= addends.size();
        
        // the pattern above always has at least 1 child;
        // pop the last child
        assert addends.size() > 0;
        Expression sum = stack.pop();
        
        // pop the older children, one by one, and add them on
        for (int i = 1; i < addends.size(); ++i) {
            sum = new Plus(stack.pop(), sum);
        }
        
        // the result is this subtree's Expression
        stack.push(sum);
    }
    
    @Override public void exitProduct(ExpressionParser.ProductContext context) {  
        // matched the primitive ('*' primitive)* rule
        List<ExpressionParser.PrimitiveContext> addends = context.primitive();
        assert stack.size() >= addends.size();
        
        // the pattern above always has at least 1 child;
        // pop the last child
        assert addends.size() > 0;
        Expression product = stack.pop();
        
        // pop the older children, one by one, and add them on
        for (int i = 1; i < addends.size(); ++i) {
            product = new Times(stack.pop(), product);
        }
        
        // the result is this subtree's Expression
        stack.push(product);
    }

    @Override public void exitPrimitive(ExpressionParser.PrimitiveContext context) {
        if (context.NUMBER() != null) {
            // matched the NUMBER alternative
            Double n = Double.valueOf(context.NUMBER().getText());
            Expression number = new Number(n);
            stack.push(number);
        } else if (context.VARIABLE() != null) {
            // matched the VARIABLE alternative
            String s = context.VARIABLE().getText();
            Expression var = new Variable(s);
            stack.push(var);
        } else {
            // matched the '(' sum ')' alternative
            // do nothing, because sum's value is already on the stack
        }
    }

    // don't need these here, so just make them empty implementations
    @Override public void enterRoot(ExpressionParser.RootContext context) { }
    @Override public void enterSum(ExpressionParser.SumContext context) { }
    @Override public void enterProduct(ExpressionParser.ProductContext context) { }
    @Override public void enterPrimitive(ExpressionParser.PrimitiveContext context) { }

    @Override public void visitTerminal(TerminalNode terminal) { }
    @Override public void enterEveryRule(ParserRuleContext context) { }
    @Override public void exitEveryRule(ParserRuleContext context) { }
    @Override public void visitErrorNode(ErrorNode node) { }        
}
