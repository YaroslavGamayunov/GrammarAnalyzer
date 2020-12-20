import kotlinx.serialization.Serializable

@Serializable
data class Grammar(
    val terminals: Set<Char>,
    val nonTerminals: Set<Char>,
    val startNonTerminal: Char,
    val rules: Map<String, List<String>>
) {
    init {
        if (terminals.contains(SpecialSymbols.SPECIAL_NON_TERMINAL.value) ||
            nonTerminals.contains(SpecialSymbols.SPECIAL_NON_TERMINAL.value)
        ) {
            throw IllegalArgumentException(
                "Alphabets contain special nonterminal"
            )
        }
    }
}


enum class SpecialSymbols(val value: Char) {
    SPECIAL_NON_TERMINAL('$')
}