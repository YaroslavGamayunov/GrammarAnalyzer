import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

class SerializationTest {
    @Test
    fun testSituationSerialization() {
        val s = Situation(EarleyRule('a', "bcd", 1), 0)
        val serialized = Json.stringify(Situation.serializer(), s)
        val deserialized = Json.parse(Situation.serializer(), serialized)
        assert(s == deserialized)
    }

    @Test
    fun testGrammarSerialization() {
        val grammar = Grammar(
            setOf('a', 'b'),
            setOf('S'),
            'S',
            mapOf(
                "S" to listOf("aSa", "", "bSb", "a", "b")
            )
        )
        val serialized = Json.stringify(Grammar.serializer(), grammar)
        val deserialized = Json.parse(Grammar.serializer(), serialized)
        assert(grammar == deserialized)
    }
}