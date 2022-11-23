// generated with ast extension for cup
// version 0.8
// 29/7/2022 15:43:48


package rs.ac.bg.etf.pp1.ast;

public class Term implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private TermDodatak TermDodatak;

    public Term (TermDodatak TermDodatak) {
        this.TermDodatak=TermDodatak;
        if(TermDodatak!=null) TermDodatak.setParent(this);
    }

    public TermDodatak getTermDodatak() {
        return TermDodatak;
    }

    public void setTermDodatak(TermDodatak TermDodatak) {
        this.TermDodatak=TermDodatak;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(TermDodatak!=null) TermDodatak.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(TermDodatak!=null) TermDodatak.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(TermDodatak!=null) TermDodatak.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Term(\n");

        if(TermDodatak!=null)
            buffer.append(TermDodatak.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Term]");
        return buffer.toString();
    }
}
