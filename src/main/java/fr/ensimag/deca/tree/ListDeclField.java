package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

import javax.naming.Context;

/**
 * Created by buthodgt on 1/12/17.
 */
public class ListDeclField extends TreeList<AbstractDeclField>{
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField f : getList()) {
            f.decompile(s);
            s.println();
        }
    }

    void verifyListField(DecacCompiler compiler) throws ContextualError {
        //TODO : Surement à modifier pour prendre en compte la classe
        for (AbstractDeclField f : getList()) {
            //A modifier pour environnement
            f.verifyDeclField(compiler, null, null);
        }
    }
}
