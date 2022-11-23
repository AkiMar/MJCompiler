// generated with ast extension for cup
// version 0.8
// 29/7/2022 15:43:48


package rs.ac.bg.etf.pp1.ast;

public class OptArgListParserForm extends FormPars {

    private OptArgListParser OptArgListParser;

    public OptArgListParserForm (OptArgListParser OptArgListParser) {
        this.OptArgListParser=OptArgListParser;
        if(OptArgListParser!=null) OptArgListParser.setParent(this);
    }

    public OptArgListParser getOptArgListParser() {
        return OptArgListParser;
    }

    public void setOptArgListParser(OptArgListParser OptArgListParser) {
        this.OptArgListParser=OptArgListParser;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(OptArgListParser!=null) OptArgListParser.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OptArgListParser!=null) OptArgListParser.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OptArgListParser!=null) OptArgListParser.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OptArgListParserForm(\n");

        if(OptArgListParser!=null)
            buffer.append(OptArgListParser.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OptArgListParserForm]");
        return buffer.toString();
    }
}
