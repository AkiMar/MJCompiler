// generated with ast extension for cup
// version 0.8
// 29/7/2022 15:43:48


package rs.ac.bg.etf.pp1.ast;

public class StmsPrviList extends StatementPrviList {

    private StatementPrviList StatementPrviList;
    private StatementPrvi StatementPrvi;

    public StmsPrviList (StatementPrviList StatementPrviList, StatementPrvi StatementPrvi) {
        this.StatementPrviList=StatementPrviList;
        if(StatementPrviList!=null) StatementPrviList.setParent(this);
        this.StatementPrvi=StatementPrvi;
        if(StatementPrvi!=null) StatementPrvi.setParent(this);
    }

    public StatementPrviList getStatementPrviList() {
        return StatementPrviList;
    }

    public void setStatementPrviList(StatementPrviList StatementPrviList) {
        this.StatementPrviList=StatementPrviList;
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
        if(StatementPrviList!=null) StatementPrviList.accept(visitor);
        if(StatementPrvi!=null) StatementPrvi.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StatementPrviList!=null) StatementPrviList.traverseTopDown(visitor);
        if(StatementPrvi!=null) StatementPrvi.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StatementPrviList!=null) StatementPrviList.traverseBottomUp(visitor);
        if(StatementPrvi!=null) StatementPrvi.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StmsPrviList(\n");

        if(StatementPrviList!=null)
            buffer.append(StatementPrviList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementPrvi!=null)
            buffer.append(StatementPrvi.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StmsPrviList]");
        return buffer.toString();
    }
}
