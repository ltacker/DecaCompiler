package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable.Symbol;

import java.util.HashMap;

/**
 * Dictionary associating identifier's ExpDefinition to their names.
 *
 * This is actually a linked list of dictionaries: each EnvironmentExp has a
 * pointer to a parentEnvironment, corresponding to superblock (eg superclass).
 *
 * The dictionary at the head of this list thus corresponds to the "current"
 * block (eg class).
 *
 * Searching a definition (through method get) is done in the "current"
 * dictionary and in the parentEnvironment if it fails.
 *
 * Insertion (through method declare) is always done in the "current" dictionary.
 *
 * @author gl35
 * @date 01/01/2017
 */
public class EnvironmentExp {
    private HashMap<Symbol, ExpDefinition> variables;

    private EnvironmentExp parentEnvironment;

    public EnvironmentExp(EnvironmentExp parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
        variables = new HashMap<>();
    }

    public HashMap<Symbol, ExpDefinition> getHashMap() {
        return variables;
    }

    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }


    /*
    public HashMap<Symbol, ExpDefinition> getHashMap() {
        reutrn variables;
    }
    */


    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public ExpDefinition get(Symbol key) {
        if (variables.containsKey(key)) {
            return variables.get(key);
        }
        else if (parentEnvironment != null) {
            return parentEnvironment.get(key);
        }
        return null;
    }

    public EnvironmentExp getParent() {
        return parentEnvironment;
    }

    /**
     * Add the definition def associated to the symbol name in the environment.
     *
     * Adding a symbol which is already defined in the environment,
     * - throws DoubleDefException if the symbol is in the "current" dictionary
     * - or, hides the previous declaration otherwise.
     *
     * @param name
     *            Name of the symbol to define
     * @param def
     *            Definition of the symbol
     * @throws DoubleDefException
     *             if the symbol is already defined at the "current" dictionary
     *
     */
    public void declare(Symbol name, ExpDefinition def) throws DoubleDefException {
        if (!variables.containsKey(name)) {
            variables.put(name, def);
        }
        else {
            throw new DoubleDefException();
        }
    }

}
