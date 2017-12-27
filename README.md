# A Lab application for demonstration purpose only

## Build

```bash
$ docker build .
```

## Readiness

You can delay readiness test with `READINESS_DELAY` env. variable

You can make liveness test fails after some delay with the `LIVENESS_DELAY` variable.

## Leak memory

You can create a memory leak with the `/leak?size=bytes` endpoint.