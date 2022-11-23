// generated with ast extension for cup
// version 0.8
// 29/7/2022 15:43:48


package rs.ac.bg.etf.pp1.ast;

public class StmIfElse2 extends StmIfElse {

    private IfStart IfStart;
    private Condition Condition;
    private StatementPrviList StatementPrviList;
    private ElseStart ElseStart;
    private Statement Statement;

    public StmIfElse2 (IfStart IfStart, Condition Condition, StatementPrviList StatementPrviList, ElseStart ElseStart, Statement Statement) {
        this.IfStart=IfStart;
        if(IfStart!=null) IfStart.setParent(this);
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
        this.StatementPrviList=StatementPrviList;
        if(StatementPrviList!=null) StatementPrviList.setParent(this);
        this.ElseStart=ElseStart;
        if(ElseStart!=null) ElseStart.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public IfStart getIfStart() {
        return IfStart;
    }

    public void setIfStart(IfStart IfStart) {
        this.IfStart=IfStart;
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public StatementPrviList getStatementPrviList() {
        return StatementPrviList;
    }

    public void setStatementPrviList(StatementPrviList StatementPrviList) {
        this.StatementPrviList=StatementPrviList;
    }

    public ElseStart getElseStart() {
        return ElseStart;
    }

    public void setElseStart(ElseStart ElseStart) {
        this.ElseStart=ElseStart;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(IfStart!=null) IfStart.accept(visitor);
        if(Condition!=null) Condition.accept(visitor);
        if(StatementPrviList!=null) StatementPrviList.accept(visitor);
        if(ElseStart!=null) ElseStart.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IfStart!=null) IfStart.traverseTopDown(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
        if(StatementPrviList!=null) StatementPrviList.traverseTopDown(visitor);
        if(ElseStart!=null) ElseStart.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IfStart!=null) IfStart.traverseBottomUp(visitor);
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        if(StatementPrviList!=null) StatementPrviList.traverseBottomUp(visitor);
        if(ElseStart!=null) ElseStart.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StmIfElse2(\n");

        if(IfStart!=null)
            buffer.append(IfStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementPrviList!=null)
            buffer.append(StatementPrviList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ElseStart!=null)
            buffer.append(ElseStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StmIfElse2]");
        return buffer.toString();
    }
}
