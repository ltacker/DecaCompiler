package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl35
 * @date 01/01/2017
 */
public class ListExpr extends TreeList<AbstractExpr> {

    public ListExpr verifyArgs(DecacCompiler compiler, EnvironmentExp localenv, ClassDefinition currentClass,
                           MethodDefinition methDef) throws ContextualError{
        Signature sign = methDef.getSignature();
        if (sign.size() > size()) {
            throw new ContextualError("Not enough arguments for this method", this.getLocation());
        }
        else if (sign.size() < size()) {
            throw new ContextualError("Too many arguments for this method", this.getLocation());
        }
        int i = 0;
        ListExpr le = new ListExpr();
        for (AbstractExpr expr : getList()) {
            le.add(expr.verifyRValue(compiler, localenv, currentClass, sign.paramNumber(i)));
            i++;
        }
        return le;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        int i=0;
        for (AbstractExpr expr : getList()) {
            if (i>0) {
                s.print(", ");
            }
            i++;
            expr.decompile(s);
        }
    }
}
