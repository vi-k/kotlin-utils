package ru.vik.utils.wordbreaks

import org.junit.Test

import org.junit.Assert.*
import ru.vik.utils.wordbreaks.WordBreaks

class WordBreaksUnitTest {
    @Test
    fun test() {

        WordBreaks.reinit("-")

        val str = WordBreaks().apply(
                "[йС] эйнштейний эйнштейновский проэйнштейновский" +
                "\n[X-] дья́кон сча́стье объявле́ние майо́р разбо́йник кайнозойский эйзенштейновский йогурт восьмьюдесятью сейсмостойкий? герольдство? фильтрпресс?" +
                "\n[ГГ-Г] длинноше́ее длинношееее́д змеее́д Иоаки́м зоообъединение" +
                "\n[ГГ-] оглаша́емые наполня́ющий путеше́ствующие но оглаша́ем наполня́ем путеше́ствуем авиа́тор" +
                "\n[С-С] обро́к отве́рзни" +
                "\n[СГ-СГ] подоба́ет себя́" +
                "\n[С-СС] отве́рзни ца́рство пресви́терство дья́конство избра́нник путеше́ствующие воззри́ вои́нствующие взира́ющие откры́в" +
                "\n\nавиа́тор" +
                "\n" +
                "\nпреднамеренный предприимчивый предводитель предадим" +
                "\nпреосвяще́ннейший преображе́ние преблагослове́нная престо́л пресви́тер безблагодатный запрестольный запредельный небезблагодатный" +
                "\nприуготовле́ние причастный неприча́стный" +
                "\nбогохрани́мая богоспаса́емый" +
                "\nархити́п архитрикли́н архиепи́скоп архи́в архи́вы" +
                "\nблагослове́ние бла́гость благо́й благода́ть" +
                "\nвсесвята́я посвяти́ть")

        assertEquals(
                "[йС] эйн-штей-ний эйн-штей-нов-ский про-эйн-штей-нов-ский\n" +
                "[X-] дья́кон сча́стье объ-яв-ле́-ние май-о́р раз-бо́й-ник кай-но-зой-ский эй-зен-штей-нов-ский йо-гурт восьмьюде-сятью сей-смос-той-кий? ге-роль-дство? филь-трпресс?\n" +
                "[ГГ-Г] длин-но-ше́ее длин-но-шеее-е́д змее-е́д Ио-аки́м зоо-объ-еди-не-ние\n" +
                "[ГГ-] ог-ла-ша́е-мые на-пол-ня́ю-щий пу-те-ше́с-твую-щие но ог-ла-ша́-ем на-пол-ня́-ем пу-те-ше́с-тву-ем авиа́-тор\n" +
                "[С-С] об-ро́к от-ве́р-зни\n" +
                "[СГ-СГ] по-до-ба́-ет се-бя́\n" +
                "[С-СС] от-ве́р-зни ца́р-ство пре-сви́-тер-ство дья́кон-ство из-бра́н-ник пу-те-ше́с-твую-щие воз-зри́ во-и́н-ствую-щие взи-ра́ю-щие от-кры́в\n" +
                "\n" +
                "авиа́-тор\n" +
                "\n" +
                "пред-на-ме-рен-ный пред-при-им-чи-вый пред-во-ди-тель пре-да-дим\n" +
                "пре-ос-вя-ще́н-ней-ший пре-об-ра-же́-ние пре-бла-го-сло-ве́н-ная пре-сто́л пре-сви́-тер без-бла-го-дат-ный за-пре-столь-ный за-пре-дель-ный не-без-бла-го-дат-ный\n" +
                "при-уго-тов-ле́-ние при-час-тный не-при-ча́с-тный\n" +
                "бо-го-хра-ни́-мая бо-го-спа-са́е-мый\n" +
                "ар-хи-ти́п ар-хи-трик-ли́н ар-хи-епи́с-коп ар-хи́в ар-хи́-вы\n" +
                "бла-го-сло-ве́-ние бла́-гость бла-го́й бла-го-да́ть\n" +
                "все-свя-та́я по-свя-ти́ть",
                str)
    }
}