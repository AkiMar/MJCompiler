// generated with ast extension for cup
// version 0.8
// 29/7/2022 15:43:48


package rs.ac.bg.etf.pp1.ast;

public class FnewExpr extends Factor {

    private Type Type;
    private FactorOpcija FactorOpcija;

    public FnewExpr (Type Type, FactorOpcija FactorOpcija) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.FactorOpcija=FactorOpcija;
        if(FactorOpcija!=null) FactorOpcija.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public FactorOpcija getFactorOpcija() {
        return FactorOpcija;
    }

    public void setFactorOpcija(FactorOpcija FactorOpcija) {
        this.FactorOpcija=FactorOpcija;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(FactorOpcija!=null) FactorOpcija.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(FactorOpcija!=null) FactorOpcija.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(FactorOpcija!=null) FactorOpcija.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FnewExpr(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FactorOpcija!=null)
            buffer.append(FactorOpcija.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FnewExpr]");
        return buffer.toString();
    }
}
