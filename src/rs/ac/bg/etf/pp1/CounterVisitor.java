package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;

public class CounterVisitor extends VisitorAdaptor {

	protected int count;
	
	public int getCount(){
		return count;
	}
	
	public static class FormParamCounter extends CounterVisitor{
	
		public void visit(Forma1 formParamDecl){
			count++; // ovaj counter sluzi za brojanje formalnih parmetra metode
		}
		
		public void visit(Forma2 formParamDecl){
			count++; // ovaj counter sluzi za brojanje formalnih parmetra metode
		}
		
		public void visit(OptArg1 formParamDecl){
			count++; // ovaj counter sluzi za brojanje formalnih parmetra metode
		}
		
		public void visit(OptArg2 formParamDecl){
			count++; // ovaj counter sluzi za brojanje formalnih parmetra metode
		}
		
		public void visit(OptArg3 formParamDecl){
			count++; // ovaj counter sluzi za brojanje formalnih parmetra metode
		}
	}
	
	public static class VarCounter extends CounterVisitor{
		
		public void visit(VarDeclMet1 varDecl){
			count++; // ovaj counter sluzi za brojanje lokalnih promenljivih metode
		}
		public void visit(VarDeclMet2 varDecl){
			count++; // ovaj counter sluzi za brojanje lokalnih promenljivih metode
		}
	}

	public static class AddOpVisitor extends CounterVisitor{
		
		public void visit(AddopPlus varDecl){
			count = 0; 
		}
		
		public void visit(AddopMinus varDecl){
			count = 1; 
		}
		
	}

}
