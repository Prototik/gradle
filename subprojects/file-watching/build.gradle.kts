plugins {
    id("gradlebuild.distribution.api-java")
}

description = "File system watchers for keeping the VFS up-to-date"

dependencies {
    api(project(":snapshots"))

    implementation(project(":base-annotations"))
    implementation(project(":enterprise-operations"))

    implementation(libs.guava)
    implementation(libs.nativePlatform)
    implementation(libs.nativePlatformFileEvents)
    implementation(libs.slf4jApi)

    testImplementation(project(":process-services"))
    testImplementation(project(":resources"))
    testImplementation(project(":persistent-cache"))
    testImplementation(project(":build-option"))
    testImplementation(testFixtures(project(":core")))
    testImplementation(testFixtures(project(":file-collections")))
    testImplementation(testFixtures(project(":tooling-api")))
    testImplementation(testFixtures(project(":launcher")))
    testImplementation(testFixtures(project(":snapshots")))

    testImplementation(libs.commonsIo)
    testImplementation(libs.gradleEnterpriseTestAnnotation)

    integTestDistributionRuntimeOnly(project(":distributions-core"))
}
