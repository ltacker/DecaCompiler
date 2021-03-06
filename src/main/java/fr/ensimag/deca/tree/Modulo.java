package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GenCode;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.*;
import javax.naming.Context;

/**
 *
 * @author gl35
 * @date 01/01/2017
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        AbstractExpr leftOp = this.getLeftOperand();
        AbstractExpr rightOp = this.getRightOperand();
        Type typeL = leftOp.verifyExpr(compiler,localEnv,currentClass);
        Type typeR = rightOp.verifyExpr(compiler, localEnv, currentClass);
        if (!typeL.isInt() || !typeR.isInt()) {
            throw new ContextualError("Modulo applies only on int.", this.getLocation());
        }
        this.setType(typeL);
        return typeL;
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GenCode gc) {
        GPRegister tmp;   // registre temporaire pour les expressions binaires
        super.codeGenInst(compiler, gc);

        tmp = gc.popTmpReg();

        // On verifie que le dénominateur n'est pas nul
        compiler.addInstruction(new CMP(0, gc.getRetReg()));
        compiler.addInstruction(new BEQ(gc.getDividedZero()));

        compiler.addInstruction(new REM(gc.getRetReg(), tmp));
        compiler.addInstruction(new LOAD(tmp, gc.getRetReg()));
    }

}
