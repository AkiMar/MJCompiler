// generated with ast extension for cup
// version 0.8
// 29/7/2022 15:43:48


package rs.ac.bg.etf.pp1.ast;

public class KlasaDecl1 extends KlasaDecl {

    private String I1;
    private KlasaListVar KlasaListVar;

    public KlasaDecl1 (String I1, KlasaListVar KlasaListVar) {
        this.I1=I1;
        this.KlasaListVar=KlasaListVar;
        if(KlasaListVar!=null) KlasaListVar.setParent(this);
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

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(KlasaListVar!=null) KlasaListVar.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(KlasaListVar!=null) KlasaListVar.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(KlasaListVar!=null) KlasaListVar.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("KlasaDecl1(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(KlasaListVar!=null)
            buffer.append(KlasaListVar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [KlasaDecl1]");
        return buffer.toString();
    }
}
