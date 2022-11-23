package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{

	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%

" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\f" 	{ }

"program"   { return new_symbol(sym.PROG, yytext()); }
"print" 	{ return new_symbol(sym.PRINT, yytext()); }
"return" 	{ return new_symbol(sym.RETURN, yytext()); }
"void" 		{ return new_symbol(sym.VOID, yytext()); }
"break" 	{ return new_symbol(sym.BREAK, yytext()); }
"class" 	{ return new_symbol(sym.CLASS, yytext()); }
"enum"		{ return new_symbol(sym.ENUM, yytext()); }
"else" 		{ return new_symbol(sym.ELSE, yytext()); }
"const" 	{ return new_symbol(sym.CONST, yytext()); }
"if" 		{ return new_symbol(sym.IF, yytext()); }
"do" 		{ return new_symbol(sym.DO, yytext()); }
"while" 	{ return new_symbol(sym.WHILE, yytext()); }
"new" 		{ return new_symbol(sym.NEW, yytext()); }
"read" 		{ return new_symbol(sym.READ, yytext()); }

"continue" 	{ return new_symbol(sym.CONTINUE, yytext()); }
"this" 		{ return new_symbol(sym.THIS, yytext()); }
"final"		{ return new_symbol(sym.FINAL, yytext()); }

"*" {return new_symbol(sym.TIMES);}
"/" {return new_symbol(sym.DIV);}
"+" {return new_symbol(sym.PLUS);}
"-" {return new_symbol(sym.MINUS);}
"=" {return new_symbol(sym.EQUAL);}
";" {return new_symbol(sym.SEMI);}
"," {return new_symbol(sym.COMMA);}
"(" {return new_symbol(sym.LPAREN);}
")" {return new_symbol(sym.RPAREN);}
"{" {return new_symbol(sym.LBRACE);}
"}" {return new_symbol(sym.RBRACE);}
"[" {return new_symbol(sym.LSQUARE);}
"]" {return new_symbol(sym.RSQUARE);}
"%" {return new_symbol(sym.PROCENAT);}
"==" {return new_symbol(sym.ISTO);}
"!=" {return new_symbol(sym.RAZLICITO);}
">" {return new_symbol(sym.VECE);}
"<" {return new_symbol(sym.MANJE);}
">=" {return new_symbol(sym.VECEJED);}
"<=" {return new_symbol(sym.MANJEJED);}
"&&" {return new_symbol(sym.LOGI);}
"||" {return new_symbol(sym.LOGILI);}
"++" {return new_symbol(sym.INC);}
"--" {return new_symbol(sym.DEC);}
":" {return new_symbol(sym.DUPTACKA);}
"." {return new_symbol(sym.TACKA);}
"#" {return new_symbol(sym.HASHTAG);}


"//" {yybegin(COMMENT);}
<COMMENT>"\n" {yybegin(YYINITIAL);}
<COMMENT>. {yybegin(COMMENT);}
<COMMENT>"\r\n" {yybegin(YYINITIAL);}


((true) | (false)) {return new_symbol (sym.BOOLCONST, yytext());}
[0-9]+  { return new_symbol(sym.NUMBER, new Integer (yytext())); }
([a-z]|[A-Z])[a-z|A-Z|0-9|_]* 	{return new_symbol (sym.IDENT, yytext()); }
"'"[\040-\176]"'" {return new_symbol (sym.CHARCONST, new Character (yytext().charAt(1)));}


. { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1) + " pozicija greske " + (yycolumn + 1)); }






