// generated with ast extension for cup
// version 0.8
// 29/7/2022 15:43:48


package rs.ac.bg.etf.pp1.ast;

public class KlasaListaVar extends KlasaListVar {

    private KlasaListVar KlasaListVar;
    private VarDecl VarDecl;

    public KlasaListaVar (KlasaListVar KlasaListVar, VarDecl VarDecl) {
        this.KlasaListVar=KlasaListVar;
        if(KlasaListVar!=null) KlasaListVar.setParent(this);
        this.VarDecl=VarDecl;
        if(VarDecl!=null) VarDecl.setParent(this);
    }

    public KlasaListVar getKlasaListVar() {
        return KlasaListVar;
    }

    public void setKlasaListVar(KlasaListVar KlasaListVar) {
        this.KlasaListVar=KlasaListVar;
    }

    public VarDecl getVarDecl() {
        return VarDecl;
    }

    public void setVarDecl(VarDecl VarDecl) {
        this.VarDecl=VarDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(KlasaListVar!=null) KlasaListVar.accept(visitor);
        if(VarDecl!=null) VarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(KlasaListVar!=null) KlasaListVar.traverseTopDown(visitor);
        if(VarDecl!=null) VarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(KlasaListVar!=null) KlasaListVar.traverseBottomUp(visitor);
        if(VarDecl!=null) VarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("KlasaListaVar(\n");

        if(KlasaListVar!=null)
            buffer.append(KlasaListVar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDecl!=null)
            buffer.append(VarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [KlasaListaVar]");
        return buffer.toString();
    }
}
