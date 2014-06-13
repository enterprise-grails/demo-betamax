package demo.betamax

import co.freeside.betamax.*
import grails.test.spock.*
import grails.util.BuildSettingsHolder
import org.junit.Rule
import spock.lang.*

class RepoServiceSpec extends IntegrationSpec {
    
    @Rule Recorder recorder = new Recorder(
        tapeRoot: new File(BuildSettingsHolder.settings?.baseDir, 'test/resources/tapes'),
        ignoreLocalhost: true,
        sslSupport: true
    )

    def repoService
 
    def setup() {
        repoService.httpBuilderFactory = new BetamaxHttpBuilderFactory()
    }

    //---------------------------------------------------------------------------------------------
    //
    @Betamax(tape='github')
    def "repo listing"() { 
        expect:
            repoService.list().size() > 0
    }
}