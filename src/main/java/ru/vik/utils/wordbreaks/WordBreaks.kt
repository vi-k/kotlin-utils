package ru.vik.utils.wordbreaks

class WordBreaks {
    companion object {
        private val options = setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE)
        private lateinit var hyphen: String
        private var regexList = mutableListOf<RegexItem>()

        init {
            reinit()
        }

        data class RegexItem(val regex: Regex, val replace: String)

        fun addRegex(pattern: String, replace: String = "$1$hyphen") {
            regexList.add(RegexItem(Regex(pattern, options), replace))
        }

        fun reinit(hyphen: String = "\u00AD") {
            this.hyphen = hyphen

//            val A = "[абвгдеёжзийклмнопрстуфхцчшщъыьэюя\u0301]"
//            val nA = "[^абвгдеёжзийклмнопрстуфхцчшщъыьэюя\u0301]"
            val V = "(?:[аеёиоуыэюя]\u0301?)"
            val X = "[йъь]"
            val N = "[бвгджйзклмнпрстфхцчшщ]"
            val syl = "(?:$N{1,}$V|$V$N)"

            regexList.clear()

            // Алгоритм найден в сети под имененем: "П.Хpистова в модификации Дымченко и Ваpсанофьева"
            // Х-
            // Г-Г
            // ГС-СГ
            // СГ-СГ
            // ГС-ССГ
            // ГСС-ССГ
            //
            // и переработан
            addRegex("(пред)(?=$N)")            // пред- (пред-намеренный, запредельный, но пре-дадим)
            addRegex("\\b(пре|при|без)(?=$syl)") // пре- при- без- (пре-стол, пре-святая, пре-благословенная, пре-свитер, без-благодатный)
            addRegex("($syl)(пре|при|без)(?=$syl)", "$1$hyphen$2$hyphen") // -пре- -при- (за-пре-стольный, за-пре-дельный, не-при-частный, не-без-благодатный)
            addRegex("\\b(бого)(?=$syl)")       // бого- (бого-хранимая, бого-сродный)
            addRegex("\\b(архи)(?=$syl)")       // архи- (архи-тип, архи-епископ, архи-триклин)
            addRegex("\\b(благо)(?=$syl)")      // благо- (благо-словение)
            addRegex("\\b(не)(?=$syl)")         // не-
//            addRegex("\\b(со)(?=$syl)")         // со-
//            addRegex("\\b(все)(?=$syl)")        // все-
            addRegex("($syl)(?=свят)")          // -свят (все-свя-тая, посвятить)

            // Твоею
            addRegex("\\b($V$X$N)(?=$N{1,}$V)") // йС- (эйн-штейний, эйн-штейновский)
            addRegex("($V$V$X$N)(?=$N{1,}$V)")  // йС- (проэйн-штейновский)
            addRegex("($syl$X)(?=$syl)")        // X- (дьякон, разбой-ник, счастье, объ-явление, май-ор)
            addRegex("($V{2,})(?=$V$N)")        // ГГ-Г (змее-ед, длинношеее-ед)
            addRegex("($V$V)(?=$N$V)")          // ГГ- (оглашае-мые, наполняю-щий)
            addRegex("($V)(?=$V$N)")            // Г-Г (оглаша-ем, наполня-ем, путешеству-ем)
            addRegex("($V$N)(?=$N$V)")          // С-С (об-рок, от-верзни)
            addRegex("($N$V)(?=$N$V)")          // СГ-СГ (по-до-бает, себя)
            addRegex("($V$N)(?=$N{2,}$V)")      // С-СС
        }
    }

    fun apply(text: String): String {
        var newText = text
        for (item in regexList) {
            newText = newText.replace(item.regex, item.replace)
        }
        return newText
    }
}
