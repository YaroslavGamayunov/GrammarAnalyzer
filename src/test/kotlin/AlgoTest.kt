import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

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
        tests: ArrayList<TestData>,
        argumentSelector: (TestData) -> List<Any>
    ) {
        val args = argumentSelector(tests[0])
        val method = Algo::class.java.declaredMethods.find {
            val params = it.parameters
            return@find it.name == methodName && (params.size == args.size)
                    && params.indices.all { i -> args[i]::class.java == params[i].annotatedType }
        }

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

    @Test
    fun testGrammar() {
        assertThrows<IllegalArgumentException> {
            Grammar(
                setOf(SpecialSymbols.SPECIAL_NON_TERMINAL.value),
                setOf(SpecialSymbols.SPECIAL_NON_TERMINAL.value),
                ' ',
                mapOf()
            )
        }
    }
}
