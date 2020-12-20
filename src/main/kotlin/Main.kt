import SpecialSymbols.EPS_SYMBOL

fun main() {
    val algo =
        Algo.fit(
            Grammar(
                setOf('(', ')'),
                setOf('S'),
                'S',
                mapOf(
                    "S" to listOf("(S)", "SS", EPS_SYMBOL.value.toString())
                )
            )
        )
    print(algo.predict("(()(()()))"))
}