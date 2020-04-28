# Reactive

### Difference between map and flatMap [https://medium.com/@nikeshshetty/5-common-mistakes-of-webflux-novices-f8eda0cd6291]
* flatMap should be used for non-blocking operations, or in short anything which returns back Mono,Flux.
* map should be used when you want to do the transformation of an object /data in fixed time. The operations 
which are done synchronously.

