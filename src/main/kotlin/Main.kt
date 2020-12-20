fun main() {
    val grammar = Grammar(
        setOf('(', ')'),
        setOf('S'),
        'S',
        mapOf(
            "S" to listOf("(S)", "SS", "")
        )
    )

    val algo = Algo.fit(grammar)

    print(algo.predict("(())()"))
}