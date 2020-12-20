import SpecialSymbols.EPS_SYMBOL

fun main() {
    val algo =
        Algo.fit(
            Grammar(
                setOf('(', ')'),
                setOf('S'),
                'S',
                mapOf("S" to listOf("(S)", EPS_SYMBOL.value.toString()))
            )
        )

    println(algo.predict("()"))
    println(algo.predict("()"))
    println(algo.predict("(((())))"))
}