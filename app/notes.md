# Cancellation and exceptions
## KotlinConf 2019


### CoroutineScope
Keep track of coroutines
Ability to cancel
Notified when a failure happens (exception)

coroutine 101 example
```kotlin
val scope = CoroutineScope(Job())
val job = scope.launch {
    //Coroutine
}
```

### Job
Handle to the coroutine
Provides lifecycle
Coroutine hierarchy

### Job lifecycle

states:  
- new
- active
- completing
- completed
- cancelling
- cancelled

properties:  
- isActive
- isCancelled
- isCompleted

by default coroutines launched eagerly but can be launched lazily 

tip: will be in completing state until all children are completed

### CoroutineContext
set of elements

CoroutineDispatcher -> Threading (Dispatchers.Default == computation)
Job -> Lifecycle
CoroutineExceptionHandler (default none)
CoroutineName -> "meow"

a new coroutine inherits the parent context by default + new Job()

### Understanding cancellation
cancelling a scope cancels its children

Android pre-built scopes
 - auto call cancel for you based on lifecycle
`viewModelScope`
`lifecycleScope` 

#### a cancelled child doesn't cancel other siblings
CancellationException is special for coroutines
this is how a child tells a parent they were cancelled and how parent knows to to do anything else

### cancellation is cooperative 

```kotlin
//checks if the coroutine is in the active state, use in if check in your coroutine 
job.isActive

// checks isActive and throws CancellationException if false
ensureActive() 


// for CPU heavy tasks, yields to other coroutines (in the dispatcher)
// checks if job was already completed, if yes throws CancellationException (huh)?
yield() 
```

### `suspendCancellableCoroutine`

essentially gives you a callback when your coroutine is cancelled  

useful for cleaning up resources

```kotlin
suspendCancellableCoroutine { continuation ->
   val call = oldSchoolRetrofitCallbackCall() // Opens some resource
   continuation.invokeOnCancellation {
       call.close() // Ensures the resource is closed on cancellation
   }
}

///////////////////// or  ///////////////////////////

suspendCancellableCoroutine { continuation ->
    val file = openFile() // Opens some resource
    continuation.invokeOnCancellation {
        file.close() // Ensures the resource is closed on cancellation
    }
}
```
#### `suspendCancellableCoroutine` is not in a Job or CoroutineScope context
This means... no
```kotlin
job.isActive()
ensureActive()
yield()
```

### `join()` 
will block until job is finished
ignores cancel
makes sure the coroutine finishes completely
no action if completed

### `await()`
same behavior as `join()`
if called after cancelled, JobCancellationException is thrown and CRASH
throws if completed


## Handling cancellation
check `isActive`
try-catch

**a cancellable coroutine is not able to suspend!**

so can use

```kotlin
val job = launch {
    try {
        work()
    } finally {
        withContext(NonCancellable) {
            //stuff that must happen but it's ok it suspends
        }
    }
}
```

## Exceptions (oh shit)

#### Failed `Job`
```kotlin
val scope = CoroutineScope(Job())
```
The failure of a `Job` *cancels* the parent `Job` and other siblings

child fails -> tells parent -> parent cancels all other children -> parent cancels its parent

true for both types of jobs: exceptions that are not handled bubble up

the failure of a child cancels the `scope` and other children

**A cancelled scope cannot start more coroutines**

#### `SupervisorJob()`

```kotlin
val scope = CoroutineScope(SupervisorJob())
```
the failure or cancellation of a child doesn't affect other children
true for both types of jobs: exceptions that are not handled bubble up


GOTCHA re: launch and jobs and parents
//parents always want you to launch your own job
```kotlin
val scope = CoroutineScope(Job())
//scope job

//supervisor job
scope.launch(SupervisorJob()) {
    
    //launch job
    launch { // creates its own job
        //child 1 launch job
    }
    launch {
        //child 2 launch job
    }
    //end launch job
}
//end supervisor job


//end scope job
```

how to solve this?

```kotlin
val scope = CoroutineScope(Job())
scope.launch(SupervisorJob()) {
    supervisorScope {
        launch { // now failure wont cancel child 2
            //child 1
        }
        launch { // now failure wont cancel child 1
            //child 2 
        }
    }
}
```

alternatively

```kotlin
val scope = CoroutineScope(Job())
val sharedJob = SupervisorJob()  // parent job

// non-nested approach
scope.launch(sharedJob) { // now failure wont cancel child 2
    //child 1
}
scope.launch(sharedJob) { // now failure wont cancel child 1
    //child 2 
}
```

### How to deal with exceptions

#### Categories
Thrown -> `launch`
Exposed -> `async`

example of exposed
```kotlin
val scope = CoroutineScope(Job())
scope.launch {
    async {
        // If async throws 
        // launch throws 
        // without calling await()
    }
}
```

#### use `try-catch`

```kotlin
scope.launch {
    try {
        codeThatCanThrowOhNo()
    } catch (e: Exception) {
        // handle
    }
}
```
but with async/await
```kotlin
supervisorScope { // not gonna notify parent nor handle exception, exception in await not sent up
    val deferred = async {
        codeThatCanThrowOhNo()
    }
    try {
        deferred.await()
    } catch (e: Exception) {
        // handle exception thrown in async
    }
}
```

#### `runCatching`

```kotlin
scope.launch {
    val result = runCatching {
        codeThatCanThrowOhNo()
    }
    
    if (result.isSuccess) {
        meowParty()
    }
}
```

#### `CoroutineExceptionHandler`
Handles uncaught exceptions
optional element in the `CoroutineContext`

```kotlin
import kotlin.coroutines.coroutineContext

val handler = CoroutineExceptionHandler { coroutineContext, exception ->
    println("meow handled: $exception")
}
```
called with automatically propagated exceptions
- works with `launch` (only root? unclear)
- but not `async`

root scope or `supervisorScope` direct child
