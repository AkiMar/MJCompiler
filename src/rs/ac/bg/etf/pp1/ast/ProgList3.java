// generated with ast extension for cup
// version 0.8
// 29/7/2022 15:43:48


package rs.ac.bg.etf.pp1.ast;

public class ProgList3 extends ProgramListaProm {

    private KlasaDecl KlasaDecl;
    private ProgramListaProm ProgramListaProm;

    public ProgList3 (KlasaDecl KlasaDecl, ProgramListaProm ProgramListaProm) {
        this.KlasaDecl=KlasaDecl;
        if(KlasaDecl!=null) KlasaDecl.setParent(this);
        this.ProgramListaProm=ProgramListaProm;
        if(ProgramListaProm!=null) ProgramListaProm.setParent(this);
    }

    public KlasaDecl getKlasaDecl() {
        return KlasaDecl;
    }

    public void setKlasaDecl(KlasaDecl KlasaDecl) {
        this.KlasaDecl=KlasaDecl;
    }

    public ProgramListaProm getProgramListaProm() {
        return ProgramListaProm;
    }

    public void setProgramListaProm(ProgramListaProm ProgramListaProm) {
        this.ProgramListaProm=ProgramListaProm;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(KlasaDecl!=null) KlasaDecl.accept(visitor);
        if(ProgramListaProm!=null) ProgramListaProm.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(KlasaDecl!=null) KlasaDecl.traverseTopDown(visitor);
        if(ProgramListaProm!=null) ProgramListaProm.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(KlasaDecl!=null) KlasaDecl.traverseBottomUp(visitor);
        if(ProgramListaProm!=null) ProgramListaProm.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ProgList3(\n");

        if(KlasaDecl!=null)
            buffer.append(KlasaDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ProgramListaProm!=null)
            buffer.append(ProgramListaProm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ProgList3]");
        return buffer.toString();
    }
}
