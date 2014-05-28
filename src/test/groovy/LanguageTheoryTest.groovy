


import static org.junit.Assume.assumeTrue as assume

import org.junit.experimental.theories.DataPoint
import org.junit.experimental.theories.Theories
import org.junit.experimental.theories.Theory
import org.junit.runner.RunWith

@RunWith(Theories)
class LanguageTheoryTest {
    @DataPoint public static String java = 'Java'
    @DataPoint public static String ruby = 'JRuby'
    @DataPoint public static String python = 'Jython'
    @DataPoint public static String javascript = 'Rhino'
    @DataPoint public static String groovy = 'Groovy'
    @DataPoint public static String scala = 'Scala'
    @DataPoint public static String csharp = 'C#'

    def jvmLanguages = [java, ruby, python, groovy, scala, javascript]

    def teamSkills = [
        tom:   [java, groovy, ruby],
        dick:  [csharp, scala, java, python],

        harry: [javascript, groovy, java]
    ]

    @Theory void everyone_knows_java() {
        teamSkills.each { person, skills ->
            assert java in skills
        }
    }

    @Theory void someone_knows_each_jvm_language(String language) {
        assume language in jvmLanguages
        assert teamSkills.any { person, skills ->
            language in skills
        }
    }

    @Theory void tom_knows_all_languages_ending_with_y(String language) {
        assume language.endsWith('y')
        assert language in teamSkills.tom
    }
}


