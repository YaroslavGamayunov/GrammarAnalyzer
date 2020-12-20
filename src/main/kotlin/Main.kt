import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.Serializable

@Serializable
data class TestPackInfo(val path: String, val numOfTests: Int)

@Serializable
data class TestConfig(
    val scanTests: TestPackInfo,
    val completeTests: TestPackInfo,
    val predictTests: TestPackInfo
)

@ImplicitReflectionSerializer
fun main() {
    val grammar1 = Grammar( // psp
        setOf('(', ')'),
        setOf('S'),
        'S',
        mapOf(
            "S" to listOf("(S)", "SS", "")
        )
    )
    val grammar2 = Grammar( // w = w^r
        setOf('a', 'b'),
        setOf('S'),
        'S',
        mapOf(
            "S" to listOf("aSa", "", "bSb", "a", "b")
        )
    )

    val grammar3 = Grammar( // a = 2b
        setOf('a', 'b'),
        setOf('S'),
        'S',
        mapOf(
            "S" to listOf("aSbb", "abb")
        )
    )

    val grammar4 = Grammar( // Wa != Wb
        setOf('a', 'b'),
        setOf('S', 'T', 'U', 'V'),
        'S',
        mapOf(
            "S" to listOf("T", "U"),
            "T" to listOf("VaT", "VaV", "TaV"),
            "U" to listOf("VbU", "VbV", "UbV"),
            "V" to listOf("aVbV", "bVaV", "")
        )
    )

    val algo = Algo.fit(grammar4)

    print(algo.predict("ababaaabbb"))
}