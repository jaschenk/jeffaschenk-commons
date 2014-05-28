


import org.junit.Test
import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat
import static org.junit.matchers.JUnitMatchers.containsString

class LanguageTest {
    def languages = [tom:['Java', 'Groovy'], dick:['C#', 'Ruby']]

    @Test void tomKnowsJavaAndGroovyHamcrest() {
        assertThat languages['tom'], is(["Java", "Groovy"])
        assertThat languages['tom'][0], containsString("v")
        assertThat languages['tom'][1], containsString("v")
    }

    @Test void tomKnowsJavaAndGroovyNative() {
        assert languages['tom'] == ["Java", "Groovy"]
        assert languages['tom'].every{ it.contains("v") }
    }
}

