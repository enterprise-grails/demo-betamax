package demo.betamax

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.ContentEncoding
import co.freeside.betamax.httpclient.BetamaxRoutePlanner

class BetamaxHttpBuilderFactory extends HttpBuilderFactory {

    @Override
    HTTPBuilder get() {

        // get regular builder instance
        def builder = super.get()

        // setup betamax compatibility
        BetamaxRoutePlanner.configure(builder.client)
        
        // relax ssl verification enforcement
        builder.ignoreSSLIssues()

        // remove default (i.e. gzip, deflate) content encoding handlers
        builder.client.removeRequestInterceptorByClass( ContentEncoding.RequestInterceptor.class );
        builder.client.removeResponseInterceptorByClass( ContentEncoding.ResponseInterceptor.class );

        return builder
    }
}