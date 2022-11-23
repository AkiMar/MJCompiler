// generated with ast extension for cup
// version 0.8
// 29/7/2022 15:43:48


package rs.ac.bg.etf.pp1.ast;

public class ProgList1 extends ProgramListaProm {

    private VarDecl VarDecl;
    private ProgramListaProm ProgramListaProm;

    public ProgList1 (VarDecl VarDecl, ProgramListaProm ProgramListaProm) {
        this.VarDecl=VarDecl;
        if(VarDecl!=null) VarDecl.setParent(this);
        this.ProgramListaProm=ProgramListaProm;
        if(ProgramListaProm!=null) ProgramListaProm.setParent(this);
    }

    public VarDecl getVarDecl() {
        return VarDecl;
    }

    public void setVarDecl(VarDecl VarDecl) {
        this.VarDecl=VarDecl;
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
        if(VarDecl!=null) VarDecl.accept(visitor);
        if(ProgramListaProm!=null) ProgramListaProm.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDecl!=null) VarDecl.traverseTopDown(visitor);
        if(ProgramListaProm!=null) ProgramListaProm.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDecl!=null) VarDecl.traverseBottomUp(visitor);
        if(ProgramListaProm!=null) ProgramListaProm.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ProgList1(\n");

        if(VarDecl!=null)
            buffer.append(VarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ProgramListaProm!=null)
            buffer.append(ProgramListaProm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ProgList1]");
        return buffer.toString();
    }
}
