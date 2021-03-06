/*
 * Copyright 2013-2017 (c) MuleSoft, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.raml.ramltopojo.plugin.maven;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.raml.ramltopojo.RamlToPojo;
import org.raml.ramltopojo.RamlToPojoBuilder;
import org.raml.v2.api.RamlModelBuilder;
import org.raml.v2.api.RamlModelResult;
import org.raml.v2.api.model.common.ValidationResult;
import org.raml.v2.api.model.v10.api.Api;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static org.apache.maven.plugins.annotations.ResolutionScope.COMPILE_PLUS_RUNTIME;
import static org.raml.ramltopojo.TypeFetchers.fromAnywhere;
import static org.raml.ramltopojo.TypeFinders.everyWhere;

@Mojo(name = "generate", requiresProject = true, threadSafe = false, requiresDependencyResolution = COMPILE_PLUS_RUNTIME,
        defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class RamlToPojoMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    /**
     * Skip plug-in execution.
     */
    @Parameter(property = "skip", defaultValue = "false")
    private boolean skip;

    /**
     * Target directory for generated Java source files.
     */
    @Parameter(property = "outputDirectory",
            defaultValue = "${project.build.directory}/generated-sources/raml-to-jaxrs-maven-plugin")
    private File outputDirectory;

    /**
     * An array of locations of the RAML file(s).
     */
    @Parameter(property = "ramlFile", required = true)
    private File ramlFile;

    /**
     * An array of locations of the RAML file(s).
     */
    @Parameter(property = "defaultPackage", required = true)
    private String defaultPackage;

    /**
     * An array of locations of the RAML file(s).
     */
    @Parameter(property = "defaultPackage", required = false)
    private List<String> basePlugins;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        if (skip) {
            getLog().info("Skipping execution...");
            return;
        }

        if (ramlFile == null) {
            throw new MojoExecutionException("ramlFile is not defined");
        }

        try {
            FileUtils.forceMkdir(outputDirectory);
        } catch (final IOException ioe) {
            throw new MojoExecutionException("Failed to createHandler directory: " + outputDirectory, ioe);
        }

        try {
            project.addCompileSourceRoot(outputDirectory.getPath());

            getLog().info("about to read file " + ramlFile + " in directory " + ramlFile.getParent());
            RamlModelResult ramlModelResult =
                    new RamlModelBuilder().buildApi(
                            new FileReader(ramlFile),
                            ramlFile.getAbsolutePath());
            if (ramlModelResult.hasErrors()) {
                for (ValidationResult validationResult : ramlModelResult.getValidationResults()) {
                    getLog().error("raml error:" + validationResult.getMessage());
                }
                throw new MojoExecutionException("invalid raml " + ramlFile);
            }

            final Api api = ramlModelResult.getApiV10();
            RamlToPojo ramlToPojo = RamlToPojoBuilder.builder(api)
                    .inPackage(defaultPackage)
                    .fetchTypes(fromAnywhere())
                    .findTypes(everyWhere()).build(basePlugins);

            ramlToPojo.buildPojos().createAllTypes(outputDirectory.getAbsolutePath());

        } catch (IOException e) {

            throw new MojoExecutionException("execution exception", e);
        }
    }
}
