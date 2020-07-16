# lupin

![Professor Lupin](professor-lupin.png)

> “Water demon,” said Lupin, surveying the grindylow thoughtfully. “We shouldn’t have much difficulty with him, not after the kappas. The trick is to break his grip. You notice the abnormally long fingers? Strong, but very brittle.”


[get orientation return values](https://developer.android.com/reference/android/hardware/SensorManager#getOrientation "Android Developer Doc")


## Design Decisions

1. Workmanager for periodic tasks - has to be more than 15 minutes. This will be good to periodically record pitch & roll throughout the night. 
2. SensorManager registerListener params - takes two ints: a sampling period and a max latency. these can have an effect but should not be relied upon solely. instead:
3. setup and tear down the listeners as part of every periodic batch of work
