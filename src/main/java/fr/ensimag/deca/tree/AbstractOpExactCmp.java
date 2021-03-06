package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

/**
 *
 * @author gl35
 * @date 01/01/2017
 */
public abstract class AbstractOpExactCmp extends AbstractOpCmp {

    public AbstractOpExactCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        //A modifier pour les classes
        AbstractExpr leftOp = this.getLeftOperand();
        AbstractExpr rightOp = this.getRightOperand();
        Type typeL = leftOp.verifyExpr(compiler,localEnv,currentClass);
        Type typeR = rightOp.verifyExpr(compiler, localEnv, currentClass);
        if (typeL.sameType(typeR) || (typeL.isClassOrNull() && typeR.isClassOrNull())) {
            //On autorise la comparaison de 2 classes quelconques.
        }
        else if (typeL.isFloat() && typeR.isInt()) {
            ConvFloat conv = new ConvFloat(rightOp);
            conv.verifyExpr(compiler, localEnv, currentClass);
            conv.setLocation(rightOp.getLocation());
            this.setRightOperand(conv);
        }
        else if (typeL.isInt() && typeR.isFloat()) {
            ConvFloat conv = new ConvFloat(leftOp);
            conv.verifyExpr(compiler, localEnv, currentClass);
            conv.setLocation(leftOp.getLocation());
            this.setLeftOperand(conv);
        }
        else {
            throw new ContextualError("Types not compatible for exact comparison", this.getLocation());
        }
        Type t = compiler.getType("boolean");
        this.setType(t);
        return t;
    }
}
