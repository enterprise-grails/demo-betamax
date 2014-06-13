Demo: Betamax Integration Test in Grails 2.3
============================================

A sample Grails 2.3.9 application demonstrating full setup for mocking external web services with [Betamax](http://freeside.co/betamax/) framework. When it runs, it makes an HTTPS call to GitHub API retrieving list of our [repositories](https://github.com/enterprise-grails). This part is eventually mocked out during integration test utilizing Betamax tape with previously captured traffic.  

**Facilities**

* Grails 2.3.9 with Betamax 1.1.2
* Spock for test specifications
* Geb and Selenium present but not used (to have typical full test setup)

There are several areas of interest:

**Resolving dependency conflicts** 

Betamax comes with Jetty 7 while Selenium brings Jetty 8. For now, we are simply excluding Selenium's dependencies (`jetty-server` and `jetty-websocket`) to fall back. We do recognize however this may cause issues later on with functional testing. Also, in order to utilize new `HttpBuilder` features (method `ignoreSSLIssues()` in particular), we are replacing this dependency in REST plugin with recent version 0.7.1. 

**Injecting HttpBuilder**

Original Betamax [example](https://github.com/robfletcher/betamax/tree/master/examples) uses direct injection of a builder instance into the application context. This may however lead to scalability and multi-threading issues since the builder uses single connection under the hood. Because the typical use case for the builder is creating a new instance per request, we are injecting a builder factory instead (`demo.betamax.HttpBuilderFactory`). This also allows us to move all necessary builder tweaks out of the specification (see below and `demo.betamax.BetamaxHttpBuilderFactory`). 

**Configuring Betamax proxy**

This just means doing what the Betamax [documentation](http://freeside.co/betamax/#proxy_compatibility) is calling for:
    
    BetamaxRoutePlanner.configure(builder.client)

**Relaxing SSL verification restrictions**

Because Betamax basically proxies the HTTP traffic, SSL verification is likely to fail. The easiest way to relax this is a method introduced in HttpBuilder 0.7.1:

    builder.ignoreSSLIssues()

**Disabling content compression**

By default the HttpBuilder uses `Accept-Encoding: gzip, deflate` in a request. This results in the response being recorded on the Betamax tape in compressed form, unsuitable for manual manipulation. We are defaulting to no compression by removing respective handlers:

    builder.client.removeRequestInterceptorByClass( ContentEncoding.RequestInterceptor.class );
    builder.client.removeResponseInterceptorByClass( ContentEncoding.ResponseInterceptor.class );

**Enforcing Betamax configuration**

For some reason Betamax does not recognize configuration in `BetamaxConfig.groovy` file. The workaround is to configure `Recorder` manually within the Spock specification:

    @Rule Recorder recorder = new Recorder(
        tapeRoot: new File(BuildSettingsHolder.settings?.baseDir, 'test/resources/tapes'),
        ignoreLocalhost: true,
        sslSupport: true
    )