/*
 * Copyright 2021 the original author or authors.
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

package org.gradle.api.plugins.jvm.internal;

import org.gradle.api.Action;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.plugins.jvm.JvmTestSuite;
import org.gradle.api.plugins.jvm.JvmTestSuiteTarget;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.internal.Cast;
import org.gradle.language.BinaryCollection;
import org.gradle.language.internal.DefaultBinaryCollection;

import javax.inject.Inject;

public abstract class DefaultJvmTestSuite implements JvmTestSuite {
    private final DefaultBinaryCollection<JvmTestSuiteTarget> targets;
    private final SourceSet sourceSet;
    private final String name;

    @Inject
    public DefaultJvmTestSuite(String name, SourceSetContainer sourceSets, ConfigurationContainer configurations) {
        this.name = name;
        this.sourceSet = sourceSets.create(getName());
        Configuration compileOnly = configurations.getByName(sourceSet.getCompileOnlyConfigurationName());
        Configuration implementation = configurations.getByName(sourceSet.getImplementationConfigurationName());
        Configuration runtimeOnly = configurations.getByName(sourceSet.getRuntimeOnlyConfigurationName());
        this.targets = Cast.uncheckedCast(getObjectFactory().newInstance(DefaultBinaryCollection.class, JvmTestSuiteTarget.class));
    }

    @Override
    public String getName() {
        return name;
    }

    @Inject
    public abstract ObjectFactory getObjectFactory();

    public SourceSet getSources() {
        return sourceSet;
    }
    public void sources(Action<? super SourceSet> configuration) {
        configuration.execute(getSources());
    }


    public DefaultBinaryCollection<JvmTestSuiteTarget> getTargets() {
        return targets;
    }
    public void targets(Action<BinaryCollection<? extends JvmTestSuiteTarget>> configuration) {
        configuration.execute(targets);
    }

    public void addTestTarget(String target) {
        DefaultJvmTestSuiteTarget defaultJvmTestSuiteTarget = getObjectFactory().newInstance(DefaultJvmTestSuiteTarget.class, target);
        defaultJvmTestSuiteTarget.getTestClasses().from(sourceSet.getOutput().getClassesDirs());
        defaultJvmTestSuiteTarget.getRuntimeClasspath().from(sourceSet.getRuntimeClasspath());
        targets.add(defaultJvmTestSuiteTarget);
    }
}
