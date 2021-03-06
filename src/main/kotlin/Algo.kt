import kotlinx.serialization.Serializable

@Serializable
data class EarleyRule(val leftPart: Char, val rightPart: String, val dotPosition: Int) {
    fun getNextSymbol() = rightPart.getOrNull(dotPosition)

    fun isComplete() = dotPosition == rightPart.length
}

@Serializable
data class Situation(val rule: EarleyRule, val position: Int)

class Algo(private val grammar: Grammar) {
    companion object {
        fun fit(grammar: Grammar) = Algo(grammar)
    }

    fun predict(word: String): Boolean {
        if (word.any { !grammar.terminals.contains(it) }) {
            return false
        }

        val situations = initialize(word)
        for (j in 0..word.length) {
            scan(situations, j, word)
            var prevSize = -1
            while (situations[j].size != prevSize) {
                prevSize = situations[j].size
                complete(situations, j)
                predict(situations, j)
            }
        }

        return situations[word.length].contains(
            Situation(
                EarleyRule(
                    SpecialSymbols.SPECIAL_NON_TERMINAL.value,
                    grammar.startNonterminal.toString(),
                    1
                ), 0
            )
        )
    }

    private fun initialize(word: String): ArrayList<HashSet<Situation>> {
        val situations = ArrayList<HashSet<Situation>>(word.length)
        for (i in word.indices + 1) {
            situations.add(HashSet())
        }

        val initialRule =
            EarleyRule(SpecialSymbols.SPECIAL_NON_TERMINAL.value, grammar.startNonterminal.toString(), 0)
        situations[0].add(Situation(initialRule, 0))

        return situations
    }

    private fun scan(situations: ArrayList<HashSet<Situation>>, j: Int, word: String) {
        if (j == 0) {
            return
        }

        for (situation in situations[j - 1]) {
            val rule = situation.rule

            rule.getNextSymbol()?.let { a ->
                if (a == word[j - 1]) {
                    situations[j].add(Situation(rule.copy(dotPosition = rule.dotPosition + 1), situation.position))
                }
            }
        }
    }

    private fun predict(situations: ArrayList<HashSet<Situation>>, j: Int) {
        val updatedData = arrayListOf<Situation>()

        for (situation in situations[j]) {
            val rule = situation.rule
            rule.getNextSymbol()?.let { b ->
                if (grammar.rules.containsKey(b.toString())) {
                    for (grammarRule in grammar.rules[b.toString()]!!) {
                        updatedData.add(Situation(EarleyRule(b, grammarRule, 0), j))
                    }
                }
            }
        }
        situations[j].addAll(updatedData)
    }

    private fun complete(situations: ArrayList<HashSet<Situation>>, j: Int) {
        val updatedData = arrayListOf<Situation>()

        for ((rule1, i) in situations[j]) {
            if (!rule1.isComplete()) continue

            for ((rule2, k) in situations[i]) {
                rule2.getNextSymbol()?.let {
                    if (it == rule1.leftPart) {
                        updatedData.add(Situation(rule2.copy(dotPosition = rule2.dotPosition + 1), k))
                    }
                }
            }
        }
        situations[j].addAll(updatedData)
    }
}