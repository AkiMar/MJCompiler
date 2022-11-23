// generated with ast extension for cup
// version 0.8
// 29/7/2022 15:43:48


package rs.ac.bg.etf.pp1.ast;

public interface Visitor { 

    public void visit(FactorOpcija FactorOpcija);
    public void visit(KlasaListVar KlasaListVar);
    public void visit(MethodDecl MethodDecl);
    public void visit(Mulop Mulop);
    public void visit(IfStart IfStart);
    public void visit(Relop Relop);
    public void visit(ConditionList ConditionList);
    public void visit(CondTermList CondTermList);
    public void visit(StatementDrugi StatementDrugi);
    public void visit(FormalParamDecl FormalParamDecl);
    public void visit(KlasaDecl KlasaDecl);
    public void visit(StatementList StatementList);
    public void visit(Addop Addop);
    public void visit(Factor Factor);
    public void visit(CondTerm CondTerm);
    public void visit(StmIf StmIf);
    public void visit(Designator Designator);
    public void visit(ParametriMehod ParametriMehod);
    public void visit(TermDodatak TermDodatak);
    public void visit(OptArgParser OptArgParser);
    public void visit(ConstDeclList ConstDeclList);
    public void visit(ElseStart ElseStart);
    public void visit(VarDeclMet VarDeclMet);
    public void visit(WhileStm WhileStm);
    public void visit(ActualParamList ActualParamList);
    public void visit(ProgramListaProm ProgramListaProm);
    public void visit(VarDeclList VarDeclList);
    public void visit(FormalParamList FormalParamList);
    public void visit(Expr Expr);
    public void visit(DoStart DoStart);
    public void visit(StmIfElse StmIfElse);
    public void visit(MethodTypeName MethodTypeName);
    public void visit(StatementPrviList StatementPrviList);
    public void visit(DesignatorStatement DesignatorStatement);
    public void visit(ActualPars ActualPars);
    public void visit(Statement Statement);
    public void visit(VarDecl VarDecl);
    public void visit(OptArgListParser OptArgListParser);
    public void visit(ConstDeclParser ConstDeclParser);
    public void visit(CondFact CondFact);
    public void visit(MethodDeclList MethodDeclList);
    public void visit(SingleStatement SingleStatement);
    public void visit(FormPars FormPars);
    public void visit(StatementPrvi StatementPrvi);
    public void visit(MulopProcenat MulopProcenat);
    public void visit(MulopDiv MulopDiv);
    public void visit(MulopTimes MulopTimes);
    public void visit(AddopMinus AddopMinus);
    public void visit(AddopPlus AddopPlus);
    public void visit(RelopManjeJed RelopManjeJed);
    public void visit(RelopManje RelopManje);
    public void visit(RelopVeceJed RelopVeceJed);
    public void visit(RelopVece RelopVece);
    public void visit(RelopRaz RelopRaz);
    public void visit(RelopIsto RelopIsto);
    public void visit(DesignatorMat DesignatorMat);
    public void visit(DesignatorArr DesignatorArr);
    public void visit(DesignatorObican DesignatorObican);
    public void visit(FactorOpcija2 FactorOpcija2);
    public void visit(FactorOpcija1 FactorOpcija1);
    public void visit(FuncCall FuncCall);
    public void visit(Var Var);
    public void visit(FExp FExp);
    public void visit(FnewBezExpr FnewBezExpr);
    public void visit(FnewExpr FnewExpr);
    public void visit(FboolConst FboolConst);
    public void visit(FcharConst FcharConst);
    public void visit(FnumConst FnumConst);
    public void visit(ActualParam ActualParam);
    public void visit(ActualParams ActualParams);
    public void visit(NoActuals NoActuals);
    public void visit(Actuals Actuals);
    public void visit(MinusFactor MinusFactor);
    public void visit(SimpleFactor SimpleFactor);
    public void visit(MulopFactor MulopFactor);
    public void visit(Term Term);
    public void visit(TermExpr TermExpr);
    public void visit(AddExpr AddExpr);
    public void visit(PrintStmt PrintStmt);
    public void visit(ReadSingStm ReadSingStm);
    public void visit(SingDesStm SingDesStm);
    public void visit(OnlyFuncCall OnlyFuncCall);
    public void visit(DesStatmDec DesStatmDec);
    public void visit(DesStatmHash DesStatmHash);
    public void visit(DesStatmInc DesStatmInc);
    public void visit(Assignment Assignment);
    public void visit(ElseStart1 ElseStart1);
    public void visit(IfStart1 IfStart1);
    public void visit(StmIf2 StmIf2);
    public void visit(StmIf1 StmIf1);
    public void visit(StmIfElse2 StmIfElse2);
    public void visit(StmIfElse1 StmIfElse1);
    public void visit(NoStmsPrviList NoStmsPrviList);
    public void visit(StmsPrviList StmsPrviList);
    public void visit(MatchedIf MatchedIf);
    public void visit(MathcedIfElse MathcedIfElse);
    public void visit(DoWhileStm DoWhileStm);
    public void visit(StatementPrviDerived1 StatementPrviDerived1);
    public void visit(Break Break);
    public void visit(Continue Continue);
    public void visit(ReturnNoExpr ReturnNoExpr);
    public void visit(ReturnExpr ReturnExpr);
    public void visit(ErrorStmt ErrorStmt);
    public void visit(StmDrugi StmDrugi);
    public void visit(StmPrvi StmPrvi);
    public void visit(ErrorCondFact3 ErrorCondFact3);
    public void visit(CondFact2 CondFact2);
    public void visit(CondFact1 CondFact1);
    public void visit(CondTermListDerived1 CondTermListDerived1);
    public void visit(CondLogI CondLogI);
    public void visit(CondTerm1 CondTerm1);
    public void visit(ConditionListDerived1 ConditionListDerived1);
    public void visit(CondLogILI CondLogILI);
    public void visit(Condition Condition);
    public void visit(DoStart1 DoStart1);
    public void visit(DoWhile DoWhile);
    public void visit(NoStmt NoStmt);
    public void visit(Statements Statements);
    public void visit(ErrorForma3 ErrorForma3);
    public void visit(Forma2 Forma2);
    public void visit(Forma1 Forma1);
    public void visit(SingleFormalParamDecl SingleFormalParamDecl);
    public void visit(FormalParamDecls FormalParamDecls);
    public void visit(NoFormParam NoFormParam);
    public void visit(ObaImajuFormIoptArg ObaImajuFormIoptArg);
    public void visit(OptArgListParserForm OptArgListParserForm);
    public void visit(FormParams FormParams);
    public void visit(OptArg3 OptArg3);
    public void visit(OptArg2 OptArg2);
    public void visit(OptArg1 OptArg1);
    public void visit(SingleOptArg SingleOptArg);
    public void visit(MultiOptArgs MultiOptArgs);
    public void visit(MethodTypeName2 MethodTypeName2);
    public void visit(MethodTypeName1 MethodTypeName1);
    public void visit(MethodDecl1 MethodDecl1);
    public void visit(NoMethodDecl NoMethodDecl);
    public void visit(MethodDeclarations MethodDeclarations);
    public void visit(Type Type);
    public void visit(NoConstDecl NoConstDecl);
    public void visit(ConstDeclListDerived2 ConstDeclListDerived2);
    public void visit(ConstDeclListDerived1 ConstDeclListDerived1);
    public void visit(ConsDeclarations ConsDeclarations);
    public void visit(ConstDeclBool ConstDeclBool);
    public void visit(ConstDeclChar ConstDeclChar);
    public void visit(ConstDeclNumber ConstDeclNumber);
    public void visit(NoVarDeclMet3 NoVarDeclMet3);
    public void visit(VarDeclMet2 VarDeclMet2);
    public void visit(VarDeclMet1 VarDeclMet1);
    public void visit(NoVarDecl NoVarDecl);
    public void visit(VarDeclarations2 VarDeclarations2);
    public void visit(VarDeclarations1 VarDeclarations1);
    public void visit(ErrorStmVart ErrorStmVart);
    public void visit(VarDecl3 VarDecl3);
    public void visit(VarDecl2 VarDecl2);
    public void visit(VarDecl1 VarDecl1);
    public void visit(NoKlasaVarLista NoKlasaVarLista);
    public void visit(KlasaListaVar KlasaListaVar);
    public void visit(KLasaDecl2 KLasaDecl2);
    public void visit(KlasaDecl1 KlasaDecl1);
    public void visit(NoProgram3 NoProgram3);
    public void visit(ProgList3 ProgList3);
    public void visit(ProgList2 ProgList2);
    public void visit(ProgList1 ProgList1);
    public void visit(ProgName ProgName);
    public void visit(Program Program);

}