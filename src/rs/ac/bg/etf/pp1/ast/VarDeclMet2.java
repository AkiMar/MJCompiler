// generated with ast extension for cup
// version 0.8
// 29/7/2022 15:43:48


package rs.ac.bg.etf.pp1.ast;

public class VarDeclMet2 extends VarDeclMet {

    private VarDeclMet VarDeclMet;
    private Type Type;
    private String varName;

    public VarDeclMet2 (VarDeclMet VarDeclMet, Type Type, String varName) {
        this.VarDeclMet=VarDeclMet;
        if(VarDeclMet!=null) VarDeclMet.setParent(this);
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.varName=varName;
    }

    public VarDeclMet getVarDeclMet() {
        return VarDeclMet;
    }

    public void setVarDeclMet(VarDeclMet VarDeclMet) {
        this.VarDeclMet=VarDeclMet;
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName=varName;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclMet!=null) VarDeclMet.accept(visitor);
        if(Type!=null) Type.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclMet!=null) VarDeclMet.traverseTopDown(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclMet!=null) VarDeclMet.traverseBottomUp(visitor);
        if(Type!=null) Type.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclMet2(\n");

        if(VarDeclMet!=null)
            buffer.append(VarDeclMet.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+varName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclMet2]");
        return buffer.toString();
    }
}
