// generated with ast extension for cup
// version 0.8
// 29/7/2022 15:43:48


package rs.ac.bg.etf.pp1.ast;

public class KLasaDecl2 extends KlasaDecl {

    private String I1;
    private KlasaListVar KlasaListVar;
    private MethodDeclList MethodDeclList;

    public KLasaDecl2 (String I1, KlasaListVar KlasaListVar, MethodDeclList MethodDeclList) {
        this.I1=I1;
        this.KlasaListVar=KlasaListVar;
        if(KlasaListVar!=null) KlasaListVar.setParent(this);
        this.MethodDeclList=MethodDeclList;
        if(MethodDeclList!=null) MethodDeclList.setParent(this);
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
    }

    public KlasaListVar getKlasaListVar() {
        return KlasaListVar;
    }

    public void setKlasaListVar(KlasaListVar KlasaListVar) {
        this.KlasaListVar=KlasaListVar;
    }

    public MethodDeclList getMethodDeclList() {
        return MethodDeclList;
    }

    public void setMethodDeclList(MethodDeclList MethodDeclList) {
        this.MethodDeclList=MethodDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(KlasaListVar!=null) KlasaListVar.accept(visitor);
        if(MethodDeclList!=null) MethodDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(KlasaListVar!=null) KlasaListVar.traverseTopDown(visitor);
        if(MethodDeclList!=null) MethodDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(KlasaListVar!=null) KlasaListVar.traverseBottomUp(visitor);
        if(MethodDeclList!=null) MethodDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("KLasaDecl2(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(KlasaListVar!=null)
            buffer.append(KlasaListVar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodDeclList!=null)
            buffer.append(MethodDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [KLasaDecl2]");
        return buffer.toString();
    }
}
