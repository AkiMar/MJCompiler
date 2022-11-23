// generated with ast extension for cup
// version 0.8
// 29/7/2022 15:43:48


package rs.ac.bg.etf.pp1.ast;

public class MatchedIf extends StatementDrugi {

    private StmIf StmIf;

    public MatchedIf (StmIf StmIf) {
        this.StmIf=StmIf;
        if(StmIf!=null) StmIf.setParent(this);
    }

    public StmIf getStmIf() {
        return StmIf;
    }

    public void setStmIf(StmIf StmIf) {
        this.StmIf=StmIf;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StmIf!=null) StmIf.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StmIf!=null) StmIf.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StmIf!=null) StmIf.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MatchedIf(\n");

        if(StmIf!=null)
            buffer.append(StmIf.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MatchedIf]");
        return buffer.toString();
    }
}
