package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;

public class MJParserTest {

	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	public static void main(String[] args) throws Exception {
		
		Logger log = Logger.getLogger(MJParserTest.class);
		
		Reader br = null;
		try {
			String testFile = args[0];
			String outFileName = testFile.substring(0, testFile.length() - 3) + "Out.out";
			String errorFileName = testFile.substring(0, testFile.length() - 3) + "Error.txt";
			
			File sourceCode = new File(testFile);
			log.info("Compiling source file: " + sourceCode.getAbsolutePath());
			
			File outFile = new File(outFileName);
			outFile.delete();
			File errorFile = new File(errorFileName);
			errorFile.delete();
			outFile.createNewFile();
			errorFile.createNewFile();
			FileWriter outFileWriter = new FileWriter(outFileName);
			FileWriter errorFileWriter = new FileWriter(errorFileName);
			
			br = new BufferedReader(new FileReader(sourceCode));
			Yylex lexer = new Yylex(br);
			
			MJParser p = new MJParser(lexer);
	        Symbol s = p.parse();  //pocetak parsiranja
	        
	        Program prog = (Program)(s.value); 
	        Tab.init();
			// ispis sintaksnog stabla
	        
			//log.info(prog.toString(""));
	        outFileWriter.write(prog.toString(""));
	        log.info("===================================");
	        outFileWriter.write("\n===================================\n");
	        
			// ispis prepoznatih programskih konstrukcija
			SemanticPass v = new SemanticPass();
			v.outFileWriter = outFileWriter;
			v.errorFileWriter = errorFileWriter;
			
			v.insertBooltype();  // unos bool tipa u TS
			
			prog.traverseBottomUp(v); 
	      
			log.info(" Print count calls = " + v.printCallCount);
			outFileWriter.write(" Print count calls = " + v.printCallCount);

			log.info(" Deklarisanih globalnih promenljivih ima = " + v.varDeclCount);
			outFileWriter.write(" Deklarisanih globalnih promenljivih ima = " + v.varDeclCount);
			
			log.info("===================================");
			outFileWriter.write("\n===================================\n");
			Tab.dump();
			
			if(!p.errorDetected && v.passed()){  // OVDE SE POZIVA visitor CodeGenerator za generisanje byte koda MikroJava
				String objFilePath = args[1];
				System.out.println("Putanja obj File: " + objFilePath);
				
				File objFile = new File(objFilePath);
				if(objFile.exists()) objFile.delete();
				
				CodeGenerator codeGenerator = new CodeGenerator();
				codeGenerator.initPredeklarisaneFje();
				
				prog.traverseBottomUp(codeGenerator);
				Code.dataSize = v.nVars;
				Code.mainPc = codeGenerator.getMainPc();
				Code.write(new FileOutputStream(objFile));
				log.info("Parsiranje uspesno zavrseno!");
				outFileWriter.write("Parsiranje uspesno zavrseno!");
			}else{
				log.error("Parsiranje NIJE uspesno zavrseno!");
				outFileWriter.write("Parsiranje NIJE uspesno zavrseno!");
			}
		} 
		finally {
			if (br != null) try { br.close(); } catch (IOException e1) { log.error(e1.getMessage(), e1); }
		}

	}
	
	
}
