package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * Created by buthodgt on 1/12/17.
 * Voir si on peut fusionner cette classe avec DeclVar
 */
public class DeclField extends AbstractDeclField {
    final private Visibility privacy;
    final private AbstractIdentifier type;
    final private AbstractIdentifier fieldName;
    private AbstractInitialization initialization;

    public DeclField(Visibility privacy, AbstractIdentifier type, AbstractIdentifier fieldName,
                     AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(fieldName);
        Validate.notNull(initialization);
        this.privacy = privacy;
        this.type = type;
        this.fieldName = fieldName;
        this.initialization = initialization;
    }

    public AbstractIdentifier getFieldName(){
        return this.fieldName;
    }

    public AbstractIdentifier getType(){
        return this.type;
    }

    public AbstractInitialization getInitialization(){
        return this.initialization;
    }

    /** Verify the pass 2.
     *  Control if a field with the same name has already been declared.
     *
     * @param compiler
     * @param currentClass
     * @throws ContextualError
     */
    @Override
    protected void verifyDeclFieldHeader(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError {
        Type t = type.verifyType(compiler);
        if (compiler.getType(fieldName.getName()) != null) {
            throw new ContextualError("Field name used is a type", this.getLocation());
        }
        int index = currentClass.getNumberOfFields()+1;
        fieldName.setDefinition(new FieldDefinition(t, this.getLocation(), privacy, currentClass, index));
        try {
            currentClass.getMembers().declare(fieldName.getName(), fieldName.getFieldDefinition());
        }
        catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Field already defined", this.getLocation());
        }
        currentClass.incNumberOfFields();
    }

    /** Verify the pass 3.
     *
     * @param compiler
     * @param currentClass
     * @throws ContextualError
     */
    @Override
    protected void verifyDeclFieldInit(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError {
        initialization.verifyInitialization(compiler, type.getType(), currentClass.getMembers(), currentClass);
    }


    @Override
    public void decompile(IndentPrintStream s) {
        if (privacy == Visibility.PROTECTED) {
            s.print("protected ");
        }
        type.decompile(s);
        s.print(" ");
        fieldName.decompile(s);
        initialization.decompile(s);
        s.print(";");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        fieldName.iter(f);
        initialization.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        fieldName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
