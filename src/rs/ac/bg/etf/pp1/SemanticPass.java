package rs.ac.bg.etf.pp1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticPass extends VisitorAdaptor {

	FileWriter outFileWriter = null;
	FileWriter errorFileWriter = null;
	
	int printCallCount = 0;
	int varDeclCount = 0;
	Obj currentMethod = null;
	boolean returnFound = false;
	boolean errorDetected = false;
	int nVars;
	
	
	boolean mainVoid = false; // flag da li postoji void main metoda
	ArrayList<String> varList = new ArrayList<String>();  // za promenljive koje se vezuju redom zarezom cuvamo u listi dok ne mozemo da ih ubacimo
	Struct typeArg = null;
	int cntPar = 0; // broj parametra za funkciju prilikom njene definicije
	int doWhileScope = -1; // flag da znamo da li smo u do-while opsegu
	public static Struct boolTypeStruct = new Struct(5); // struct cvor za bool tip -- bice vrednosti 5
	public static Obj boolTypeObj = null; // Obj cvor za bool tip
	
	int[] cntArg = {0, 0, 0, 0}; // broj argumenata prilikom poziva f-je
	ArrayList<Struct>[] actualParList = (ArrayList<Struct>[]) new ArrayList[4];
	//ArrayList<Struct> actualParList = new ArrayList<Struct>(); // lista za proveru unetih argumenata da li su dobrog tipa sa parametrom f-je
	// za obicne parametre bez default vrednosti polje fpPos u Obj klasi datog parametra imace vrednost = -100
	// za deafult parametre , znaci parametri sa mogucom default vrednoscu poljefpPos u Obj klasi datog parametra imace vrednost = Vrednosti samog defaulta
	int cntFuncLevel = -1;
	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
		
		
		if( errorFileWriter != null) {
			try {
				errorFileWriter.write("ERROR -" + msg.toString() + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
		
		
		if( outFileWriter != null) {
			try {
				outFileWriter.write("INFO -" + msg.toString() + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}
	
	public void insertBooltype() { // poziva se odma posle Tab.init();
		Tab.currentScope.addToLocals(new Obj(Obj.Type, "bool", boolTypeStruct));
		//boolTypeObj = Tab.insert(Obj.Type, "bool", boolTypeStruct);
		boolTypeObj = Tab.find("bool");
		report_info("Ubacena bool type u TS kao obj i struct " + boolTypeObj + " " + boolTypeObj.getType(), null);
		
		// inicijalizacija actualParList
		for (int i = 0; i < actualParList.length; i++) {
			actualParList[i] = new ArrayList<Struct>();
		}
	}
	
	
	public void visit(VarDecl1 varDecl){ // globalna promenljiva tipa koji nije niz (int i char)
		Obj varNode = Tab.find(varDecl.getVarName());
		if( varNode != Tab.noObj) {
			report_error("Vec postoji globalna promenljiva "+ varDecl.getVarName(), varDecl);
			varList.clear();
			return;
		}
		Struct struct = varDecl.getType().struct;
		varNode = Tab.insert(Obj.Var, varDecl.getVarName(), struct);
		varDeclCount++;
		report_info("Deklarisana globalna promenljiva "+ varDecl.getVarName(), varDecl);
		// for za promenljive iz ,
		for (String name : varList) {
			
			varNode = Tab.find(name);
			if( varNode != Tab.noObj) {
				report_error("Vec postoji globalna promenljiva "+ name, varDecl);
				continue;
			}
			
			varNode = Tab.insert(Obj.Var, name, struct);
			report_info("Deklarisana globalna promenljiva "+ name, varDecl);
		}
		varList.clear();
	}
	
	public void visit(VarDecl2 varDecl){ // globalna promenljiva tipa niz
		
		Obj varNode = Tab.find(varDecl.getVarName());
		if( varNode != Tab.noObj) {
			report_error("Vec postoji globalna promenljiva "+ varDecl.getVarName(), varDecl);
			varList.clear();
			return;
		}
		
		varDeclCount++;
		Struct stArr = new Struct(Struct.Array, varDecl.getType().struct);
		varNode = Tab.insert(Obj.Var, varDecl.getVarName(), stArr);
		report_info("Deklarisana globaln promenljiva "+ varDecl.getVarName() + "[]", varDecl);
		// for za promenljive iz ,
		for (String name : varList) {
			
			varNode = Tab.find(name);
			if( varNode != Tab.noObj) {
				report_error("Vec postoji globalna promenljiva "+ name, varDecl);
				continue;
			}
			
			varNode = Tab.insert(Obj.Var, name, stArr);
			report_info("Deklarisana globalna promenljiva "+ name, varDecl);
		}
		varList.clear();
	}
	
	public void visit(VarDecl3 varDecl) {
		Obj varNode = Tab.find(varDecl.getVarName());
		if( varNode != Tab.noObj) {
			report_error("Vec postoji globalna promenljiva "+ varDecl.getVarName(), varDecl);
			varList.clear();
			return;
		}
		
		varDeclCount++;
		Struct stArr = new Struct(Struct.Array, varDecl.getType().struct);
		varNode = Tab.insert(Obj.Var, varDecl.getVarName(), stArr);
		report_info("Deklarisana globaln promenljiva "+ varDecl.getVarName() + "[]", varDecl);
		// for za promenljive iz ,
		for (String name : varList) {
			
			varNode = Tab.find(name);
			if( varNode != Tab.noObj) {
				report_error("Vec postoji globalna promenljiva "+ name, varDecl);
				continue;
			}
			
			varNode = Tab.insert(Obj.Var, name, stArr);
			report_info("Deklarisana globalna promenljiva "+ name, varDecl);
		}
		varList.clear();
	}
	
	public void visit(VarDeclarations1 varDecl) {
		varList.add(varDecl.getVarName());
	}
	
	public void visit(VarDeclarations2 varDecl) {
		varList.add(varDecl.getVarName());
	}
	
	public void visit(VarDeclMet1 varDecl) { // lokalna promenljiva
		
		Obj varNode = Tab.find(varDecl.getVarName());
		if( varNode != Tab.noObj && varNode.getLevel() == 1) {
			report_error("Vec postoji lokalna promenljiva "+ varDecl.getVarName(), varDecl);
			varList.clear();
			return;
		}
		
		report_info("Deklarisana lokalna promenljiva "+ varDecl.getVarName() , varDecl);
		varNode = Tab.insert(Obj.Var, varDecl.getVarName(), varDecl.getType().struct);
	}
	
	public void visit(VarDeclMet2 varDecl) { // lokalna NIZ promenljiva
		
		Obj varNode = Tab.find(varDecl.getVarName());
		if( varNode != Tab.noObj && varNode.getLevel() == 1) {
			report_error("Vec postoji lokalna promenljiva "+ varDecl.getVarName(), varDecl);
			varList.clear();
			return;
		}
		
		report_info("Deklarisana lokalna promenljiva "+ varDecl.getVarName() + "[]", varDecl);
		Struct stArr = new Struct(Struct.Array, varDecl.getType().struct);
		varNode = Tab.insert(Obj.Var, varDecl.getVarName(), stArr);
	}
	
	public void visit(ConstDeclNumber constDecl) { // globalna const promenljiva tipa int
		Obj varNode = Tab.find(constDecl.getConstName());
		if( varNode != Tab.noObj) {
			report_error("Vec postoji globalna promenljiva "+ constDecl.getConstName(), constDecl);
			varList.clear();
			return;
		}
		
		
		
		Struct struct = constDecl.getType().struct;
		
		if( struct != Tab.intType) {
			report_error("Los tip const ili losa vrednost "+ constDecl.getConstName(), constDecl);
			return;
		}
		
		varNode = Tab.insert(Obj.Con, constDecl.getConstName(), struct);
		varNode.setAdr(constDecl.getValue());
		
		varDeclCount++;
		report_info("Deklarisana globalna konstanta "+ constDecl.getConstName() + "=" + constDecl.getValue(), constDecl);
	}
	
	public void visit(ConstDeclChar constDecl) { // globalna const promenljiva tipa char
		Obj varNode = Tab.find(constDecl.getConstName());
		if( varNode != Tab.noObj) {
			report_error("Vec postoji globalna promenljiva "+ constDecl.getConstName(), constDecl);
			varList.clear();
			return;
		}
		
		
		
		Struct struct = constDecl.getType().struct;
		
		if( struct != Tab.charType) {
			report_error("Los tip const ili losa vrednost "+ constDecl.getConstName(), constDecl);
			return;
		}
		
		varNode = Tab.insert(Obj.Con, constDecl.getConstName(), struct);
		varNode.setAdr(constDecl.getValue());
		
		varDeclCount++;
		report_info("Deklarisana globalna konstanta "+ constDecl.getConstName() + "=" + constDecl.getValue(), constDecl); // ASCII kod char-a se zapamti
	}
	
	
	public void visit(Forma1 parDecl) { // unos parametra funkcije
		cntPar++;
		if( currentMethod.getName().equals("main")) {
    		mainVoid = false;
    	}
		
		Obj varNode = Tab.find(parDecl.getParName());
		if( varNode != Tab.noObj && varNode.getLevel() == 1) {
			report_error("Vec postoji lokalna promenljiva "+ parDecl.getParName(), parDecl);
			varList.clear();
			return;
		}
		
		report_info("Deklarisana lokalna/parametar promenljiva "+ parDecl.getParName() , parDecl);
		varNode = Tab.insert(Obj.Var, parDecl.getParName(), parDecl.getType().struct);
		
		varNode.setFpPos(-100); // oznaka za obican parametar
	}
	
	public void visit(Forma2 parDecl) { // unos paramerta funkcije tipa []
		cntPar++;
		if( currentMethod.getName().equals("main")) {
    		mainVoid = false;
    	}
		
		Obj varNode = Tab.find(parDecl.getParName());
		if( varNode != Tab.noObj && varNode.getLevel() == 1) {
			report_error("Vec postoji lokalna promenljiva "+ parDecl.getParName(), parDecl);
			varList.clear();
			return;
		}
		
		report_info("Deklarisana lokalna/parametar promenljiva "+ parDecl.getParName() + "[]", parDecl);
		Struct stArr = new Struct(Struct.Array, parDecl.getType().struct);
		varNode = Tab.insert(Obj.Var, parDecl.getParName(), stArr);
		
		varNode.setFpPos(-100); // oznaka za obican parametar
	}

	public void visit(OptArg1 parDecl) {
		cntPar++;
		if( currentMethod.getName().equals("main")) {
    		mainVoid = false;
    	}
		
		Obj varNode = Tab.find(parDecl.getNameOptArg());
		if( varNode != Tab.noObj && varNode.getLevel() == 1) {
			report_error("Vec postoji lokalna promenljiva "+ parDecl.getNameOptArg(), parDecl);
			varList.clear();
			return;
		}
		
		report_info("Deklarisana lokalna/parametar promenljiva "+ parDecl.getNameOptArg() , parDecl);
		varNode = Tab.insert(Obj.Var, parDecl.getNameOptArg(), parDecl.getType().struct);
		
		varNode.setFpPos(parDecl.getN1()); // oznaka za default parametar
	}
	
	public void visit(OptArg2 parDecl) {
		cntPar++;
		if( currentMethod.getName().equals("main")) {
    		mainVoid = false;
    	}
		
		Obj varNode = Tab.find(parDecl.getNameOptArg());
		if( varNode != Tab.noObj && varNode.getLevel() == 1) {
			report_error("Vec postoji lokalna promenljiva "+ parDecl.getNameOptArg(), parDecl);
			varList.clear();
			return;
		}
		
		report_info("Deklarisana lokalna/parametar promenljiva "+ parDecl.getNameOptArg() , parDecl);
		varNode = Tab.insert(Obj.Var, parDecl.getNameOptArg(), parDecl.getType().struct);
		
		varNode.setFpPos(parDecl.getC1()); // oznaka za default parametar
	}
	
	public void visit(OptArg3 parDecl) {
		cntPar++;
		if( currentMethod.getName().equals("main")) {
    		mainVoid = false;
    	}
		
		Obj varNode = Tab.find(parDecl.getNameOptArg());
		if( varNode != Tab.noObj && varNode.getLevel() == 1) {
			report_error("Vec postoji lokalna promenljiva "+ parDecl.getNameOptArg(), parDecl);
			varList.clear();
			return;
		}
		
		report_info("Deklarisana lokalna/parametar promenljiva "+ parDecl.getNameOptArg() , parDecl);
		varNode = Tab.insert(Obj.Var, parDecl.getNameOptArg(), parDecl.getType().struct);
		int flag = 0;
		if( parDecl.getB1().equals("true")) {
			flag = 1;
		}
		varNode.setFpPos(flag); // oznaka za default parametar
	}
	
    
    
    public void visit(ProgName progName){
    	progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
    	Tab.openScope();
    }
    
    public void visit(Program program){
    	nVars = Tab.currentScope.getnVars();
    	Tab.chainLocalSymbols(program.getProgName().obj);
    	Tab.closeScope();
    	if( !mainVoid ) {
    		report_error("Niste definisali main funkciju sa void povratnim tipom i bez parametra.", null);
    	}
    }
    
    public void visit(Type type){
    	Obj typeNode = Tab.find(type.getTypeName());
    	if(typeNode == Tab.noObj){
    		report_error("Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola! ", null);
    		type.struct = Tab.noType;
    	}else{
    		if(Obj.Type == typeNode.getKind()){
    			type.struct = typeNode.getType();
    		}else{
    			report_error("Greska: Ime " + type.getTypeName() + " ne predstavlja tip!", type);
    			type.struct = Tab.noType;
    		}
    	}
    }
    
    public void visit(MethodTypeName1 methodTypeName){ // za funkcije sa povratin tipom
    	cntPar = 0;
    	Obj varNode = Tab.find(methodTypeName.getMethName());
		if( varNode != Tab.noObj) {
			report_error("Vec postoji fukncija sa tim imenom "+ methodTypeName.getMethName(), methodTypeName);
			Tab.openScope();
			return;
		}
    	
    	currentMethod = Tab.insert(Obj.Meth, methodTypeName.getMethName(), methodTypeName.getType().struct);
    	methodTypeName.obj = currentMethod;
    	Tab.openScope();
		report_info("Obradjuje se funkcija " + methodTypeName.getMethName(), methodTypeName);
    }
    
    public void visit(MethodTypeName2 methodTypeName){ // za funkcije sa void povratnim tipom
    	cntPar = 0;
    	Obj varNode = Tab.find(methodTypeName.getMethName());
		if( varNode != Tab.noObj) {
			report_error("Vec postoji fukncija sa tim imenom "+ methodTypeName.getMethName(), methodTypeName);
			Tab.openScope();
			return;
		}
    	
    	currentMethod = Tab.insert(Obj.Meth, methodTypeName.getMethName(), Tab.noType);
    	methodTypeName.obj = currentMethod;
    	Tab.openScope();
		report_info("Obradjuje se funkcija " + methodTypeName.getMethName(), methodTypeName);
		
		if( methodTypeName.getMethName().equals("main")) {
			mainVoid = true;
		}
		
    }
    
    public void visit(MethodDecl1 methodDecl){
    	if( currentMethod == null) {
    		Tab.closeScope();
    		return;
    	}
    		
    	if(!returnFound && currentMethod.getType() != Tab.noType){
			report_error("Semanticka greska na liniji " + methodDecl.getLine() + ": funkcija " + currentMethod.getName() + " nema return iskaz!", null);
    	}
    	currentMethod.setLevel(cntPar); // postavlja broj parametra/formalnih argumenta f-je
    	cntPar = 0;
    	Tab.chainLocalSymbols(currentMethod);
    	Tab.closeScope();
    	report_info("KRAJ Funkcije: " + currentMethod.getName(), null);
    	
    	returnFound = false;
    	currentMethod = null;

    }
    
    public void visit(DesignatorObican designator){
    	Obj obj = Tab.find(designator.getName());
    	if(obj == Tab.noObj){
			report_error("Greska na liniji " + designator.getLine()+ " : ime "+designator.getName()+" nije deklarisano! ", null);
			designator.obj = obj;
			return;
    	}
    	if( obj.getKind() != Obj.Meth) {
    		report_info("Koristi se promenljiva: " + designator.getName() + " na liniji " + designator.getLine() + " OBJ cvor: " + obj, null);
    	}else {
    		cntFuncLevel++;
    	}
    	designator.obj = obj; // sacuvamo u designator neterminalu tip obj koji je promenljiva/funkcija da bi kasnije mogli da proverimo
    }
    
    public void visit(DesignatorArr desArr) {
    	Obj obj = Tab.find(desArr.getName());
    	if(obj == Tab.noObj){
			report_error("Greska na liniji " + desArr.getLine()+ " : ime "+desArr.getName()+" nije deklarisano! ", null);
    	}
    	int kind = -1;
    	Struct stIzraza = desArr.getExpr().struct;
    	try {
    	kind = obj.getType().getElemType().getKind();
    	}
    	catch(Exception e) {
    		report_error("Greska Promenljiva " + desArr.getName() + " nije niz ", desArr);
    		desArr.obj = Tab.noObj;
    		return;
    	}
    	// sacuvamo u designator neterminalu tip obj koji je element da bi kasnije mogli da proverimo
    	if( kind == 1) { // int
    		desArr.obj = Tab.find("int"); 
    	}else if( kind == 2) { // char
    		desArr.obj = Tab.find("char"); 
    	}else {
    		desArr.obj = Tab.noObj;
    	}
    	
    	if( stIzraza != Tab.intType) {
    		report_error("Greska izraz u [] nije tipa int" , desArr);
    	}
    	desArr.setArrObj(obj);
    	report_info("Pronadjen pristup nizu " + desArr.getName() + " na liniji " + desArr.getLine() + " OBJ cvor: " + obj, null);
    }
    
    public void visit(DesignatorMat desArr) {
    	Obj obj = Tab.find(desArr.getName());
    	if(obj == Tab.noObj){
			report_error("Greska na liniji " + desArr.getLine()+ " : ime "+desArr.getName()+" nije deklarisano! ", null);
    	}
    	int kind = -1;
    	Struct stIzraza = desArr.getExpr().struct;
    	try {
    	kind = obj.getType().getElemType().getKind();
    	}
    	catch(Exception e) {
    		report_error("Greska Promenljiva " + desArr.getName() + " nije niz ", desArr);
    		desArr.obj = Tab.noObj;
    		return;
    	}
    	// sacuvamo u designator neterminalu tip obj koji je element da bi kasnije mogli da proverimo
    	if( kind == 1) { // int
    		desArr.obj = Tab.find("int"); 
    	}else if( kind == 2) { // char
    		desArr.obj = Tab.find("char"); 
    	}else {
    		desArr.obj = Tab.noObj;
    	}
    	
    	if( stIzraza != Tab.intType) {
    		report_error("Greska izraz u [] nije tipa int" , desArr);
    	}
    	desArr.setArrObj(obj);
    	report_info("Pronadjen pristup nizu " + desArr.getName() + " na liniji " + desArr.getLine() + " OBJ cvor: " + obj, null);
    }
    
    
    public void visit(FuncCall funcCall){
    	
    	Obj func = funcCall.getDesignator().obj;
    	String nameCall = func.getName();
    	
    	if(Obj.Meth == func.getKind()){
    		
    		report_info("Pronadjen poziv funkcije " + func.getName() + " na liniji " + funcCall.getLine() + " OBJ cvor: " + func, null);
			funcCall.struct = func.getType();  // dodela tipa za proveru kod Assignmenta :)
			//System.out.println(funcCall.struct.getKind());
			
			int cntPar = func.getLevel();
			if( cntArg[cntFuncLevel] > cntPar) {
				report_error("Greska na liniji " + funcCall.getLine()+" : ime " + func.getName() + " previse argumenata", null);
				cntArg[cntFuncLevel] = 0;
				actualParList[cntFuncLevel].clear();
				cntFuncLevel--;
				return;
			}
			
			// provera len-a zasebno
			if( nameCall.equals("len")) {
    			if( cntArg[cntFuncLevel] != 1 || !typeArg.isRefType()) {
    				report_error("Greska na liniji " + funcCall.getLine()+" : ime " + func.getName() + " losi argumenti", null);
    				cntArg[cntFuncLevel] = 0;
    				actualParList[cntFuncLevel].clear();
    				cntFuncLevel--;
    				return;
    			}else {
    				cntArg[cntFuncLevel] = 0;
    				actualParList[cntFuncLevel].clear();
    				cntFuncLevel--;
    				return;
    			}
    		}
			
			
			Collection<Obj> coll = func.getLocalSymbols();
			List<Obj> listPar;
			if (coll instanceof List)
				listPar = (List)coll;
			else
				listPar = new ArrayList<Obj>(coll);
			
			for (int i = 0; i < cntArg[cntFuncLevel]; i++) {
				Obj par = listPar.get(i);
				Struct st = actualParList[cntFuncLevel].get(i);
				if( !par.getType().equals(st)) {
					System.out.println(st.getKind() + " " + par.getName());
					report_error("Greska na liniji " + funcCall.getLine()+" : ime " + func.getName() + " los tip argumenta", null);
					cntArg[cntFuncLevel] = 0;
					actualParList[cntFuncLevel].clear();
					cntFuncLevel--;
					return;
				}
			}
			if( cntArg[cntFuncLevel] < cntPar) {
				Obj par = listPar.get(cntArg[cntFuncLevel]);
				if( par.getFpPos() == -100) {
					report_error("Greska na liniji " + funcCall.getLine()+" : ime " + func.getName() + " zaboravili ste uneti argumente za sve obicne parametre koji nisu default", null);
					cntArg[cntFuncLevel] = 0;
					actualParList[cntFuncLevel].clear();
					cntFuncLevel--;
					return;
				}
			}
			
    		//provera za universe funkcije
    		/*if( nameCall.equals("chr")) {
    			if( cntArg != 1 || typeArg != Tab.intType) {
    				report_error("Greska na liniji " + funcCall.getLine()+" : ime " + func.getName() + " losi argumenti", null);
    				cntArg = 0;
    				actualParList.clear();
    				return;
    			}
    		}
    		
    		if( nameCall.equals("ord")) {
    			if( cntArg != 1 || typeArg != Tab.charType) {
    				report_error("Greska na liniji " + funcCall.getLine()+" : ime " + func.getName() + " losi argumenti", null);
    				cntArg = 0;
    				actualParList.clear();
    				return;
    			}
    		}
    		
    		if( nameCall.equals("len")) {
    			if( cntArg != 1 || !typeArg.isRefType()) {
    				report_error("Greska na liniji " + funcCall.getLine()+" : ime " + func.getName() + " losi argumenti", null);
    				cntArg = 0;
    				actualParList.clear();
    				return;
    			}
    		}
    		*/
    	}else{
			report_error("Greska na liniji " + funcCall.getLine()+" : ime " + nameCall + " nije funkcija!", null);
			funcCall.struct = Tab.noType;
    	}
    	cntArg[cntFuncLevel] = 0;
    	actualParList[cntFuncLevel].clear();
    	cntFuncLevel--;
    }
    
    public void visit(OnlyFuncCall funcCall){
    	
    	Obj func = funcCall.getDesignator().obj;
    	String nameCall = func.getName();
    	
    	if(Obj.Meth == func.getKind()){
    		
    		report_info("Pronadjen poziv funkcije " + func.getName() + " na liniji " + funcCall.getLine() + " OBJ cvor: " + func, null);		
			
			int cntPar = func.getLevel();
			if( cntArg[cntFuncLevel] > cntPar) {
				report_error("Greska na liniji " + funcCall.getLine()+" : ime " + func.getName() + " previse argumenata", null);
				cntArg[cntFuncLevel] = 0;
				actualParList[cntFuncLevel].clear();
				cntFuncLevel--;
				return;
			}
			
			// provera len-a zasebno
			if( nameCall.equals("len")) {
    			if( cntArg[cntFuncLevel] != 1 || !typeArg.isRefType()) {
    				report_error("Greska na liniji " + funcCall.getLine()+" : ime " + func.getName() + " losi argumenti", null);
    				cntArg[cntFuncLevel] = 0;
    				actualParList[cntFuncLevel].clear();
    				cntFuncLevel--;
    				return;
    			}else {
    				cntArg[cntFuncLevel] = 0;
    				actualParList[cntFuncLevel].clear();
    				cntFuncLevel--;
    				return;
    			}
    		}
			
			
			Collection<Obj> coll = func.getLocalSymbols();
			List<Obj> listPar;
			if (coll instanceof List)
				listPar = (List)coll;
			else
				listPar = new ArrayList<Obj>(coll);
			
			for (int i = 0; i < cntArg[cntFuncLevel]; i++) {
				Obj par = listPar.get(i);
				Struct st = actualParList[cntFuncLevel].get(i);
				if( !par.getType().equals(st)) {
					report_error("Greska na liniji " + funcCall.getLine()+" : ime " + func.getName() + " los tip argumenta", null);
					cntArg[cntFuncLevel] = 0;
					actualParList[cntFuncLevel].clear();
					cntFuncLevel--;
					return;
				}
			}
			if( cntArg[cntFuncLevel] < cntPar) {
				Obj par = listPar.get(cntArg[cntFuncLevel]);
				if( par.getFpPos() != 1) {
					report_error("Greska na liniji " + funcCall.getLine()+" : ime " + func.getName() + " zaboravili ste uneti argumente za sve obicne parametre koji nisu default", null);
					cntArg[cntFuncLevel] = 0;
					actualParList[cntFuncLevel].clear();
					cntFuncLevel--;
					return;
				}
			}
			
    		//provera za universe funkcije
    		/*if( nameCall.equals("chr")) {
    			if( cntArg != 1 || typeArg != Tab.intType) {
    				report_error("Greska na liniji " + funcCall.getLine()+" : ime " + func.getName() + " losi argumenti", null);
    				cntArg = 0;
    				actualParList.clear();
    				return;
    			}
    		}
    		
    		if( nameCall.equals("ord")) {
    			if( cntArg != 1 || typeArg != Tab.charType) {
    				report_error("Greska na liniji " + funcCall.getLine()+" : ime " + func.getName() + " losi argumenti", null);
    				cntArg = 0;
    				actualParList.clear();
    				return;
    			}
    		}
    		
    		if( nameCall.equals("len")) {
    			if( cntArg != 1 || !typeArg.isRefType()) {
    				report_error("Greska na liniji " + funcCall.getLine()+" : ime " + func.getName() + " losi argumenti", null);
    				cntArg = 0;
    				actualParList.clear();
    				return;
    			}
    		}
    		*/
    	}else{
			report_error("Greska na liniji " + funcCall.getLine()+" : ime " + nameCall + " nije funkcija!", null);
    	}
    	cntArg[cntFuncLevel] = 0;
    	actualParList[cntFuncLevel].clear();
    	cntFuncLevel--;
    }
    
    
    public void visit(ActualParams actPar) {
    	Struct st = actPar.getExpr().struct;
    	actualParList[cntFuncLevel].add(0,st);
    	cntArg[cntFuncLevel]++;
    }
    
    public void visit(ActualParam actPar) {
    	typeArg = actPar.getExpr().struct;  // za universe f-je se koristi len, chr, ord
    	Struct st = actPar.getExpr().struct;
    	actualParList[cntFuncLevel].add(0,st);
    	cntArg[cntFuncLevel]++;
    }
    
    public void visit(Term term){
    	term.struct = term.getTermDodatak().struct;
    }
    
    public void visit(MulopFactor fac) {
    	if( fac.getTermDodatak().struct != Tab.intType || fac.getFactor().struct != Tab.intType ) {
    		report_error("Greska na liniji "+ fac.getLine()+" : nekompatibilni tip za mnozenje.", null);
    	}
    	fac.struct = Tab.intType;
    }
    
    public void visit(MinusFactor fac) {
    	if( fac.getFactor().struct != Tab.intType) {
    		report_error("Greska na liniji "+ fac.getLine()+" : nekompatibilni tip za dodelu minus znaka.", null);
    	}
    	fac.struct = Tab.intType;
    }
    
    public void visit(SimpleFactor fac) {
    	fac.struct = fac.getFactor().struct;
    }
    
    public void visit(TermExpr termExpr){
    	termExpr.struct = termExpr.getTerm().struct;
    }
    
    public void visit(AddExpr addExpr){
    	Struct te = addExpr.getExpr().struct;
    	Struct t = addExpr.getTerm().struct;
    	if(te.equals(t) && te == Tab.intType){
    		addExpr.struct = te;
    	}else{
			report_error("Greska na liniji "+ addExpr.getLine()+" : nekompatibilni tipovi u izrazu za sabiranje/oduzimanje.", null);
			addExpr.struct = Tab.noType;
    	}
    }
    
    public void visit(FnumConst cnst){
    	cnst.struct = Tab.intType;
    }
    
    public void visit(FcharConst cnst){
    	cnst.struct = Tab.charType;
    }
    
    public void visit(FboolConst cnst){
    	cnst.struct = boolTypeStruct;//moras ubaciti bool type pre pocetka prolaza kroz stablo
    }
    
    public void visit(FnewExpr newExpr) {
    	Struct st = newExpr.getFactorOpcija().struct;
    	if( st != Tab.intType) {
    		report_error("Greska tip u zagradama [] nije int", newExpr);
    	}
    	newExpr.struct = new Struct(Struct.Array, newExpr.getType().struct);
    }
    
    public void visit(FactorOpcija1 facOpc) {
    	facOpc.struct = facOpc.getExpr().struct;
    }
    
    public void visit(FactorOpcija2 facOpc) {
    	if( facOpc.getExpr1().struct == facOpc.getExpr().struct){
    		facOpc.struct = facOpc.getExpr().struct;
    	}else {
    		facOpc.struct = null;
    	}
    }
    
    public void visit(Var var){
    	var.struct = var.getDesignator().obj.getType();
    }
    
    public void visit(ReturnExpr returnExpr){ // provera return tipa
    	if( currentMethod == null) {
    		return;
    	}
    	returnFound = true;
    	Struct currMethType = currentMethod.getType();
    	if(!currMethType.compatibleWith(returnExpr.getExpr().struct)){
			report_error("Greska na liniji " + returnExpr.getLine() + " : " + "tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije " + currentMethod.getName(), null);
    	}
    }
    
    public void visit(ReturnNoExpr returnExpr){ // provera return tipa
    	if( currentMethod == null) {
    		return;
    	}
    	returnFound = true;
    	Struct currMethType = currentMethod.getType();
    	if(currMethType != Tab.noType){
			report_error("Greska na liniji " + returnExpr.getLine() + " : " + " return nema povratnu vrednost funkcije " + currentMethod.getName(), null);
    	}
    }
    
    public void visit(Assignment assignment){
    	if(!assignment.getExpr().struct.assignableTo(assignment.getDesignator().obj.getType()))
    		report_error("Greska na liniji " + assignment.getLine() + " : " + "nekompatibilni tipovi u dodeli vrednosti! ", null);
    }
    
    public void visit(CondFact1 condFact) { // provera da li je Expr bool tipa
    	Struct st = condFact.getExpr().struct;
    	if( st == boolTypeStruct || st.getKind() == 5 || st == Tab.intType) {
    		return;
    	}else {
    		report_error("Greska izraz espr nije bool tipa", condFact);
    	}
    }
    
    public void visit(CondFact2 condFact) {
    	Struct te = condFact.getExpr().struct;
    	Struct t = condFact.getExpr1().struct;
    	Struct relop = condFact.getRelop().struct;
    	if(te.equals(t)){  // moraju biti istog tipa one koje ulaze u izraz
    		if( te.getKind() == Struct.Array) {
    			if( relop.getKind() > 12 ) {
    				report_error("Greska na liniji "+ condFact.getLine()+" : nizovi se mogu uporedjivati samo sa == ili !=.", null);
    			}
    		}
    		return;
    	}else{
			report_error("Greska na liniji "+ condFact.getLine()+" : nekompatibilni tipovi u izrazu za proveru logicnosti.", null);
    	}
    }
    
    public void visit(RelopIsto relop) {
    	relop.struct = new Struct(11); // 11 -oznaka za ==
    }
    
    public void visit(RelopRaz relop) {
    	relop.struct = new Struct(12); // 12 -oznaka za !=
    }
    
    public void visit(RelopVece relop) {
    	relop.struct = new Struct(13); // 13 -oznaka za >
    }
    
    public void visit(RelopVeceJed relop) {
    	relop.struct = new Struct(14); // 14 -oznaka za >=
    }
    
    public void visit(RelopManje relop) {
    	relop.struct = new Struct(15); // 15 -oznaka za <
    }
    
    public void visit(RelopManjeJed relop) {
    	relop.struct = new Struct(16); // 16 -oznaka za <=
    }
    
    public void visit(DesStatmInc desDecl) {
    	Struct st = desDecl.getDesignator().obj.getType();
    	if( st != Tab.intType) {
    		report_error("Greska los tip promenljive za ++ ", desDecl);
    	}
    }
    
    public void visit(DesStatmDec desDecl) {
    	Struct st = desDecl.getDesignator().obj.getType();
    	if( st != Tab.intType) {
    		report_error("Greska los tip promenljive za -- ", desDecl);
    	}
    }
    
    public void visit(DoStart1 doStart) {
    	doWhileScope++;
    }
    
    public void visit(DoWhile doWhile) {
    	doWhileScope--;
    }
    
    public void visit(Break breakDecl) {
    	if( doWhileScope < 0 ) {
    		report_error("Greska break je van do-while opsega ", breakDecl);
    	}
    }
    
    public void visit(Continue continueDecl) {
    	if( doWhileScope < 0 ) {
    		report_error("Greska continue je van do-while opsega ", continueDecl);
    	}
    }
    
    public void visit(ReadSingStm readDecl) {
    	report_info("Poziv funkcije read", readDecl);
    	Struct st = readDecl.getDesignator().obj.getType();
    	if( st != Tab.intType && st!= Tab.charType) {
    		report_error("Greska los argument za read fukciju", readDecl);
    	}
    }
    
    public void visit(PrintStmt printDecl) {
    	printCallCount++;
    	report_info("Poziv funkcije print", printDecl);
    	Struct st = printDecl.getExpr().struct;
    	if( st != Tab.intType && st!= Tab.charType) {
    		report_error("Greska los argument za print fukciju", printDecl);
    	}
    }
    
    public boolean passed(){
    	return !errorDetected;
    }
    
}
