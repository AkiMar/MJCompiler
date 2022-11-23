// generated with ast extension for cup
// version 0.8
// 29/7/2022 15:43:48


package rs.ac.bg.etf.pp1.ast;

public class StmPrvi extends Statement {

    private StatementPrvi StatementPrvi;

    public StmPrvi (StatementPrvi StatementPrvi) {
        this.StatementPrvi=StatementPrvi;
        if(StatementPrvi!=null) StatementPrvi.setParent(this);
    }

    public StatementPrvi getStatementPrvi() {
        return StatementPrvi;
    }

    public void setStatementPrvi(StatementPrvi StatementPrvi) {
        this.StatementPrvi=StatementPrvi;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StatementPrvi!=null) StatementPrvi.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StatementPrvi!=null) StatementPrvi.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StatementPrvi!=null) StatementPrvi.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StmPrvi(\n");

        if(StatementPrvi!=null)
            buffer.append(StatementPrvi.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StmPrvi]");
        return buffer.toString();
    }
}
