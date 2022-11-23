// generated with ast extension for cup
// version 0.8
// 29/7/2022 15:43:48


package rs.ac.bg.etf.pp1.ast;

public class SingleOptArg extends OptArgListParser {

    private OptArgParser OptArgParser;

    public SingleOptArg (OptArgParser OptArgParser) {
        this.OptArgParser=OptArgParser;
        if(OptArgParser!=null) OptArgParser.setParent(this);
    }

    public OptArgParser getOptArgParser() {
        return OptArgParser;
    }

    public void setOptArgParser(OptArgParser OptArgParser) {
        this.OptArgParser=OptArgParser;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(OptArgParser!=null) OptArgParser.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OptArgParser!=null) OptArgParser.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OptArgParser!=null) OptArgParser.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SingleOptArg(\n");

        if(OptArgParser!=null)
            buffer.append(OptArgParser.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SingleOptArg]");
        return buffer.toString();
    }
}
