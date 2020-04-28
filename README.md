# Reactive

### Difference between map and flatMap 
[https://medium.com/@nikeshshetty/5-common-mistakes-of-webflux-novices-f8eda0cd6291]
* flatMap should be used for non-blocking operations, or in short anything which returns back Mono,Flux.
* map should be used when you want to do the transformation of an object /data in fixed time. The operations 
which are done synchronously.

Good articles I followed:
[https://medium.com/@cheron.antoine/reactor-java-1-how-to-create-mono-and-flux-471c505fa158] --> all 4 parts
[https://medium.com/faun/reactive-programming-using-spring-webflux-ad3cfc6f0471]
[https://medium.com/swlh/building-a-crud-with-spring-webflux-5606a0fccc31]
