import kotlinx.serialization.Serializable

@Serializable
data class Grammar(
    val terminals: Set<Char>,
    val nonterminals: Set<Char>,
    val startNonterminal: Char,
    val rules: Map<String, List<String>>
) {
    init {
        if (terminals.contains(SpecialSymbols.SPECIAL_NON_TERMINAL.value) ||
            nonterminals.contains(SpecialSymbols.SPECIAL_NON_TERMINAL.value)
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