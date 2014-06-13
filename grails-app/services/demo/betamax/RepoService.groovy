package demo.betamax

import groovyx.net.http.HTTPBuilder

import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

class RepoService {

    static transactional = false

    def httpBuilderFactory

    def list() {
        def res = null
        try {
            def builder = httpBuilderFactory.get()
            builder.request('https://api.github.com/orgs/enterprise-grails/repos', GET, JSON) { req ->
                headers.'User-Agent' = 'alesbukovsky'
                response.success = { resp, json ->
                    res = json.collect{ [ name: it.name, description: it.description ] }
                }
                response.failure = { resp ->
                    log.error "Listing failed: ${resp.statusLine}"
                }
            }
        } catch (Throwable ex) {
            log.error 'Listing failed', ex 
        }
        return res
    }
}