# Chapter 0: Before You Start

[← Series Overview](README.md) | [Chapter 1: Your First Day →](part-01-project-setup.md)

---

You need three things: Java 25, Gradle 9.1, and a terminal.

## Java 25 (Amazon Corretto)

```bash
curl -LO https://corretto.aws/downloads/latest/amazon-corretto-25-x64-linux-jdk.tar.gz
tar -xzf amazon-corretto-25-x64-linux-jdk.tar.gz
sudo mkdir -p /usr/lib/jvm
sudo mv amazon-corretto-25.* /usr/lib/jvm/corretto-25
export JAVA_HOME=/usr/lib/jvm/corretto-25
export PATH=$JAVA_HOME/bin:$PATH
```

Add the `export` lines to your `~/.bashrc` or `~/.zshrc` to make it permanent.

Verify:

```bash
java -version
```

```
openjdk version "25" 2025-09-16
OpenJDK Runtime Environment Corretto-25...
```

## Gradle 9.1

You don't need to install Gradle globally — the project includes a wrapper (`gradlew`) that downloads it automatically. But if you want it:

```bash
sdk install gradle 9.1
```

## IDE (Optional)

Any text editor works. IntelliJ IDEA or VS Code recommended but not required.

## Quick Check

```bash
java -version && echo "---" && gradle --version
```

If both print version numbers, you're good. Let's build the engine.

---

[← Series Overview](README.md) | [Chapter 1: Your First Day →](part-01-project-setup.md)
