package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.GenCode;
import fr.ensimag.deca.context.*;
import fr.ensimag.ima.pseudocode.*;

/**
 * Initialization (of variable, field, ...)
 *
 * @author gl35
 * @date 01/01/2017
 */
public abstract class AbstractInitialization extends Tree {

    /**
     * Implements non-terminal "initialization" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param t corresponds to the "type" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass
     *          corresponds to the "class" attribute (null in the main bloc).
     */
    protected abstract void verifyInitialization(DecacCompiler compiler,
            Type t, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;


    // Permet de generer le code d'initialisation
    public void codeGenInit(DAddr addr, DecacCompiler comp, GenCode gc, Type t){
    }

    // Permet de generer le code d'initialisation sans le stockage
    public void codeGenInit(DecacCompiler comp, GenCode gc, Type t){
    }

}
