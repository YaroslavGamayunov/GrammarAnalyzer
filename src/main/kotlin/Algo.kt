import kotlin.properties.Delegates

data class EarleyRule(val leftPart: Char, val rightPart: String, val dotPosition: Int)
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
            scan(word, situations, j)
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

    private fun initialize(word: String): List<MutableSet<Situation>> {
        val situations = ArrayList<HashSet<Situation>>(word.length)
        for (i in word.indices + 1) {
            situations.add(HashSet())
        }

        val initialRule =
            EarleyRule(SpecialSymbols.SPECIAL_NON_TERMINAL.value, grammar.startNonterminal.toString(), 0)
        situations[0].add(Situation(initialRule, 0))

        return situations
    }

    private fun scan(word: String, situations: List<MutableSet<Situation>>, j: Int) {
        if (j == 0) {
            return
        }
        // situation is (A -> <alpha>.a<beta>, i)
        for (situation in situations[j - 1]) {
            val rule = situation.rule

            if (rule.dotPosition == rule.rightPart.length) {
                continue
            }

            val a: Char = rule.rightPart[rule.dotPosition]
            if (a == SpecialSymbols.EPS_SYMBOL.value || a == word[j - 1]) {
                situations[j].add(Situation(rule.copy(dotPosition = rule.dotPosition + 1), situation.position))
            }
        }
    }

    private fun predict(situations: List<MutableSet<Situation>>, j: Int) {
        val updatedData = arrayListOf<Situation>()

        for (situation in situations[j]) {
            val rule = situation.rule
            if (rule.dotPosition >= rule.rightPart.length) {
                continue
            }

            val b: Char = rule.rightPart[rule.dotPosition]
            if (grammar.rules[b.toString()] != null) {
                for (grammarRule in grammar.rules[b.toString()]!!) {
                    updatedData.add(Situation(EarleyRule(b, grammarRule, 0), j))
                }
            }
        }
        situations[j].addAll(updatedData)
    }

    private fun complete(situations: List<MutableSet<Situation>>, j: Int) {
        for ((rule1, i) in situations[j]) {
            if (rule1.dotPosition != rule1.rightPart.length) continue

            for ((rule2, k) in situations[i]) {
                if (rule2.rightPart.filterIndexed { index, c -> c == rule1.leftPart && index == i }.isNotEmpty()) {
                    situations[j].add(Situation(rule2.copy(dotPosition = rule2.dotPosition + 1), k))
                }
            }
        }
    }
}