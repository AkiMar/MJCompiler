// generated with ast extension for cup
// version 0.8
// 29/7/2022 15:43:48


package rs.ac.bg.etf.pp1.ast;

public class DoWhileStm extends StatementPrvi {

    private WhileStm WhileStm;

    public DoWhileStm (WhileStm WhileStm) {
        this.WhileStm=WhileStm;
        if(WhileStm!=null) WhileStm.setParent(this);
    }

    public WhileStm getWhileStm() {
        return WhileStm;
    }

    public void setWhileStm(WhileStm WhileStm) {
        this.WhileStm=WhileStm;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(WhileStm!=null) WhileStm.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(WhileStm!=null) WhileStm.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(WhileStm!=null) WhileStm.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DoWhileStm(\n");

        if(WhileStm!=null)
            buffer.append(WhileStm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DoWhileStm]");
        return buffer.toString();
    }
}
