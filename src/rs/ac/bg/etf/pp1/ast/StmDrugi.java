// generated with ast extension for cup
// version 0.8
// 29/7/2022 15:43:48


package rs.ac.bg.etf.pp1.ast;

public class StmDrugi extends Statement {

    private StatementDrugi StatementDrugi;

    public StmDrugi (StatementDrugi StatementDrugi) {
        this.StatementDrugi=StatementDrugi;
        if(StatementDrugi!=null) StatementDrugi.setParent(this);
    }

    public StatementDrugi getStatementDrugi() {
        return StatementDrugi;
    }

    public void setStatementDrugi(StatementDrugi StatementDrugi) {
        this.StatementDrugi=StatementDrugi;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StatementDrugi!=null) StatementDrugi.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StatementDrugi!=null) StatementDrugi.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StatementDrugi!=null) StatementDrugi.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StmDrugi(\n");

        if(StatementDrugi!=null)
            buffer.append(StatementDrugi.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StmDrugi]");
        return buffer.toString();
    }
}
