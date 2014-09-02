/**
 * Copyright (C) 2014 4th Line GmbH, Switzerland and others
 *
 * The contents of this file are subject to the terms of either the
 * Common Development and Distribution License Version 1 or later
 * ("CDDL") (collectively, the "License"). You may not use this file
 * except in compliance with the License. See LICENSE.txt for more
 * information.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.jupnp.tool.cli;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * Extension of tool to allow redirection of logback to stdout and stderr.
 * 
 * @author Jochen Hiller - Initial contribution
 */
public class JUPnPToolWithRedirectionOfOutput extends JUPnPTool {

	public JUPnPToolWithRedirectionOfOutput() {
		super();
	}

	public JUPnPToolWithRedirectionOfOutput(PrintStream out, PrintStream err) {
		super(out, err);
	}

	/**
	 * Add an appender to Root-Logger with redirection to stdout.
	 */
	protected void setLogging(String resourceName, String rootAppenderLogLevel) {
		super.setLogging(resourceName, rootAppenderLogLevel);

		LoggerContext context = (LoggerContext) LoggerFactory
				.getILoggerFactory();
		// try to redirect logback output to given outputStream
		// add appender to output stream for ROOT logger
		PatternLayoutEncoder ple = new PatternLayoutEncoder();
		ple.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
		ple.setContext(context);
		ple.start();

		ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory
				.getLogger(Logger.ROOT_LOGGER_NAME);
		OutputStreamAppender<ILoggingEvent> appender = new OutputStreamAppender<ILoggingEvent>();
		// first set encoder, then output stream
		appender.setEncoder(ple);
		OutputStream os = new RedirectToOutputStream(this.outputStream);
		appender.setOutputStream(os);
		appender.setName("RedirectToOutputStream");
		appender.setContext(context);
		appender.start();

		rootLogger.addAppender(appender);
		Level level = Level.valueOf(rootAppenderLogLevel);
		rootLogger.setLevel(level);
		// see https://issues.apache.org/jira/browse/SLING-3045
		// there can by sync issues when reconfiguring logback
		long now = new Date().getTime();
		StatusPrinter.printInCaseOfErrorsOrWarnings(context, now + 1000);
	}

	private class RedirectToOutputStream extends OutputStream {

		private OutputStream outputStream;

		public RedirectToOutputStream(OutputStream os) {
			this.outputStream = os;
		}

		@Override
		public void write(int b) throws IOException {
			outputStream.write(b);
		}

		@Override
		public void write(byte[] b) throws IOException {
			outputStream.write(b);
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			outputStream.write(b, off, len);
		}

		@Override
		public void flush() throws IOException {
			outputStream.flush();
		}

		@Override
		public void close() throws IOException {
			// do NOT close the output stream
		}

	}
}
