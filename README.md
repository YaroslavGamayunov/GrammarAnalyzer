# GrammarAnalyzer
Context-free grammar analyzer based on Earley algorithm 

[![codecov](https://codecov.io/gh/YaroslavGamayunov/GrammarAnalyzer/branch/main/graph/badge.svg?token=T4P505PGVA)](https://codecov.io/gh/YaroslavGamayunov/GrammarAnalyzer)

## Features 
This project is built entirely on Kotlin with the help of Gradle, JUnit was used for testing.

## How to start
* Install **JRE** from  [here](https://www.oracle.com/java/technologies/javase-jre8-downloads.html)

* Build project by putting this line in terminal: 
   ```
   $ ./gradlew build
   ```
* If you want to get a jar with CLI of Earley parser, put this:
    ```
   $ ./gradlew BuildEarleyAnalyzerCli
   ```
  and then you will see a jar file in build/libs 
* If you want to run tests and see detailed code coverage report, put this:
  ```
   $ ./gradlew runTestsWithReport
   ```
## CLI
  In order to make program multifunctional, I have created console inetrface with the ability to load custom grammar 
  and check if word belongs to the language of the grammar.
#### How to use it 
  Firstly, press enter to start the input. Insert rules in the form **S -> a|b|c|d**. Then make a line break and put
  Starting nonterminal on the next line. After these steps you will be able to check if word belongs to the grammar.
  To stop the session, i.e create new early parser on different grammar, put ENDS. If you want to quit, put EXIT 
 