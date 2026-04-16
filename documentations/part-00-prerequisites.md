# Chapter 0: Before You Start

[Chapter 1: Your First Day →](part-01-project-setup.md)

---

## The Story

This is a series about concurrency in Java — but not the kind where you memorize "use `synchronized`" and move on.

You're an intern at a fintech startup. Day one, your tech lead walks over and says: "We need a background job engine. Jobs come in, engine runs them. How hard can it be?"

You say yes. You have no idea what's coming.

Over the next 14 chapters, you'll build a job engine from scratch — the kind that processes payments, sends notifications, and generates reports. It'll work perfectly on your laptop. Then you'll deploy it, and everything will break.

A customer gets charged twice. The dashboard lies. Metrics vanish. A fraud alert waits 47 minutes. Black Friday crashes the server. Someone deletes 12,000 records and nobody knows who.

Each incident teaches you something about concurrency that no textbook could. You'll fix every bug, write a test that proves the fix, and ship it. By the end, you'll have a production-grade engine with threading, backpressure, deadlock detection, authentication, rate limiting, and graceful shutdown — and you'll understand *why* every line of code is there.

## How to Read This

Every chapter is the same loop:

1. Something breaks — you get a Slack message at 2 AM
2. You write a test that reproduces the bug
3. You figure out why it happened
4. You fix it
5. The test goes green

No concept shows up before you need it. You won't hear about `volatile` until a thread literally can't see another thread's write. You won't touch `PriorityBlockingQueue` until a fraud alert sits behind 10,000 log cleanups. The bugs come first. The theory follows.

## The Roadmap

| Part | The Incident | What You Learn |
|------|-------------|----------------|
| 1 | You build the engine | Project setup, naive implementation |
| 2 | A customer gets charged twice | Race conditions → `AtomicReference`, CAS |
| 3 | The dashboard shows stale data | CPU cache visibility → `volatile` |
| 4 | 3,000 metrics vanish | Lost increments → `LongAdder`, `AtomicLong` |
| 5 | A fraud alert waits 47 minutes | Priority inversion → `PriorityBlockingQueue` |
| 6 | One stuck API freezes everything | Thread starvation → timeouts, `Future.cancel()` |
| 7 | Two jobs wait for each other forever | Deadlocks → DFS cycle detection |
| 8 | Black Friday crashes the server | Backpressure → bounded queue + rejection |
| 9 | All DB connections vanish | Resource exhaustion → `ArrayBlockingQueue` (fair) |
| 10 | You ship the final engine | Everything combined, 26 tests |
| 11 | The VP wants a live URL by Thursday | Docker, CI/CD, cloud deployment |
| 12 | Someone deletes 12,000 records | PostgreSQL, auth, audit trail |
| 13 | A stolen token floods the queue | Rate limiting → token bucket with CAS |
| 14 | A deploy kills 47 running jobs | Graceful shutdown → drain period, health checks |

## Prerequisites

Three things: Java 25, Gradle 9.1, and a terminal.

### Java 25 (Amazon Corretto)

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

### Gradle 9.1

You don't need to install Gradle globally — the project includes a wrapper (`gradlew`) that downloads it automatically. But if you want it:

```bash
sdk install gradle 9.1
```

### IDE (Optional)

Any text editor works. IntelliJ IDEA or VS Code recommended but not required.

### Quick Check

```bash
java -version && echo "---" && gradle --version
```

If both print version numbers, you're good. Let's build the engine.

---

[Chapter 1: Your First Day →](part-01-project-setup.md)
