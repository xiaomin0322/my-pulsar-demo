package org.vander.tracing;

import brave.Tracing;
import brave.baggage.BaggagePropagation;
import brave.opentracing.BraveTracer;
import brave.propagation.B3Propagation;
import brave.propagation.Propagation.Factory;
import io.opentracing.util.GlobalTracer;
import lombok.extern.slf4j.Slf4j;
import zipkin2.reporter.brave.AsyncZipkinSpanHandler;
import zipkin2.reporter.okhttp3.OkHttpSender;
@Slf4j
public class OpenTracingZipkin {

	public static void init() {

		log.info("OpenTracingZipkin int start ............................");
		// Configure a reporter, which controls how often spans are sent
		// (the dependency is io.zipkin.reporter2:zipkin-sender-okhttp3)
		OkHttpSender sender = OkHttpSender.create(org.vander.common.Config.ZIPKIN_URL);
		AsyncZipkinSpanHandler createHandler = AsyncZipkinSpanHandler.create(sender);

		// Baggage does not need to be sent remotely via headers, but if you configure
		// with `addRemoteField()`, it will be
		Factory propagationFactory = BaggagePropagation.newFactoryBuilder(B3Propagation.FACTORY).build();

		// Now, create a Brave tracing component with the service name you want to see
		// in Zipkin.
		// (the dependency is io.zipkin.brave:brave)
		Tracing braveTracing = Tracing.newBuilder().localServiceName("mq-service")
				.propagationFactory(propagationFactory).addSpanHandler(createHandler).build();

		// use this to create an OpenTracing Tracer
		BraveTracer tracer = BraveTracer.create(braveTracing);

		GlobalTracer.registerIfAbsent(tracer);
		
		log.info("OpenTracingZipkin int end............................");
	}

}
