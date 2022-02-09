/*
 * Copyright 2013 the original author or authors.
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

package org.gradle.internal.component.model;

import com.google.common.io.Files;
import org.gradle.api.artifacts.PublishArtifact;
import org.gradle.util.internal.GUtil;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Objects;


public class DefaultIvyArtifactName implements IvyArtifactName {
    private final String name;
    private final String type;
    private final String extension;
    private final String classifier;
    private final boolean mustExist;
    private final int hashCode;

    public static DefaultIvyArtifactName forPublishArtifact(PublishArtifact publishArtifact) {
        // TODO: 18791 There must be a better way to handle the failure of JacocoPluginIntegrationTest#"reports miss configuration of destination file"
        // after adding the destination file as an artifact in JacocoPlugin - that test uses a provider { null } for the destination file.
        try {
            String name = publishArtifact.getName();
            if (name == null) {
                name = publishArtifact.getFile().getName();
            }
            String classifier = GUtil.elvis(publishArtifact.getClassifier(), null);
            return new DefaultIvyArtifactName(name, publishArtifact.getType(), publishArtifact.getExtension(), classifier);
        } catch (Exception e) {
            return null;
        }
    }

    public static DefaultIvyArtifactName forFile(File file, @Nullable String classifier) {
        String fileName = file.getName();
        return forFileName(fileName, classifier);
    }

    public static DefaultIvyArtifactName forFileName(String fileName, @Nullable String classifier) {
        String name = Files.getNameWithoutExtension(fileName);
        String extension = Files.getFileExtension(fileName);
        return new DefaultIvyArtifactName(name, extension, extension, classifier);
    }

    public DefaultIvyArtifactName(String name, String type, @Nullable String extension) {
        this(name, type, extension, null);
    }

    public DefaultIvyArtifactName(String name, String type, @Nullable String extension, @Nullable String classifier) {
        this(name, type, extension, null, false);
    }

    public DefaultIvyArtifactName(String name, String type, @Nullable String extension, @Nullable String classifier, boolean mustExist) {
        this.name = name;
        this.type = type;
        this.extension = extension;
        this.classifier = classifier;
        this.mustExist = mustExist;
        this.hashCode = computeHashCode();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(name);
        if (GUtil.isTrue(classifier)) {
            result.append("-");
            result.append(classifier);
        }
        if (GUtil.isTrue(extension) && !Files.getFileExtension(name).equals(extension)) {
            result.append(".");
            result.append(extension);
        }
        return result.toString();
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    private int computeHashCode() {
        return Objects.hash(name, type, extension, classifier, mustExist);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        DefaultIvyArtifactName other = (DefaultIvyArtifactName) obj;
        return Objects.equals(name, other.name)
            && Objects.equals(type, other.type)
            && Objects.equals(extension, other.extension)
            && Objects.equals(classifier, other.classifier)
            && Objects.equals(mustExist, other.mustExist);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getExtension() {
        return extension;
    }

    @Override
    public String getClassifier() {
        return classifier;
    }

    @Override
    public boolean isMustExist() {
        return mustExist;
    }
}
