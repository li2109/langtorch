
# LangTorch🔥

[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)
[![discord](https://img.shields.io/discord/1094297543078326403)](https://discord.gg/YVUQ4X8E)
[![jitpack](https://jitpack.io/v/Knowly-ai/langtorch.svg)](https://jitpack.io/#Knowly-ai/langtorch)

Building composable LLM application with Java.





# Langtorch

Building composable LLM application with Java.


# Get started
Step 1. Add the JitPack repository to your build file
### Maven
```
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
``` 
### Gradle
```
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}
```

Step 2. Add the dependency
### Maven
```
<dependency>
    <groupId>com.github.Knowly-ai</groupId>
    <artifactId>langtorch</artifactId>
    <version>0.0.1</version>
</dependency>
```
### Gradle
```
dependencies {
	implementation 'com.github.Knowly-ai:langtorch:0.0.1'
}
```

## Components

#### LLM Service
- OpenAI Service

#### Prompt Template

#### Chain

#### Tool

#### Capability

## Documentation

[Documentation](https://github.com/Knowly-ai/langtorch): TBD. Still working on it.


## Acknowledgements
This library is inspired by the following libraries:
 - [langchain](https://github.com/hwchase17/langchain)
 - [semantic-kernel](https://github.com/microsoft/semantic-kernel)
 - [openai-java](https://github.com/TheoKanning/openai-java)

