/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.initializr.support

import groovy.text.GStringTemplateEngine
import groovy.text.Template
import groovy.text.TemplateEngine
import org.codehaus.groovy.control.CompilationFailedException

/**
 * @author Dave Syer
 * @since 1.0
 */
class GroovyTemplate {

	// This is a copy/paste from GroovyTemplate in spring-boot-cli. We should migrate
	// to Spring's native support available in 4.1

	static String template(String name, Map<String, ?> model) throws IOException,
			CompilationFailedException, ClassNotFoundException {
		template(new GStringTemplateEngine(), name, model)
	}

	static String template(TemplateEngine engine, String name, Map<String, ?> model)
			throws IOException, CompilationFailedException, ClassNotFoundException {
		Writable writable = getTemplate(engine, name).make(model)
		StringWriter result = new StringWriter()
		writable.writeTo(result)
		result.toString()
	}

	static Template getTemplate(TemplateEngine engine, String name)
			throws CompilationFailedException, ClassNotFoundException, IOException {

		File file = new File("templates", name)
		if (file.exists()) {
			return engine.createTemplate(file)
		}

		ClassLoader classLoader = GroovyTemplate.class.getClassLoader()
		URL resource = classLoader.getResource("templates/" + name)
		if (resource != null) {
			return engine.createTemplate(resource)
		}

		return engine.createTemplate(name)
	}
}
