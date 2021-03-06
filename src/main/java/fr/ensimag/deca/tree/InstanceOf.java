package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.codegen.GenCode;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.*;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * Created by clastret on 1/17/17.
 */
public class InstanceOf extends AbstractExpr {

    AbstractIdentifier ident_type;
    AbstractExpr expr;
    public InstanceOf(AbstractIdentifier type, AbstractExpr expr) {
        Validate.notNull(type);
        Validate.notNull(expr);
        this.ident_type= type;
        this.expr=expr;
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        expr.decompile(s);
        s.print(" instanceof ");
        ident_type.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        ident_type.prettyPrint(s,prefix, false);
        expr.prettyPrint(s,prefix,true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expr.iter(f);
        ident_type.iter(f);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type type_ref = ident_type.verifyType(compiler);
        Type type = expr.verifyExpr(compiler,localEnv,currentClass);

        if (!type.isNull() && !type.isClass()) {
            throw new ContextualError("Impossible InstanceOf, type of expression is not compatible", expr.getLocation());
        }
        if (!type_ref.isClass()) {
            throw new ContextualError("Impossible InstanceOf, type is not a class", ident_type.getLocation());
        }
        Type t = compiler.getType("boolean");
        this.setType(t);
        return t;
    }


    @Override
    public void codeGenInst(DecacCompiler compiler, GenCode gc) {
        Label debut = gc.newLabel("InstanceOfDebut");
        Label bloc = gc.newLabel("InstanceOfBloc");
        Label find = gc.newLabel("InstanceOfFind");
        Label notFind = gc.newLabel("InstanceOfNotFind");
        Label fin = gc.newLabel("InstanceOfFin");

        // On obtient l'adresse de la classe à retrouver
        int offset = ident_type.getClassDefinition().getOffset();
        DAddr addrClass = new RegisterOffset(offset, Register.GB);

        // On réalise l'expression
        expr.codeGenInst(compiler, gc);

        // Si l'objet est null alors ce n'est pas une instacne
        compiler.addInstruction(new CMP(new NullOperand(), gc.getRetReg()));
        compiler.addInstruction(new LOAD(0, gc.getRetReg()));
        compiler.addInstruction(new BEQ(fin));

        compiler.addLabel(debut);
        compiler.addInstruction(new LOAD(new RegisterOffset(0, gc.getRetReg()), gc.getRetReg()));
        compiler.addInstruction(new CMP(new NullOperand(), gc.getRetReg()));
        compiler.addInstruction(new BNE(bloc));
        compiler.addInstruction(new BRA(notFind));

        compiler.addLabel(bloc);
        compiler.addInstruction(new LEA(addrClass, gc.getR0()));
        compiler.addInstruction(new CMP(gc.getR0(), gc.getRetReg()));
        compiler.addInstruction(new BEQ(find));
        compiler.addInstruction(new BRA(debut));

        compiler.addLabel(find);
        // Classe trouvée
        compiler.addInstruction(new LOAD(1, gc.getRetReg()));
        compiler.addInstruction(new BRA(fin));

        compiler.addLabel(notFind);
        // Classe non trouvée
        compiler.addInstruction(new LOAD(0, gc.getRetReg()));

        compiler.addLabel(fin);
    }
}
