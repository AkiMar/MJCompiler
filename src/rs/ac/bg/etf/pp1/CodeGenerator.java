package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rs.ac.bg.etf.pp1.CounterVisitor.*;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {

	private int mainPc;  // ovo nam sluzi samo da sacuvamo adresu gde je main f-ja generisana
	
	public int getMainPc(){
		return mainPc;
	}
	
	Obj arrCon = null;
	boolean flagArrStore = false;
	int[] cntPar = {0,0,0,0};
	int cntFuncCallLevel = -1;
	
	int matCon = 0;
	
	int doPCadr[] = new int[10]; // adresa do petlje gde pocinje
	int doFlag = -1; // flag da znamo kod Conditiona da li smo u do petlji -1 oznacava da nismo a sve od 0 i vece oznacava da jesmo i samo ugnjezdjenje
	
	String relopFlag = "";
	
	List<Integer> doWhileListAdr = new ArrayList<Integer>();
	ArrayList<Integer>[] doBreakAdr = (ArrayList<Integer>[]) new ArrayList[8];
	
	ArrayList<Integer>[] ifPcAdr = (ArrayList<Integer>[]) new ArrayList[8];  // adrese za fixup u if-u
	int ifLevel = -1; // promenljiva za pokazivanje ugnjezdenja if-a
	
	Obj readObj = null;
	
	Map<Obj, Integer> listNiz = new HashMap<>();
	
	boolean returnFlag = false;
	
	public void initPredeklarisaneFje() {
		// chr metoda
		Obj objChr = Tab.find("chr");
		objChr.setAdr(Code.pc);
		Code.put(Code.enter); 
		Code.put(1);
		Code.put(1);
		// stavljamo parametar na EpxrStack
		Code.put(Code.load_n + 0);
		Obj con = new Obj( Obj.Con, "$", Tab.intType, 48, 0);
		Code.load(con);
		Code.put(Code.add);
		
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		
		// ord metoda
		Obj objOrd = Tab.find("ord");
		objOrd.setAdr(Code.pc);
		Code.put(Code.enter); 
		Code.put(1);
		Code.put(1);
		// stavljamo parametar na EpxrStack
		Code.put(Code.load_n + 0);
		con = new Obj( Obj.Con, "$", Tab.intType, 48, 0);
		Code.load(con);
		Code.put(Code.sub);
		
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		// len metoda
		Obj objLen = Tab.find("len");
		objLen.setAdr(Code.pc);
		Code.put(Code.enter); 
		Code.put(1);
		Code.put(1);
		// stavljamo parametar na EpxrStack
		Code.put(Code.load_n + 0);
		Code.put(Code.arraylength);
		
		Code.put(Code.exit);
		Code.put(Code.return_);
		
	}
	
	public void visit(PrintStmt printStmt){
		if(printStmt.getExpr().struct == Tab.intType){
			Code.loadConst(5);  //vrednost koju treba ispisati se vec nalazi na Expr Stack jer ce u delu Expr cvora kad se izracuna Expr nalazice se data vrednost na steku
			Code.put(Code.print); // na nama je da stavimo sad sirinu operanda da bi print znao koji ispis da uradi
		}else{ // i ofc posle toga stavljamo sam bytekod za print funkciju
			Code.loadConst(1);
			Code.put(Code.bprint);
		}
	}
	
	public void visit(DesStatmHash desStmHash) {
		Designator des = desStmHash.getDesignator();
		Obj objArr = des.obj;
		Integer br = desStmHash.getN1();
		listNiz.put(objArr, br);
		System.out.println("HASH Designator " + objArr.getName());
	}
	
	
	public void visit(FnumConst cnst){
		Obj con = new Obj( Obj.Con, "$", cnst.struct, cnst.getN1(), 0);
		System.out.println("CONST " + cnst.getN1());
		// ovo iznad je kreiranje Obj da bi mogli uraditi neposredno ubacivanje na ExprStack
		SyntaxNode parent = cnst.getParent();
		
		while( !(parent instanceof Program) ) { // ako je na levoj strani pristup niza da ga ne upise
			if( parent instanceof DesignatorArr && parent.getParent() instanceof Assignment) {
				arrCon = con;
				return;
			}
			if( parent instanceof DesignatorMat && parent.getParent() instanceof Assignment) {
				arrCon = con;
				return;
			}
			parent = parent.getParent();
		}
		parent = cnst.getParent();
		// ako je sa desne strane u [] pristupa niza da se ne ispise
		while( !(parent instanceof Program) ) {
			if( parent instanceof DesignatorArr) {
				arrCon = con;
				return;
			}
			if( parent instanceof DesignatorMat) {
				arrCon = con;
				return;
			}
			parent = parent.getParent();
		}
		
		Code.load(con); // ubacivanje const na ExprStack
	}
	
	public void visit(FcharConst cnst){
		Obj con = new Obj( Obj.Con, "$", cnst.struct, cnst.getC1(), 0);
		
		// ovo iznad je kreiranje Obj da bi mogli uraditi neposredno ubacivanje na ExprStack
		
		Code.load(con); // ubacivanje const na ExprStack
	}
	
	public void visit(FboolConst cnst){
		int flag = 0;
		if( cnst.getB1().equals("true")) {
			flag = 1;
		}
		
		Obj con = new Obj( Obj.Con, "$", Tab.intType, flag, 0);
		
		// ovo iznad je kreiranje Obj da bi mogli uraditi neposredno ubacivanje na ExprStack
		
		Code.load(con); // ubacivanje const na ExprStack
	}
	
	
	public void visit(MethodTypeName1 methodTypeName){ // ovo je definicija tela metode u Code memoriji
		
		if("main".equalsIgnoreCase(methodTypeName.getMethName())){
			mainPc = Code.pc;
		}
		methodTypeName.obj.setAdr(Code.pc);
		// Collect arguments and local variables  moramo pre nego sto pozovemo metodu moramo na ExprStack da stavimo sve argumente date metode 
		SyntaxNode methodNode = methodTypeName.getParent();
		
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		
		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);
		
		// Generate the entry
		Code.put(Code.enter);  // svaka instrukcija pokrece se sa enter instrukcijom ona generise AZ date instrukcije
		Code.put(fpCnt.getCount()); // moramo za enter instrkuciju na samom byteKodu Code delu mikroJave da stoji i broj paramtetra i lokalnih promenljivih
		Code.put(fpCnt.getCount() + varCnt.getCount()); // to su ustvari operandi instrukcije i zato je enter instrukcija 3 byte velicina i
	
		returnFlag = false;
	}
	
	public void visit(MethodTypeName2 methodTypeName){ // ovo je definicija tela metode u Code memoriji
		
		if("main".equalsIgnoreCase(methodTypeName.getMethName())){
			mainPc = Code.pc;
		}
		methodTypeName.obj.setAdr(Code.pc);
		// Collect arguments and local variables  moramo pre nego sto pozovemo metodu moramo na ExprStack da stavimo sve argumente date metode 
		SyntaxNode methodNode = methodTypeName.getParent();
		
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		
		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);
		
		// Generate the entry
		Code.put(Code.enter);  // svaka instrukcija pokrece se sa enter instrukcijom ona generise AZ date instrukcije
		Code.put(fpCnt.getCount()); // moramo za enter instrkuciju na samom byteKodu Code delu mikroJave da stoji i broj paramtetra i lokalnih promenljivih
		Code.put(fpCnt.getCount() + varCnt.getCount()); // to su ustvari operandi instrukcije i zato je enter instrukcija 3 byte velicina i
	
		returnFlag = false;
	}
	
	
	public void visit(MethodDecl1 methodDecl){ // ofc kad dodjemo do kraja metode definicije i vratimo se na ovaj glavni cvor onda moramo  i exit i ret instrukicju da stavimo u kodu metode
		if( returnFlag == false) {
			Code.put(Code.exit);
			Code.put(Code.return_);
		}
		returnFlag = false;
	}
	
	public void visit(Assignment assignment){
		// na ExprStacku cemo uzeti da se nalazi vrednost koja treba da se doda datoj promenljivoj
		// i onda samo pozovemo Code.store koji ce izgenerisati bytekod za dodelu vrednosti sa vrha steka na datu promenljivu
		if( flagArrStore ) {
			Code.put(Code.astore);
			flagArrStore = false;
		}else {
			Code.store(assignment.getDesignator().obj);
		}
	}
	
	public void visit(Designator designator){
		SyntaxNode parent = designator.getParent();
		// ideja je da se proveri kom od onij smena/cvorova pripada ovaj Designator pa ako je on u onom cvoru/smeni Var koja predstavlja
		// samo promenljivu onda cemo izgenerisati bytekod za njeno ucitavanje na ExprStack a sam poziv Code.load se bavi time da li je glabalna ili lokalna promenljiva za generisanje koda
		if(Assignment.class != parent.getClass() && FuncCall.class != parent.getClass() && OnlyFuncCall.class != parent.getClass()){
			Code.load(designator.obj);
		}
	}
	
	public void visit(DesignatorObican designator){
		SyntaxNode parent = designator.getParent();
		
		while( !(parent instanceof Program) ) { // ako je na levoj strani pristup niza da ga ne upise
			if( parent instanceof DesignatorArr && parent.getParent() instanceof Assignment) {
				arrCon = designator.obj;
				return;
			}
			if( parent instanceof DesignatorMat && parent.getParent() instanceof Assignment) {
				arrCon = designator.obj;
				return;
			}
			parent = parent.getParent();
		}
		
		parent = designator.getParent();
		 // ako je u pitanju poziv f-je povecati cntFuncCallLevel
		if( parent instanceof FuncCall) {
			cntFuncCallLevel++;
			return;
		}
		parent = parent.getParent();
		
		
		parent = designator.getParent();
		while( !(parent instanceof Program) ) { // ako je u pitanju read f-ja
			if( parent instanceof ReadSingStm) {
				System.out.println("READ OBJ obican");
				readObj = designator.obj;
				return;
			}
			parent = parent.getParent();
		}
		
		parent = designator.getParent();
		while( !(parent instanceof Program) ) { // ako je u okviru niza da se ne upisuje na ExprStack
			if( parent instanceof DesignatorArr) {
				arrCon = designator.obj;
				return;
			}
			if( parent instanceof DesignatorMat) {
				arrCon = designator.obj;
				return;
			}
			parent = parent.getParent();
		}
		
		
		parent = designator.getParent();
		if( parent instanceof Assignment) { // ako se u njega upisuje da ga ne stavlja na ExprStack
			return;
		}
    	Obj obj = designator.obj;
    	if( obj.getKind() != Obj.Meth) {
    		// stavljamo promenljivu na ExprStack
    		Code.load(designator.obj);
    	}
    	
    }
	
	
	public void visit(DesignatorArr desArr) {
		System.out.println("DES ARR");
		Obj obj = desArr.getArrObj();
		
		SyntaxNode parent = desArr.getParent();
		if( parent instanceof Assignment) { // da se za niz sa leve strane doda se adresa i index na ExprStack za astore
			Code.load(obj);
			Code.load(arrCon);
			flagArrStore = true;
			return;
		}
		
		// ako je u pitanju read f-ja
		parent = desArr.getParent();
		while( !(parent instanceof Program) ) { 
			if( parent instanceof ReadSingStm) {
				Code.load(obj);
				Code.load(arrCon);
				System.out.println("READ OBJ Array");
				readObj = desArr.getArrObj();
				return;
			}
			parent = parent.getParent();
		}
		
		// provera da li je u inc ili dec zapisu
		parent = desArr.getParent();
		while (!(parent instanceof Program)) {
			if(parent instanceof DesStatmInc || parent instanceof DesStatmDec) {
				Code.load(obj);
				Code.load(arrCon);
				System.out.println("ARR u INC/DEC");
				break;
			}
			parent = parent.getParent();
		}
		
		// sa desne strane je
		Code.load(obj);
		Code.load(arrCon);
		Code.put(Code.aload);
		return;
	}
	
	public void visit(DesignatorMat desArr) {
		System.out.println("DES MAT");
		Obj obj = desArr.getArrObj();
		
		SyntaxNode parent = desArr.getParent();
		if( parent instanceof Assignment) { // da se za niz sa leve strane doda se adresa i index na ExprStack za astore
			Code.load(obj);
			Code.load(arrCon);
			flagArrStore = true;
			return;
		}
		
		// ako je u pitanju read f-ja
		parent = desArr.getParent();
		while( !(parent instanceof Program) ) { 
			if( parent instanceof ReadSingStm) {
				Code.load(obj);
				Code.load(arrCon);
				System.out.println("READ OBJ Array");
				readObj = desArr.getArrObj();
				return;
			}
			parent = parent.getParent();
		}
		
		// provera da li je u inc ili dec zapisu
		parent = desArr.getParent();
		while (!(parent instanceof Program)) {
			if(parent instanceof DesStatmInc || parent instanceof DesStatmDec) {
				Code.load(obj);
				Code.load(arrCon);
				System.out.println("ARR u INC/DEC");
				break;
			}
			parent = parent.getParent();
		}
		
		// sa desne strane je
		Code.load(obj);
		Code.load(arrCon);
		Code.put(Code.aload);
		return;
	}
	
	
	public void visit(FuncCall funcCall){ // generisanje ByteKoda za poziv funkcija koje nemaju argumente i ovaj poziv je za funkciju koja se zove u izrazima odnosno Expr
		
		Obj functionObj = funcCall.getDesignator().obj;
		
		// provera da li ima formalnih argumenata
		int cntArg = functionObj.getLevel();
		
		Collection<Obj> coll = functionObj.getLocalSymbols();
		List<Obj> listPar;
		if (coll instanceof List)
			listPar = (List)coll;
		else
			listPar = new ArrayList<Obj>(coll);
		
		for (int i = cntPar[cntFuncCallLevel]; i < cntArg; i++) {
			Obj parFor = listPar.get(i);
			Obj con = new Obj( Obj.Con, "$", Tab.intType, parFor.getFpPos(), 0);
			System.out.println("Default par:" + parFor.getFpPos());
			Code.load(con);
		}
		
		int offset = functionObj.getAdr() - Code.pc; // dohvatamo gde nam se nalazi fukcija odnosno njen offset u odnosu na trenutno gde je
		
		Code.put(Code.call); // postavljamo call instrukciju
		
		Code.put2(offset); // operand call instrukcije postavljamo i sam rezultat instrukcije ako ima ce biti osatati ofc na ExprStacku
		cntPar[cntFuncCallLevel] = 0;
		cntFuncCallLevel--;
	}
	
	
	
	
	public void visit(OnlyFuncCall procCall){ // ovo je zaseban poziv f-je kao f1(...);
		Obj functionObj = procCall.getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
		if(procCall.getDesignator().obj.getType() != Tab.noType){
			Code.put(Code.pop); // jer je zaseban poziv moramo ako ima povratnu vrednost da je skinemo sa ExprStack-a jer niko je nece koristiti
		}
	}
	
	
	public void visit(ActualParams actPar) {
		cntPar[cntFuncCallLevel]++;
	}
	
	public void visit(ActualParam actPar) {
		cntPar[cntFuncCallLevel]++;
	}
	
	public void visit(ReturnExpr returnExpr){ // intrukcije da se napusti metoda postoji i kodMethDecl jer mozda void metoda nema return naredbu
		// a ofc return moze imati na vise mesta u fukciji 
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		returnFlag = true;
	}
	
	
	public void visit(ReturnNoExpr returnNoExpr){
		Code.put(Code.exit);
		Code.put(Code.return_);
		
		returnFlag = true;
	}
	
	
	public void visit(AddExpr addExpr){
		// stavljamo instrukciju za racun addExpr ali smatramo da su oba operanda vec nalaze na ExprStack-u
		Addop temp = addExpr.getAddop();
		if( temp instanceof AddopPlus) {
			Code.put(Code.add);
		}else {
			Code.put(Code.sub);
		}
	}
	
	
	public void visit(MulopFactor addExpr){
		// stavljamo instrukciju za racun addExpr ali smatramo da su oba operanda vec nalaze na ExprStack-u
		Mulop temp = addExpr.getMulop();
		if( temp instanceof MulopTimes) {
			Code.put(Code.mul);
		}else if( temp instanceof MulopDiv ) {
			Code.put(Code.div);
		}else {
			Code.put(Code.rem);
		}
		
	}
	
	public void visit(DesStatmInc desInc) {
		Designator des = desInc.getDesignator();
		int Br = 1;
		if(  des instanceof DesignatorArr){
			DesignatorArr desArr = (DesignatorArr) des;
			Obj objArr = desArr.getArrObj();
			System.out.println("PROVERA " + objArr.getName());
			if( listNiz.containsKey(objArr)) {
				Br = listNiz.get(objArr);
			}
		}
		
		Obj con = new Obj( Obj.Con, "$", null, Br, 0);
		Code.load(con);
		Code.put(Code.add);
		
		// ako niz[i]++; onda treba astore da se izvrsi
		if( des instanceof DesignatorArr) {
			Code.put(Code.astore);
			System.out.println("DESARR INC");
			return;
		}
		
		Code.store(desInc.getDesignator().obj);
	}
	
	
	public void visit(DesStatmDec desInc) {
		Obj con = new Obj( Obj.Con, "$", null, 1, 0);
		Code.load(con);
		Code.put(Code.sub);
		
		Designator des = desInc.getDesignator();
		// ako niz[i]--; onda treba astore da se izvrsi
		if( des instanceof DesignatorArr) {
			Code.put(Code.astore);
			System.out.println("DESARR DEC");
			return;
		}
		
		Code.store(desInc.getDesignator().obj);
	}
	
	
	public void visit(FnewExpr newExpr) {
		
		FactorOpcija fOp = newExpr.getFactorOpcija();
		if( fOp instanceof FactorOpcija2) {
			Code.put(Code.add);
		}
		
		// alociranje mem za nis a assigment ce dodeliti nisu vrednost
		Code.put(Code.newarray);
		Type type = newExpr.getType();
		int flag = 1;
		if( type.struct == Tab.charType) {
			flag = 0;
		}
		Code.put(Code.const_n + flag);
	}
	
	public void visit(DoStart1 whileStm) {
		System.out.println("DO start");
		doFlag++;
		doPCadr[doFlag] = Code.pc;
		doBreakAdr[doFlag] = new ArrayList<Integer>();
	}
	
	public void visit(DoWhile doWhile) {
		System.out.println("KRAJ WHILE");
		ConditionList condListNode = doWhile.getCondition().getConditionList();
		CondTerm1 condTerm = (CondTerm1)doWhile.getCondition().getCondTerm();
		CondTermList condTermList = condTerm.getCondTermList();
		if( condTermList.getLine() > 0) {
			// ima && izraz u Cond i treba skok na pocetak while-a i fixup za sve adrese
			Code.putJump(doPCadr[doFlag]);
			for (Iterator iterator = doWhileListAdr.iterator(); iterator.hasNext();) {
				int adrJmp = (int) iterator.next();
				Code.fixup(adrJmp);
			}
		}
		// fixup za break adrese
		for (Iterator iterator = doBreakAdr[doFlag].iterator(); iterator.hasNext();) {
			int adrJmp = (int) iterator.next();
			Code.fixup(adrJmp);
		}
		doBreakAdr[doFlag].clear();
		
		doWhileListAdr.clear();
		doFlag--;
	}
	
	public void visit(Continue cont) {
		int adr = doPCadr[doFlag]; // adresa pocetka while petlje
		Code.putJump(adr);
	}
	
	public void visit(Break br) {
		Code.putJump(0);
        int adrJmp = Code.pc - 2;
		doBreakAdr[doFlag].add(adrJmp);
	}
	
	public void visit(IfStart1  ifStm) {
		System.out.println("IF start");
		ifLevel++;
		ifPcAdr[ifLevel] = null;
		ifPcAdr[ifLevel] = new ArrayList<Integer>();
	}
	
	public void visit(StmIf1 ifStm) {
		
		for (Iterator iterator = ifPcAdr[ifLevel].iterator(); iterator.hasNext();) {
			int adrJmp = (int) iterator.next();
			Code.fixup(adrJmp);
		}
		ifPcAdr[ifLevel].clear();
		ifLevel--;
	}
	
	public void visit(StmIf2 ifStm) {
		
		for (Iterator iterator = ifPcAdr[ifLevel].iterator(); iterator.hasNext();) {
			int adrJmp = (int) iterator.next();
			Code.fixup(adrJmp);
		}
		ifPcAdr[ifLevel].clear();
		ifLevel--;
	}
	
	public void visit(ElseStart1 elsStm) {
		Code.putJump(0); // skok posle Else grane
		int adr = Code.pc - 2;
		for (Iterator iterator = ifPcAdr[ifLevel].iterator(); iterator.hasNext();) {
			int adrJmp = (int) iterator.next();
			Code.fixup(adrJmp);
		}
		ifPcAdr[ifLevel].clear();
		ifPcAdr[ifLevel].add(adr);
	}
	
	public void visit(StmIfElse1 ifStm) {
		for (Iterator iterator = ifPcAdr[ifLevel].iterator(); iterator.hasNext();) {
			int adrJmp = (int) iterator.next();
			Code.fixup(adrJmp);
		}
		ifPcAdr[ifLevel].clear();
		ifLevel--;
	}
	
	public void visit(StmIfElse2 ifStm) {
		for (Iterator iterator = ifPcAdr[ifLevel].iterator(); iterator.hasNext();) {
			int adrJmp = (int) iterator.next();
			Code.fixup(adrJmp);
		}
		ifPcAdr[ifLevel].clear();
		ifLevel--;
	}
	
	public void visit(CondFact1 cond) {
		
		boolean flagDoWhile = false;
		
		SyntaxNode parent = cond.getParent();
		while (!(parent instanceof Program)) {
			if(parent instanceof DoWhile) {
				flagDoWhile = true;
				break;
			}
			if(parent instanceof StmIf1 || parent instanceof StmIf2 ) {
				flagDoWhile = false;
				break;
			}
			if(parent instanceof StmIfElse1 || parent instanceof StmIfElse2 ) {
				flagDoWhile = false;
				break;
			}
			parent = parent.getParent();
		}
		
		// provera da li je on sam u While uslovu kao while( flag ) ili ima vise njih
		if( flagDoWhile ) {
			int adr = doPCadr[doFlag];
			DoWhile node = (DoWhile)parent;
			ConditionList condListNode = node.getCondition().getConditionList();
			CondTerm1 condTerm = (CondTerm1)node.getCondition().getCondTerm();
			CondTermList condTermList = condTerm.getCondTermList();
			// sam u while petlji Cond
			if( condListNode.getLine() < 1 && condTermList.getLine() < 1) {
				System.out.println(" CondFact1 SAM");
		        // putFalseJump je namenjen za if sto ima u sebi inverz op pa moramo i mi ovde da ga inverzujemo jer mi skacemo po datom uslovu u do_while
				Obj con = new Obj( Obj.Con, "$", Tab.intType, 1, 0);
				// ovo iznad je kreiranje Obj da bi mogli uraditi neposredno ubacivanje na ExprStack 1 ce nam biti true a sve ostalo false
				Code.load(con); // ubacivanje const na ExprStack    
				Code.putFalseJump(Code.ne, adr);
				return;
			// ima ih vise u uslovu
			}
			// vise || uslova
			if(condListNode.getLine() > 1) {
				System.out.println(" CondFact1 VISE ||");
				// putFalseJump je namenjen za if sto ima u sebi inverz op pa moramo i mi ovde da ga inverzujemo jer mi skacemo po datom uslovu u do_while
				Obj con = new Obj( Obj.Con, "$", Tab.intType, 1, 0);
				// ovo iznad je kreiranje Obj da bi mogli uraditi neposredno ubacivanje na ExprStack 1 ce nam biti true a sve ostalo false
				Code.load(con); // ubacivanje const na ExprStack    
				Code.putFalseJump(Code.ne, adr);    
				return;
			}else {
				System.out.println(" CondFact1 VISE &&");
				// vise && uslova
				int adrJmp;
				
				Obj con = new Obj( Obj.Con, "$", Tab.intType, 1, 0);
				// ovo iznad je kreiranje Obj da bi mogli uraditi neposredno ubacivanje na ExprStack 1 ce nam biti true a sve ostalo false
				Code.load(con); // ubacivanje const na ExprStack
				
				// mora fixup ofc
				// putFalseJump je namenjen za if sto ima u sebi inverz op ovde stavljamo isti kao uslov jer je && u pitanju
		        Code.putFalseJump(Code.eq, 0);
		        adrJmp = Code.pc - 2;
		        doWhileListAdr.add(adrJmp);
		        
				return;
			}
			
		}else if ( parent instanceof StmIf1 || parent instanceof StmIf2) {
			// if condition bez Elsa u StmIf1 ili StmIf2
			int adrJmp;
			
			ConditionList condListNode;
			CondTerm1 condTerm;
			CondTermList condTermList;
			if( parent instanceof StmIf1) {
				StmIf1 node = (StmIf1)parent;
				condListNode = node.getCondition().getConditionList();
				condTerm = (CondTerm1)node.getCondition().getCondTerm();
				condTermList = condTerm.getCondTermList();
			}else {
				StmIf2 node = (StmIf2)parent;
				condListNode = node.getCondition().getConditionList();
				condTerm = (CondTerm1)node.getCondition().getCondTerm();
				condTermList = condTerm.getCondTermList();
			}
			

			// sam u if-u Cond
			if( condListNode.getLine() < 1 && condTermList.getLine() < 1) {
				System.out.println(" CondFact1 SAM");
		        // putFalseJump je namenjen za if sto ima u sebi inverz op
				Obj con = new Obj( Obj.Con, "$", Tab.intType, 1, 0);
				// ovo iznad je kreiranje Obj da bi mogli uraditi neposredno ubacivanje na ExprStack 1 ce nam biti true a sve ostalo false
				Code.load(con); // ubacivanje const na ExprStack    
				Code.putFalseJump(Code.eq, 0);
				adrJmp = Code.pc - 2;
				ifPcAdr[ifLevel].add(adrJmp);
				return;
			// ima ih vise u uslovu
			}
			// vise || uslova
			if(condListNode.getLine() > 1) {
				System.out.println(" CondFact1 VISE ||");
				// putFalseJump je namenjen za if sto ima u sebi inverz op 
				Obj con = new Obj( Obj.Con, "$", Tab.intType, 1, 0);
				// ovo iznad je kreiranje Obj da bi mogli uraditi neposredno ubacivanje na ExprStack 1 ce nam biti true a sve ostalo false
				Code.load(con); // ubacivanje const na ExprStack   
				
				if( cond.getParent().getParent() instanceof CondLogILI) {
					// poslednji Conditon u if iskazu moramo fixup za sve prethodne cond da uradimo
					System.out.println("Poslednji Cond u IF-u");
					Code.putFalseJump(Code.eq, 0);
					//fixup
					for (Iterator iterator = ifPcAdr[ifLevel].iterator(); iterator.hasNext();) {
						adrJmp = (int) iterator.next();
						Code.fixup(adrJmp);
					}
					ifPcAdr[ifLevel].clear();
				}else {	
					Code.putFalseJump(Code.ne, 0);    
				}
				adrJmp = Code.pc - 2;
				ifPcAdr[ifLevel].add(adrJmp);
				return;
			}else {
				System.out.println(" CondFact1 VISE &&");
				// putFalseJump je namenjen za if sto ima u sebi inverz op 
				Obj con = new Obj( Obj.Con, "$", Tab.intType, 1, 0);
				// ovo iznad je kreiranje Obj da bi mogli uraditi neposredno ubacivanje na ExprStack 1 ce nam biti true a sve ostalo false
				Code.load(con); // ubacivanje const na ExprStack   
				Code.putFalseJump(Code.eq, 0); 
				adrJmp = Code.pc - 2;
				ifPcAdr[ifLevel].add(adrJmp);
				return;
				
			}
		}else {
			// if uslov za ELSE granom u StmIfElse1 ili StmIfElse2
			int adrJmp;
			
			ConditionList condListNode;
			CondTerm1 condTerm;
			CondTermList condTermList;
			if( parent instanceof StmIfElse1) {
				StmIfElse1 node = (StmIfElse1)parent;
				condListNode = node.getCondition().getConditionList();
				condTerm = (CondTerm1)node.getCondition().getCondTerm();
				condTermList = condTerm.getCondTermList();
			}else {
				StmIfElse2 node = (StmIfElse2)parent;
				condListNode = node.getCondition().getConditionList();
				condTerm = (CondTerm1)node.getCondition().getCondTerm();
				condTermList = condTerm.getCondTermList();
			}
			

			// sam u if-u Cond
			if( condListNode.getLine() < 1 && condTermList.getLine() < 1) {
				System.out.println(" CondFact1 SAM");
		        // putFalseJump je namenjen za if sto ima u sebi inverz op
				Obj con = new Obj( Obj.Con, "$", Tab.intType, 1, 0);
				// ovo iznad je kreiranje Obj da bi mogli uraditi neposredno ubacivanje na ExprStack 1 ce nam biti true a sve ostalo false
				Code.load(con); // ubacivanje const na ExprStack    
				Code.putFalseJump(Code.eq, 0);
				adrJmp = Code.pc - 2;
				ifPcAdr[ifLevel].add(adrJmp);
				return;
			// ima ih vise u uslovu
			}
			// vise || uslova
			if(condListNode.getLine() > 1) {
				System.out.println(" CondFact1 VISE ||");
				// putFalseJump je namenjen za if sto ima u sebi inverz op 
				Obj con = new Obj( Obj.Con, "$", Tab.intType, 1, 0);
				// ovo iznad je kreiranje Obj da bi mogli uraditi neposredno ubacivanje na ExprStack 1 ce nam biti true a sve ostalo false
				Code.load(con); // ubacivanje const na ExprStack   
				
				if( cond.getParent().getParent() instanceof CondLogILI) {
					// poslednji Conditon u if iskazu moramo fixup za sve prethodne cond da uradimo
					System.out.println("Poslednji Cond u IF-u");
					Code.putFalseJump(Code.eq, 0);
					//fixup
					for (Iterator iterator = ifPcAdr[ifLevel].iterator(); iterator.hasNext();) {
						adrJmp = (int) iterator.next();
						Code.fixup(adrJmp);
					}
					ifPcAdr[ifLevel].clear();
				}else {	
					Code.putFalseJump(Code.ne, 0);    
				}
				adrJmp = Code.pc - 2;
				ifPcAdr[ifLevel].add(adrJmp);
				return;
			}else {
				System.out.println(" CondFact1 VISE &&");
				// putFalseJump je namenjen za if sto ima u sebi inverz op 
				Obj con = new Obj( Obj.Con, "$", Tab.intType, 1, 0);
				// ovo iznad je kreiranje Obj da bi mogli uraditi neposredno ubacivanje na ExprStack 1 ce nam biti true a sve ostalo false
				Code.load(con); // ubacivanje const na ExprStack   
				Code.putFalseJump(Code.eq, 0); 
				adrJmp = Code.pc - 2;
				ifPcAdr[ifLevel].add(adrJmp);
				return;
				
			}
		}
		
	}
	
	public void visit(CondFact2 cond) {
		boolean flagDoWhile = false;
		
		SyntaxNode parent = cond.getParent();
		while (!(parent instanceof Program)) {
			if(parent instanceof DoWhile) {
				flagDoWhile = true;
				break;
			}
			if(parent instanceof StmIf1 || parent instanceof StmIf2 ) {
				flagDoWhile = false;
				break;
			}
			if(parent instanceof StmIfElse1 || parent instanceof StmIfElse2 ) {
				flagDoWhile = false;
				break;
			}
			parent = parent.getParent();
		}
		
		// provera da li je on sam u While uslovu kao while(i > 2) ili ima vise njih
		if( flagDoWhile ) {
			int adr = doPCadr[doFlag];
			DoWhile node = (DoWhile)parent;
			ConditionList condListNode = node.getCondition().getConditionList();
			CondTerm1 condTerm = (CondTerm1)node.getCondition().getCondTerm();
			CondTermList condTermList = condTerm.getCondTermList();
			// sam u while petlji Cond
			if( condListNode.getLine() < 1 && condTermList.getLine() < 1) {
				switch (relopFlag) {
		         case ">": // putFalseJump je namenjen za if sto ima u sebi inverz op pa moramo i mi ovde da ga inverzujemo jer mi skacemo po datom uslovu u do_while
		             Code.putFalseJump(Code.le, adr);
		             break;
		         case ">=":
		        	 Code.putFalseJump(Code.lt, adr);
		             break;
		         case "<":
		        	 Code.putFalseJump(Code.ge, adr);
		             break;
		         case "<=":
		        	 Code.putFalseJump(Code.gt, adr);
		             break;
		         case "==":
		        	 Code.putFalseJump(Code.ne, adr);
		             break;
		         case "!=":
		        	 Code.putFalseJump(Code.eq, adr);
		             break;
		         default:
		             throw new IllegalArgumentException("LOS Relop znak: ");
		     
				}
			
				return;
			// ima ih vise u uslovu
			}
			// vise || uslova
			if(condListNode.getLine() > 1) {
				switch (relopFlag) {
		         case ">": // putFalseJump je namenjen za if sto ima u sebi inverz op pa moramo i mi ovde da ga inverzujemo jer mi skacemo po datom uslovu u do_while
		             Code.putFalseJump(Code.le, adr);
		             break;
		         case ">=":
		        	 Code.putFalseJump(Code.lt, adr);
		             break;
		         case "<":
		        	 Code.putFalseJump(Code.ge, adr);
		             break;
		         case "<=":
		        	 Code.putFalseJump(Code.gt, adr);
		             break;
		         case "==":
		        	 Code.putFalseJump(Code.ne, adr);
		             break;
		         case "!=":
		        	 Code.putFalseJump(Code.eq, adr);
		             break;
		         default:
		             throw new IllegalArgumentException("LOS Relop znak: ");
		     
				}
				return;
			}else {
				// vise && uslova
				int adrJmp;
				switch (relopFlag) {
				// mora fixup ofc
				// putFalseJump je namenjen za if sto ima u sebi inverz op ovde stavljamo isti kao uslov jer je && u pitanju
		         case ">": 
		             Code.putFalseJump(Code.gt, 0);
		             adrJmp = Code.pc - 2;
		             doWhileListAdr.add(adrJmp);
		             break;
		         case ">=":
		        	 Code.putFalseJump(Code.ge, 0);
		        	 adrJmp = Code.pc - 2;
		             doWhileListAdr.add(adrJmp);
		             break;
		         case "<":
		        	 Code.putFalseJump(Code.lt, 0);
		        	 adrJmp = Code.pc - 2;
		             doWhileListAdr.add(adrJmp);
		             break;
		         case "<=":
		        	 Code.putFalseJump(Code.le, 0);
		        	 adrJmp = Code.pc - 2;
		             doWhileListAdr.add(adrJmp);
		             break;
		         case "==":
		        	 Code.putFalseJump(Code.eq, 0);
		        	 adrJmp = Code.pc - 2;
		             doWhileListAdr.add(adrJmp);
		             break;
		         case "!=":
		        	 Code.putFalseJump(Code.ne, 0);
		        	 adrJmp = Code.pc - 2;
		             doWhileListAdr.add(adrJmp);
		             break;
		         default:
		             throw new IllegalArgumentException("LOS Relop znak: ");
		     
				}
				return;
			}
			
		}else if ( parent instanceof StmIf1 || parent instanceof StmIf2) {
			// if condition bez Elsa u StmIf1 ili StmIf2
			int adrJmp;
			
			ConditionList condListNode;
			CondTerm1 condTerm;
			CondTermList condTermList;
			if( parent instanceof StmIf1) {
				StmIf1 node = (StmIf1)parent;
				condListNode = node.getCondition().getConditionList();
				condTerm = (CondTerm1)node.getCondition().getCondTerm();
				condTermList = condTerm.getCondTermList();
			}else {
				StmIf2 node = (StmIf2)parent;
				condListNode = node.getCondition().getConditionList();
				condTerm = (CondTerm1)node.getCondition().getCondTerm();
				condTermList = condTerm.getCondTermList();
			}
			

			// sam u if-u Cond
			if( condListNode.getLine() < 1 && condTermList.getLine() < 1) {
				System.out.println(" CondFact2 SAM");
		        // putFalseJump je namenjen za if sto ima u sebi inverz op
				
				switch (relopFlag) {
		         case ">": // putFalseJump je namenjen za if sto ima u sebi inverz op pa moramo i mi ovde da ga inverzujemo jer mi skacemo po datom uslovu u do_while
		             Code.putFalseJump(Code.gt, 0);
		             break;
		         case ">=":
		        	 Code.putFalseJump(Code.ge, 0);
		             break;
		         case "<":
		        	 Code.putFalseJump(Code.lt, 0);
		             break;
		         case "<=":
		        	 Code.putFalseJump(Code.le, 0);
		             break;
		         case "==":
		        	 Code.putFalseJump(Code.eq, 0);
		             break;
		         case "!=":
		        	 Code.putFalseJump(Code.ne, 0);
		             break;
		         default:
		             throw new IllegalArgumentException("LOS Relop znak: ");
		     
				}
				
				adrJmp = Code.pc - 2;
				ifPcAdr[ifLevel].add(adrJmp);
				return;
			// ima ih vise u uslovu
			}
			// vise || uslova
			if(condListNode.getLine() > 1) {
				System.out.println(" CondFact2 VISE ||");
				// putFalseJump je namenjen za if sto ima u sebi inverz op   
				
				if( cond.getParent().getParent() instanceof CondLogILI) {
					// poslednji Conditon u if iskazu ofc mora fixup za vse prethodne
					System.out.println("Poslednji Cond2 u IF-u");
					
					switch (relopFlag) {
			         case ">": // putFalseJump je namenjen za if sto ima u sebi inverz op pa moramo i mi ovde da ga inverzujemo jer mi skacemo po datom uslovu u do_while
			             Code.putFalseJump(Code.gt, 0);
			             break;
			         case ">=":
			        	 Code.putFalseJump(Code.ge, 0);
			             break;
			         case "<":
			        	 Code.putFalseJump(Code.lt, 0);
			             break;
			         case "<=":
			        	 Code.putFalseJump(Code.le, 0);
			             break;
			         case "==":
			        	 Code.putFalseJump(Code.eq, 0);
			             break;
			         case "!=":
			        	 Code.putFalseJump(Code.ne, 0);
			             break;
			         default:
			             throw new IllegalArgumentException("LOS Relop znak: ");
			     
					}
					
					for (Iterator iterator = ifPcAdr[ifLevel].iterator(); iterator.hasNext();) {
						adrJmp = (int) iterator.next();
						Code.fixup(adrJmp);
					}
					ifPcAdr[ifLevel].clear();
					
				}else {	
					
					switch (relopFlag) {
			         case ">": // putFalseJump je namenjen za if sto ima u sebi inverz op pa moramo i mi ovde da ga inverzujemo jer mi skacemo po datom uslovu u do_while
			             Code.putFalseJump(Code.le, 0);
			             break;
			         case ">=":
			        	 Code.putFalseJump(Code.lt, 0);
			             break;
			         case "<":
			        	 Code.putFalseJump(Code.ge, 0);
			             break;
			         case "<=":
			        	 Code.putFalseJump(Code.gt, 0);
			             break;
			         case "==":
			        	 Code.putFalseJump(Code.ne, 0);
			             break;
			         case "!=":
			        	 Code.putFalseJump(Code.eq, 0);
			             break;
			         default:
			             throw new IllegalArgumentException("LOS Relop znak: ");
			     
					}
					
				}
				adrJmp = Code.pc - 2;
				ifPcAdr[ifLevel].add(adrJmp);
				return;
			}else {
				System.out.println(" CondFact1 VISE &&");
				// putFalseJump je namenjen za if sto ima u sebi inverz op 
				
				switch (relopFlag) {
		         case ">": // putFalseJump je namenjen za if sto ima u sebi inverz op pa moramo i mi ovde da ga inverzujemo jer mi skacemo po datom uslovu u do_while
		             Code.putFalseJump(Code.gt, 0);
		             break;
		         case ">=":
		        	 Code.putFalseJump(Code.ge, 0);
		             break;
		         case "<":
		        	 Code.putFalseJump(Code.lt, 0);
		             break;
		         case "<=":
		        	 Code.putFalseJump(Code.le, 0);
		             break;
		         case "==":
		        	 Code.putFalseJump(Code.eq, 0);
		             break;
		         case "!=":
		        	 Code.putFalseJump(Code.ne, 0);
		             break;
		         default:
		             throw new IllegalArgumentException("LOS Relop znak: ");
		     
				}
				
				adrJmp = Code.pc - 2;
				ifPcAdr[ifLevel].add(adrJmp);
				return;
				
			}
		}else {
			// if uslov za ELSE granom u StmIfElse1 ili StmIfElse2
			int adrJmp;
			
			ConditionList condListNode;
			CondTerm1 condTerm;
			CondTermList condTermList;
			if( parent instanceof StmIfElse1) {
				StmIfElse1 node = (StmIfElse1)parent;
				condListNode = node.getCondition().getConditionList();
				condTerm = (CondTerm1)node.getCondition().getCondTerm();
				condTermList = condTerm.getCondTermList();
			}else {
				StmIfElse2 node = (StmIfElse2)parent;
				condListNode = node.getCondition().getConditionList();
				condTerm = (CondTerm1)node.getCondition().getCondTerm();
				condTermList = condTerm.getCondTermList();
			}
			

			// sam u if-u Cond
			if( condListNode.getLine() < 1 && condTermList.getLine() < 1) {
				System.out.println(" CondFact2 SAM");
		        // putFalseJump je namenjen za if sto ima u sebi inverz op
				
				switch (relopFlag) {
		         case ">": // putFalseJump je namenjen za if sto ima u sebi inverz op pa moramo i mi ovde da ga inverzujemo jer mi skacemo po datom uslovu u do_while
		             Code.putFalseJump(Code.gt, 0);
		             break;
		         case ">=":
		        	 Code.putFalseJump(Code.ge, 0);
		             break;
		         case "<":
		        	 Code.putFalseJump(Code.lt, 0);
		             break;
		         case "<=":
		        	 Code.putFalseJump(Code.le, 0);
		             break;
		         case "==":
		        	 Code.putFalseJump(Code.eq, 0);
		             break;
		         case "!=":
		        	 Code.putFalseJump(Code.ne, 0);
		             break;
		         default:
		             throw new IllegalArgumentException("LOS Relop znak: ");
		     
				}
				
				adrJmp = Code.pc - 2;
				ifPcAdr[ifLevel].add(adrJmp);
				return;
			// ima ih vise u uslovu
			}
			// vise || uslova
			if(condListNode.getLine() > 1) {
				System.out.println(" CondFact2 VISE ||");
				// putFalseJump je namenjen za if sto ima u sebi inverz op   
				
				if( cond.getParent().getParent() instanceof CondLogILI) {
					// poslednji Conditon u if iskazu ofc mora fixup za vse prethodne
					System.out.println("Poslednji Cond2 u IF-u");
					
					switch (relopFlag) {
			         case ">": // putFalseJump je namenjen za if sto ima u sebi inverz op pa moramo i mi ovde da ga inverzujemo jer mi skacemo po datom uslovu u do_while
			             Code.putFalseJump(Code.gt, 0);
			             break;
			         case ">=":
			        	 Code.putFalseJump(Code.ge, 0);
			             break;
			         case "<":
			        	 Code.putFalseJump(Code.lt, 0);
			             break;
			         case "<=":
			        	 Code.putFalseJump(Code.le, 0);
			             break;
			         case "==":
			        	 Code.putFalseJump(Code.eq, 0);
			             break;
			         case "!=":
			        	 Code.putFalseJump(Code.ne, 0);
			             break;
			         default:
			             throw new IllegalArgumentException("LOS Relop znak: ");
			     
					}
					
					for (Iterator iterator = ifPcAdr[ifLevel].iterator(); iterator.hasNext();) {
						adrJmp = (int) iterator.next();
						Code.fixup(adrJmp);
					}
					ifPcAdr[ifLevel].clear();
					
				}else {	
					
					switch (relopFlag) {
			         case ">": // putFalseJump je namenjen za if sto ima u sebi inverz op pa moramo i mi ovde da ga inverzujemo jer mi skacemo po datom uslovu u do_while
			             Code.putFalseJump(Code.le, 0);
			             break;
			         case ">=":
			        	 Code.putFalseJump(Code.lt, 0);
			             break;
			         case "<":
			        	 Code.putFalseJump(Code.ge, 0);
			             break;
			         case "<=":
			        	 Code.putFalseJump(Code.gt, 0);
			             break;
			         case "==":
			        	 Code.putFalseJump(Code.ne, 0);
			             break;
			         case "!=":
			        	 Code.putFalseJump(Code.eq, 0);
			             break;
			         default:
			             throw new IllegalArgumentException("LOS Relop znak: ");
			     
					}
					
				}
				adrJmp = Code.pc - 2;
				ifPcAdr[ifLevel].add(adrJmp);
				return;
			}else {
				System.out.println(" CondFact1 VISE &&");
				// putFalseJump je namenjen za if sto ima u sebi inverz op 
				
				switch (relopFlag) {
		         case ">": // putFalseJump je namenjen za if sto ima u sebi inverz op pa moramo i mi ovde da ga inverzujemo jer mi skacemo po datom uslovu u do_while
		             Code.putFalseJump(Code.gt, 0);
		             break;
		         case ">=":
		        	 Code.putFalseJump(Code.ge, 0);
		             break;
		         case "<":
		        	 Code.putFalseJump(Code.lt, 0);
		             break;
		         case "<=":
		        	 Code.putFalseJump(Code.le, 0);
		             break;
		         case "==":
		        	 Code.putFalseJump(Code.eq, 0);
		             break;
		         case "!=":
		        	 Code.putFalseJump(Code.ne, 0);
		             break;
		         default:
		             throw new IllegalArgumentException("LOS Relop znak: ");
		     
				}
				
				adrJmp = Code.pc - 2;
				ifPcAdr[ifLevel].add(adrJmp);
				return;
				
			}
		}
		
	}
	
	public void visit(RelopIsto isto) {
		System.out.println("Isto");
		relopFlag = "==";
	}
	
	public void visit(RelopRaz raz) {
		System.out.println("raz");
		relopFlag = "!=";
	}
	
	public void visit(RelopVece vece) {
		System.out.println("vece");
		relopFlag = ">";
	}
	
	public void visit(RelopVeceJed veceJed) {
		System.out.println("veceJed");
		relopFlag = ">=";
	}
	
	public void visit(RelopManje manje) {
		System.out.println("manje");
		relopFlag = "<";
	}
	
	public void visit(RelopManjeJed manjeJed) {
		System.out.println("manjeJed");
		relopFlag = "<=";
	}
	
	public void visit(CondLogI cond) {
		//System.out.println(" I &&");
	}
	
	public void visit(CondLogILI cond) {
		//System.out.println(" ILI ||");
	}
	
	public void visit(ReadSingStm readStm) {
		
		// read instrukcija
		System.out.println(readObj.getName());
		
		Designator des = readStm.getDesignator();
		// ako read(niz[i]); onda treba vrednost u niz da se upise
		if( des instanceof DesignatorArr) {
			if( readObj.getType().getElemType().getKind() == Struct.Int ) {
				// int tip read-a se radi
				Code.put(Code.read);
			}else {
				// char tip read-a se radi
				Code.put(Code.bread);
			}
			Code.put(Code.astore);
			System.out.println("READ array");
			return;
		}
		if( readObj.getType().getKind() == Struct.Int ) {
			// int tip read-a se radi
			Code.put(Code.read);
		}else {
			// char tip read-a se radi
			Code.put(Code.bread);
		}
		
		
		Code.store(readStm.getDesignator().obj);
	}
	
}
