// generated with ast extension for cup
// version 0.8
// 29/7/2022 15:43:48


package rs.ac.bg.etf.pp1.ast;

public class MathcedIfElse extends StatementDrugi {

    private StmIfElse StmIfElse;

    public MathcedIfElse (StmIfElse StmIfElse) {
        this.StmIfElse=StmIfElse;
        if(StmIfElse!=null) StmIfElse.setParent(this);
    }

    public StmIfElse getStmIfElse() {
        return StmIfElse;
    }

    public void setStmIfElse(StmIfElse StmIfElse) {
        this.StmIfElse=StmIfElse;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(StmIfElse!=null) StmIfElse.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(StmIfElse!=null) StmIfElse.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(StmIfElse!=null) StmIfElse.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MathcedIfElse(\n");

        if(StmIfElse!=null)
            buffer.append(StmIfElse.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MathcedIfElse]");
        return buffer.toString();
    }
}
