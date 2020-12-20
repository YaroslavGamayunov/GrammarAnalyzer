import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class TestPackInfo(val path: String, val numOfTests: Int)

@Serializable
data class TestConfig(
    val scanTests: TestPackInfo,
    val completeTests: TestPackInfo,
    val predictTests: TestPackInfo
)

@Serializable
data class TestData(
    val situations: List<MutableSet<Situation>>,
    val situationsAfterApplying: List<MutableSet<Situation>>,
    val j: Int,
    val word: String,
    val grammar: Grammar
)

class TestLoader(config: TestConfig) {
    var completeTests: ArrayList<TestData> = arrayListOf()
    var predictTests: ArrayList<TestData> = arrayListOf()
    var scanTests: ArrayList<TestData> = arrayListOf()

    private fun loadTests(dataHolder: ArrayList<TestData>, testInfo: TestPackInfo) {
        for (i in 1..testInfo.numOfTests) {
            val testPath = "${testInfo.path}/${i}.json"
            val testJson = LoadingTools.readLines(testPath).joinToString("\n")
            dataHolder.add(Json.parse(TestData.serializer(), testJson))
        }
    }

    constructor(configPath: String) : this(
        Json.parse(
            TestConfig.serializer(),
            LoadingTools.readLines(
                configPath
            ).joinToString("\n")
        )
    )

    init {
        loadTests(completeTests, config.completeTests)
        loadTests(predictTests, config.predictTests)
        loadTests(scanTests, config.scanTests)
    }
}