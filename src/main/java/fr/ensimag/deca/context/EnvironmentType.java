package fr.ensimag.deca.context;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.*;

import java.util.HashMap;

/**
 * Created by buthodgt on 1/9/17.
 */
public class EnvironmentType {

    private HashMap<Symbol, Type> env_type;

    private HashMap<Type, Definition> def_type;

    private AbstractIdentifier object;

    /**Constructeur qui crée tout l'environnement des types par défaut avec leur définition.
     * La location pour les types par défaut est initialisée à 0, 0, "Default".
     * @param symbols
     */
    public EnvironmentType(SymbolTable symbols) {
        env_type = new HashMap<>();
        def_type = new HashMap<>();
        //Création des types de base.
        Type t[] = new Type[4];
        Symbol intSym = symbols.create("int");
        t[0] = new IntType(intSym);
        env_type.put(intSym, t[0]);
        Symbol floatSym = symbols.create("float");
        t[1] = new FloatType(floatSym);
        env_type.put(floatSym, t[1]);
        Symbol boolSym = symbols.create("boolean");
        t[2] = new BooleanType(boolSym);
        env_type.put(boolSym, t[2]);
        Symbol voidSym = symbols.create("void");
        t[3] = new VoidType(voidSym);
        env_type.put(voidSym, t[3]);
        for (int i=0; i<t.length; i++) {
            Location loc = new Location(0,0,"Default");
            def_type.put(t[i], new TypeDefinition(t[i],loc));
        }
        //Création du type Object.
        Symbol objectSym = symbols.create("Object");
        ClassType objectType = new ClassType(objectSym,new Location(0,0,"Default"),null);
        env_type.put(objectSym, objectType);
        object = new Identifier(objectSym);
        ClassDefinition classDef = objectType.getDefinition();
        object.setDefinition(classDef);
        object.setLocation(0,0,"Default");
        def_type.put(objectType, object.getDefinition());

        //Création de la méthode equals
        EnvironmentExp env_exp = objectType.getDefinition().getMembers();
        Symbol equalsSym = symbols.create("equals");
        Signature sign = new Signature();
        sign.add(objectType);
        MethodDefinition eqDef = new MethodDefinition(t[2],new Location(0,0,"Default"),sign,1);
        try {
            env_exp.declare(equalsSym, eqDef);
        }
        catch (EnvironmentExp.DoubleDefException e) {
            throw new DecacInternalError("Méthode equals défini 2 fois.");
        }
        classDef.incNumberOfMethods();
    }

    public Type getType(SymbolTable.Symbol sym) {
        if (env_type.containsKey(sym)) {
            return env_type.get(sym);
        }
        else {
            return null;
        }
    }

    public Definition getDefinition(Type t) {
        if (def_type.containsKey(t)) {
            return def_type.get(t);
        }
        else {
            return null;
        }
    }

    /**Ajoute un type et renvoie une erreur si le type est déjà existant.
     * Ce type est ajouté aux 2 tables de hachage de cette classe.
     * @param sym
     * @param t
     * @param def
     */
    public void addType(Symbol sym, Type t, Definition def) {
        if (env_type.containsKey(sym) || def_type.containsKey(t)) {
            throw new DecacInternalError("Définition d'un type déjà existant.");
        }
        env_type.put(sym, t);
        def_type.put(t, def);
    }

    /**Renvoie un identifieur faire la classe Object de base dans le langage.
     * @return
     */
    public AbstractIdentifier getObject() {
        return this.object;
    }
}
