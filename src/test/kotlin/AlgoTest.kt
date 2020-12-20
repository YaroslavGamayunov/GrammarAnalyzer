import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

const val TEST_CONFIG_PATH = "test_config.json"

class AlgoTest {
    companion object {
        private lateinit var testLoader: TestLoader

        @JvmStatic
        @BeforeAll
        fun setupTestData() {
            testLoader = TestLoader(TEST_CONFIG_PATH)
        }
    }

    private fun generalMethodTest(
        methodName: String,
        tests: Iterable<TestData>,
        argumentSelector: (TestData) -> List<Any>
    ) {
        val method = Algo::class.java.declaredMethods.find { method -> method.name == methodName }

        method?.let {
            it.isAccessible = true
            for (test in tests) {
                val arguments = argumentSelector(test)
                it.invoke(Algo.fit(test.grammar), *arguments.toTypedArray())
                assert(test.situations == test.situationsAfterApplying)
            }
        }
    }

    @Test
    fun testScan() {
        generalMethodTest("scan", testLoader.scanTests) { arrayListOf(it.situations, it.j, it.word) }
    }


    @Test
    fun testPredict() {
        generalMethodTest("predict", testLoader.predictTests) { arrayListOf(it.situations, it.j) }
    }

    @Test
    fun testComplete() {
        generalMethodTest("complete", testLoader.completeTests) { arrayListOf(it.situations, it.j) }
    }

    @Test
    fun testParser() {
        val balancedBracketSequences = Grammar(
            setOf('(', ')'),
            setOf('S'),
            'S',
            mapOf(
                "S" to listOf("(S)", "SS", "")
            )
        )

        var algo = Algo.fit(balancedBracketSequences)

        assert(algo.predict("(())()"))
        assert(algo.predict("()()"))
        assert(algo.predict("()"))
        assert(algo.predict("(") == false)
        assert(algo.predict(")") == false)
        assert(algo.predict(""))

        val palindromes = Grammar(
            setOf('a', 'b'),
            setOf('S'),
            'S',
            mapOf(
                "S" to listOf("aSa", "", "bSb", "a", "b")
            )
        )

        algo = Algo.fit(palindromes)

        assert(algo.predict("a"))
        assert(algo.predict("b"))
        assert(algo.predict("bbaabb"))
        assert(algo.predict("ab") == false)
        assert(algo.predict("abababb") == false)
        assert(algo.predict(""))
    }
}
