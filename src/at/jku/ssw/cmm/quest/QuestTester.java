/*
 *  This file is part of C-Compact.
 *
 *  C-Compact is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  C-Compact is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with C-Compact. If not, see <http://www.gnu.org/licenses/>.
 *
 *  Copyright (c) 2014-2015 Fabian Hummer
 *  Copyright (c) 2014-2015 Thomas Pointhuber
 *  Copyright (c) 2014-2015 Peter Wassermair
 */
 
package at.jku.ssw.cmm.quest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import at.jku.ssw.cmm.compiler.Compiler;
import at.jku.ssw.cmm.compiler.Obj;
import at.jku.ssw.cmm.compiler.Tab;
import at.jku.ssw.cmm.preprocessor.exception.PreprocessorException;
import at.jku.ssw.cmm.gui.file.FileManagerCode;
import at.jku.ssw.cmm.interpreter.exceptions.RunTimeException;
import at.jku.ssw.cmm.preprocessor.Preprocessor;
import at.jku.ssw.cmm.profile.Profile;
import at.jku.ssw.cmm.profile.Quest;
import at.jku.ssw.cmm.profile.XMLWriteException;
import at.jku.ssw.cmm.quest.exception.CompilerErrorException;

public class QuestTester {
	
	/**
	 * Starting the Quest Tester and finishes the Quest if all things are ok!
	 * @param profile
	 * @param quest
	 */
	public static void start(Profile profile, Quest quest){
		String[] ignore = {"\n", ",", ";"};
		QuestTester qt = new QuestTester("qt/input.cmm", "qt/ref.cmm", "qt/user.cmm", ignore);
		
		QuestMatchError e = qt.test();
		
		if( e == null ){
			System.out.println("Successful!");
			try {
				Profile.changeQuestStateToFinished(profile, quest);
			} catch (XMLWriteException e1) {
				System.err.println("Profil konnte nicht geschrieben werden!");
				e1.printStackTrace();
			}
			
			//TODO open GUI with finish text
			return;
		}
		
		e.print();
	}
	
	public static void main(String[] args){
		String[] ignore = {"\n", ",", ";"};//TODO user regex
		QuestTester qt = new QuestTester("qt/input.cmm", "qt/ref.cmm", "qt/user.cmm", ignore);
		
		QuestMatchError e = qt.test();
		
		if( e == null ){
			System.out.println("Successful!");
			return;
		}
		
		e.print();
	}

	public static final int STEPS = 5;

	public QuestTester(String generator, String verifier, String usercode, String[] ignore) {
		this.generator = generator;
		this.verifier = verifier;
		this.usercode = usercode;
		this.ignore = ignore;

		this.questRun = new QuestRun();
	}

	private final String generator;
	private final String verifier;
	private final String usercode;
	private final String[] ignore;

	private final QuestRun questRun;

	public QuestMatchError test() {

		// ----- READ INPUT DATA -----
		String inputData = null;
		try {
			// Read or generate input data
			inputData = getInputData();
		} catch (RunTimeException e) {
			return new QuestMatchError( "Runtime error when generating input data", e);
		} catch (FileNotFoundException e) {
			return new QuestMatchError("Input data file not found", e);
		} catch (CompilerErrorException e) {
			return new QuestMatchError( "Error when compiling input data generator", e);
		} catch (PreprocessorException e) {
			return new QuestMatchError( "Preprocessor error in generator source code", e);
		}

		// Double-check if everything worked
		if (inputData == null)
			return new QuestMatchError("Unknown error after reading input data", null);

		// ----- COMPILE REFERENCE PROGRAM (which is 100% right) -----
		String referenceOutput = null;
		Tab symbTab;
		try {
			symbTab = compile(this.verifier);
			
			referenceOutput = this.questRun.run(symbTab, inputData);
		} catch (RunTimeException e) {
			return new QuestMatchError( "Runtime error when generating reference output data", e);
		} catch (CompilerErrorException e) {
			return new QuestMatchError( "Error when compiling reference output data generator", e);
		} catch (FileNotFoundException e) {
			return new QuestMatchError( "Reference output generator file could not be found", e);
		} catch (PreprocessorException e) {
			return new QuestMatchError( "Preprocessor error in reference source code", e);
		}

		// ----- COMPILE REFERENCE PROGRAM (which is 100% right) -----
		String userOutput = null;
		try {
			symbTab = compile(this.usercode);
			userOutput = this.questRun.run(symbTab, inputData);
		} catch (RunTimeException e) {
			return new QuestMatchError(
					"Runtime error when generating reference output data", e);
		} catch (CompilerErrorException e) {
			return new QuestMatchError(
					"Error when compiling reference output data generator", e);
		}
		catch (FileNotFoundException e) {
			return new QuestMatchError(
					"User's source code could not be found", e);
		} catch (PreprocessorException e) {
			return new QuestMatchError( "Preprocessor error in user's source code", e);
		}
		
		System.out.println("Out1: " + referenceOutput);
		System.out.println("Out2: " + userOutput);
		
		// Double-check if output data is available
		if( referenceOutput == null || userOutput == null )
			return new QuestMatchError("Missing output data after generating", null);
		
		if( equalOutput(referenceOutput, userOutput) )
			// Returning null means "no error"
			return null;
		else
			return new QuestMatchError("Output does not match", null);
	}

	private String getInputData() throws FileNotFoundException,
			CompilerErrorException, RunTimeException, PreprocessorException {

		// Input generator is .cmm file
		if (this.generator.endsWith(".cmm")) {
			Tab symbTab = compile(this.generator);
			return this.questRun.run(symbTab, null);
		}
		// Input data is in .txt file
		else if (this.generator.endsWith(".txt")) {

			// Create input data file
			File inputFile = new File(this.generator);

			// Check if file exists
			if (!inputFile.exists())
				throw new FileNotFoundException(
						"Input text file does not exist");

			// Read input data
			return FileManagerCode.readInputDataBlank(new File(this.generator));
		} else
			throw new FileNotFoundException("Invalid input file ending");
	}

	private static Tab compile(String path) throws FileNotFoundException, CompilerErrorException, PreprocessorException {
		File inputFile = new File(path);

		if (!inputFile.exists())
			throw new FileNotFoundException(
					"Input file for compilation not found: " + path);
		
		String sourceCode = null;
		sourceCode = Preprocessor.expand(FileManagerCode.readSourceCode(inputFile), "", new ArrayList<Object[]>(), new ArrayList<Integer>());
		
		if( sourceCode == null )
			throw new CompilerErrorException("Source code is null", null);

		// Object for the compiler is allocated
		Compiler compiler = new Compiler();

		// No debug modes
		compiler.debug[0] = false;
		compiler.debug[1] = false;

		// Compile current file
		compiler.compile(sourceCode);

		// Error displaying and error count
		at.jku.ssw.cmm.compiler.Error e = compiler.getError();

		if (e != null){
			throw new CompilerErrorException(path, e);
		}

		return compiler.getSymbolTable();
	}
	
	private boolean equalOutput(String s1, String s2){
		
		// Remove signs which shall be ignored
		if( ignore != null ){
			for( String c : ignore ){
				s1.replace(c, "");
				s2.replace(c, "");
			}
		}
		
		return s1.equals(s2);
	}
}