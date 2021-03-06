package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GenCode;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.*;

/**
 *
 * @author gl35
 * @date 01/01/2017
 */
public class LowerOrEqual extends AbstractOpIneq {
    public LowerOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "<=";
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GenCode gc) {
        GPRegister tmp;
        Label vrai = gc.newLabel("LowerEqualsVrai");  // Label lorsque l'expression est vrai
        Label fin = gc.newLabel("LowerEqualsFin");   // Label de la fin de l'expression

        super.codeGenInst(compiler, gc);
        tmp = gc.popTmpReg();
        compiler.addInstruction(new CMP(gc.getRetReg(), tmp));
        compiler.addInstruction(new BLE(vrai));

        // Les expressions ne sont pas egales
        compiler.addInstruction(new LOAD(0, gc.getRetReg()));
        compiler.addInstruction(new BRA(fin));

        // Les expressions sont egales
        compiler.addLabel(vrai);
        compiler.addInstruction(new LOAD(1, gc.getRetReg()));

        compiler.addLabel(fin);
    }

}
