# ThousandEyesApp

ThousandEyesApp is an Android application designed to monitor and display network host information, including metrics such as success rates and latency. Built with modern Android development practices, it leverages Kotlin, Jetpack Compose, and follows the MVVM architecture pattern.

---

## Features

- **Host Monitoring**: Fetches and displays a list of network hosts with relevant metrics.
- **Real-Time Updates**: Utilizes StateFlow for real-time UI updates.
- **Error Handling**: Provides clear feedback in case of network or data retrieval issues.
- **Modern UI**: Implements a clean and responsive interface using Jetpack Compose.

---

## Architecture

The application is structured using the **Model-View-ViewModel (MVVM)** pattern:

- **Model**: Handles data operations and business logic.
- **ViewModel**: Manages UI-related data and handles communication between the Model and View.
- **View**: Composable functions that render the UI based on the state provided by the ViewModel.

---

## Tech Stack

- **Kotlin**: Primary programming language.
- **Jetpack Compose**: For building declarative UI components.
- **StateFlow**: Manages and observes UI state.
- **Coroutines**: Handles asynchronous operations.
- **Hilt**: Dependency injection framework.
- **Retrofit**: For network API calls.
- **OkHttp**: HTTP client for network requests.

---

## Getting Started

### Prerequisites

- **Android Studio**: Version Arctic Fox (2020.3.1) or later.
- **Java Development Kit (JDK)**: Version 11 or higher.

### Installation

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/KesavanPanneerselvam/ThousandEyesApp.git
2. **Open in Android Studio**:
- Launch Android Studio.
- Select File > Open and navigate to the cloned repository's directory.
3. **Build the Project**:
- Click on the Sync Project with Gradle Files button to ensure all dependencies are downloaded.
4. **Run the Application**:
- Connect an Android device or start an emulator.
- Click the Run button to install and launch the app.

### Testing
The project includes unit tests to ensure the reliability of the ViewModel and other components.

### Running Tests
To execute the tests:

1. **Open the Test Directory:**
- Navigate to app/src/test/java/com/interview/cisco/thousandeyes/ in Android Studio.
2. **Run Tests:**
- Right-click on the desired test class or method.
- Select Run to execute the tests.

---
## Screenshot

**Unsorted Results:**

<img width="335" alt="image" src="https://github.com/user-attachments/assets/702219cf-8e13-4070-8b71-d6b7aa035a83" />

**Sorted Results:**

<img width="335" alt="image" src="https://github.com/user-attachments/assets/1315c293-cb70-43e1-82cf-d11463811f10" />

---
## PingLib Module
The **PingLib** module is a custom library integrated into the application to perform network reachability checks for hosts. It provides functionality to calculate average latency, success rates, and other network metrics in a highly efficient and reusable manner.


**Features of PingLib**

- **Concurrent Pinging**: Supports pinging multiple hosts concurrently using coroutines.
- **Latency Calculation**: Measures and reports the average latency for network requests.
- **Success and Failure Rates**: Tracks the number of successful and failed pings for each host.
- **Kotlin-First Design**: Fully written in Kotlin for modern Android development.
PingLib API

## PingLib Object

The main entry point for the PingLib module is the `PingLib` object. Below are its key functions:

### 1. `pingHost`
- **Description**: Pings a single host and calculates its latency and success rate.
- **Signature**:
  ```kotlin
  suspend fun pingHost(address: String, pingCount: Int = 5): PingResult

**Parameters:**
- `address`: The URL or IP address of the host to ping.
- `pingCount`: The number of ping attempts (default is 5).

**Returns**

A `PingResult` object containing the results of the ping operation.

### 2. `pingHostsConcurrently`

- **Description**: Pings multiple hosts concurrently and returns a list of results.
- **Signature**:
```kotlin
suspend fun pingHostsConcurrently(urls: List<String>, pingCount: Int = 5): List<PingResult>
```
**Parameters:**
- `urls`: A list of URLs or IP addresses to ping.
- `pingCount`: The number of ping attempts for each host (default is 5).

**Returns**

A list of PingResult objects.

`PingResult` Class

Represents the result of a ping operation:
```kotlin
data class PingResult(
    val url: String,
    val averageLatency: Long?,
    val total: Int,
    val success: Int,
    val failure: Int
)
```
---

### Example Usage

Below is an example of how to use PingLib in your project:

```kotlin
import com.interview.cisco.thousandeyes.pinglib.PingLib
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val result = PingLib.pingHost("google.com")
    println("URL: ${result.url}")
    println("Average Latency: ${result.averageLatency} ms")
    println("Total Pings: ${result.total}")
    println("Success: ${result.success}")
    println("Failure: ${result.failure}")
}
```


### Integration

To include PingLib in your project:

1. Add it as a module or publish it as a library on Maven/Gradle.
2. Import and call its APIs as shown in the example.
