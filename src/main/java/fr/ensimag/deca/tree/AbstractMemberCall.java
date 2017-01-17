package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import org.apache.commons.lang.Validate;

/**
 * Created by buthodgt on 1/17/17.
 */
public abstract class AbstractMemberCall extends AbstractExpr {

    private AbstractExpr objectName;

    public AbstractMemberCall(AbstractExpr objectName) {
        Validate.notNull(objectName, "Object passé en argument est null.");
        this.objectName = objectName;
    }

    public abstract Type verifyMember(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
                                 ClassType typeObject) throws ContextualError ;

    @Override
    public Type verifyExpr(DecacCompiler compiler,
                           EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type t = objectName.verifyExpr(compiler, localEnv, currentClass);
        if (!t.isClass()) {
            throw new ContextualError("Cet objet n'est pas un type.", this.getLocation());
        }
        ClassType typeClass = t.asClassType("Type n'est pas une classe.", this.getLocation());
        Type memberType = verifyMember(compiler, localEnv, currentClass, typeClass);
        this.setType(memberType);
        return memberType;
    }
}
