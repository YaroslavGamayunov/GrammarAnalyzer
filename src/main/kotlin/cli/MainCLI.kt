package cli

import Algo
import Grammar
import SpecialSymbols

enum class Command(var value: String) {
    EndSession("ENDS"),
    ExitProgram("EXIT")
}

enum class ConsoleState {
    InputGrammarRules,
    InputStartNonTerminal,
    InputTestString
}

fun printHelp() {
    println("---------------------------------------------------------")
    println(
        "Press enter to start a new analyzer session.\n" +
                "Then on each line input rules for the context free grammar in the form:\n" +
                "S -> a|b|c|d\n" +
                "Then one make one extra line break and input starting nonterminal\n" +
                "After these steps you can test words on this grammar\n" +
                "until you put 'ENDS' (ends the session)\n" +
                "or 'EXIT' (Exits the program) on the line\n\n" +
                "NOTE: REMEMBER:\n" +
                "You are not allowed to use '${SpecialSymbols.SPECIAL_NON_TERMINAL.value}' symbol in the grammar"
    )
    println("---------------------------------------------------------")
}

fun main() {
    while (true) {
        printHelp()
        readLine()
        println("Now start the input:")
        var state = ConsoleState.InputGrammarRules

        val rules = hashMapOf<String, ArrayList<String>>()

        val nonTerminals = hashSetOf<Char>()
        val terminals = hashSetOf<Char>()
        var startNonTerminal: Char = ' '

        lateinit var algo: Algo

        var currentLine: String?
        currentSession@ while (true) {
            if (state == ConsoleState.InputStartNonTerminal) {
                println("Now input the start nonterminal:")
            } else if (state == ConsoleState.InputTestString) {
                println("Input test string:")
            }
            currentLine = readLine()
            if (currentLine == null) {
                break
            }

            when (state) {
                ConsoleState.InputGrammarRules -> {
                    if (currentLine.isNotEmpty()) {
                        val parts = currentLine.split(" -> ")
                        val leftPart = parts[0]
                        val rightParts = parts.drop(1).joinToString(" -> ").split("|")
                        nonTerminals.add(leftPart[0])
                        rules.getOrPut(leftPart) { ArrayList() }.addAll(rightParts)
                    }
                }

                ConsoleState.InputStartNonTerminal -> {
                    startNonTerminal = currentLine[0]
                    for (word in rules.flatMap { it.value }) {
                        for (c in word) {
                            if (!nonTerminals.contains(c)) {
                                terminals.add(c)
                            }
                        }
                    }
                    algo = Algo.fit(Grammar(terminals, nonTerminals, startNonTerminal, rules))
                    state = ConsoleState.InputTestString
                }

                ConsoleState.InputTestString -> {
                    println(if (algo.predict(currentLine)) "+" else "-")
                }
            }

            when (currentLine) {
                Command.EndSession.value -> break@currentSession
                Command.ExitProgram.value -> return
                "" -> if (state == ConsoleState.InputGrammarRules) state = ConsoleState.InputStartNonTerminal
            }
        }
    }
}