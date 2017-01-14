package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl35
 * @date 01/01/2017
 */
public class DeclClass extends AbstractDeclClass {

    final private AbstractIdentifier className;
    final private AbstractIdentifier superName;
    private ListDeclField fields;
    private ListDeclMethod methods;

    public DeclClass(AbstractIdentifier className, AbstractIdentifier superName, ListDeclMethod methods, ListDeclField fields) {
        Validate.notNull(className);
        Validate.notNull(superName);
        Validate.notNull(methods);
        Validate.notNull(fields);
        this.className = className;
        this.superName = superName;
        this.methods = methods;
        this.fields =  fields;

    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class { ... A FAIRE ... }");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        if (compiler.getType(this.className.getName()) != null) {
            throw new ContextualError("Classe déjà définie.", this.getLocation());
        }
        String errorMessage = superName.getName() + " n'est pas un type de classe.";
        ClassType superType = compiler.getType(superName.getName()).asClassType(errorMessage, this.getLocation());
        Validate.notNull(superType, "Erreur : Le type de la super classe est nulle.");
        ClassType t = new ClassType(className.getName(), this.getLocation(), superType.getDefinition());
        compiler.addType(className.getName(), t);
        className.setDefinition(new ClassDefinition(t,this.getLocation(),superName.getClassDefinition()));
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        this.fields.verifyListFieldHeader(compiler, className.getClassDefinition());
        this.methods.verifyListMethodHeader(compiler, className.getClassDefinition());
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        throw new UnsupportedOperationException("Not yet supported");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet supported");
    }

}
