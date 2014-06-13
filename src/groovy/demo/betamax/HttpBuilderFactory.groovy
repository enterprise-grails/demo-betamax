package demo.betamax

import groovyx.net.http.HTTPBuilder

class HttpBuilderFactory {

    HTTPBuilder get() {
        return new HTTPBuilder()
    }
}