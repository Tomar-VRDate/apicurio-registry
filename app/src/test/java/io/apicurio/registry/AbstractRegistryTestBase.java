/*
 * Copyright 2020 Red Hat
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

package io.apicurio.registry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;

import io.apicurio.registry.content.ContentHandle;
import io.apicurio.registry.utils.tests.TestUtils;
import junit.framework.AssertionFailedError;

/**
 * Abstract base class for all registry tests.
 *
 * @author eric.wittmann@gmail.com
 */
public abstract class AbstractRegistryTestBase {

    public static final String CURRENT_ENV = "CURRENT_ENV";
    public static final String CURRENT_ENV_MAS_REGEX = ".*mas.*";

    protected String generateArtifactId() {
        return TestUtils.generateArtifactId();
    }

    /**
     * Loads a resource as a string.  Good e.g. for loading test artifacts.
     * @param resourceName the resource name
     */
    protected final String resourceToString(String resourceName) {
        try (InputStream stream = getClass().getResourceAsStream(resourceName)) {
            Assertions.assertNotNull(stream, "Resource not found: " + resourceName);
            return new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a resource as an input stream.
     * @param resourceName the resource name
     */
    protected final InputStream resourceToInputStream(String resourceName) {
        InputStream stream = getClass().getResourceAsStream(resourceName);
        Assertions.assertNotNull(stream, "Resource not found: " + resourceName);
        return stream;
    }

    protected final ContentHandle resourceToContentHandle(String resourceName) {
        return ContentHandle.create(resourceToString(resourceName));
    }

    public static void assertMultilineTextEquals(String expected, String actual) {
        Assertions.assertEquals(normalizeMultilineText(expected), normalizeMultilineText(actual));
    }

    private static String normalizeMultilineText(String text) {
        try (BufferedReader reader = new BufferedReader(new StringReader(text))) {
            String line = reader.readLine();
            StringBuilder builder = new StringBuilder();
            while (line != null) {
                builder.append(line);
                builder.append("\n");
                line = reader.readLine();
            }
            return builder.toString();
        } catch (IOException e) {
            throw new AssertionFailedError(e.getMessage());
        }
    }
}
