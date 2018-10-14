# Pact-JVM with Gradle and Kotlin 

A simple Todo application with a consumer and a provider demonstrating Pact.

The Provider application is based on

- Spark-Kotlin
- Kotlin
- Pact Gradle Plugin
- Gradle Spawn Plugin
- Gradle Shadow Plugin

The Consumer application is based on:
- Apache HttpClient Fluent API
- Java

And it's tested with:
- Pact-JVM JUnit 5

The Consumer-app will generate a Pact-file when its consumer contract test are ran. Then when the Provider get built, Gradle will spawn and start a local instance of the provider and verify the contract against its API.
