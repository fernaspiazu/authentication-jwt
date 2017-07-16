[<img src="https://img.shields.io/travis/playframework/play-scala-starter-example.svg"/>](https://travis-ci.org/playframework/play-scala-starter-example)

# authentication-jwt

This is a Play starter application example that shows how to perform both authentication and authorization by using [Json Web Token](https://tools.ietf.org/html/rfc7519).

Inspired on pauldijou jwt-scala examples [play-angular-standalone](https://github.com/pauldijou/jwt-scala/tree/master/examples/play-angular-standalone) upgraded to Play 2.6

## Running

Run this using [sbt](http://www.scala-sbt.org/):

```
sbt run
```

And then go to http://localhost:9000 to see the running web application.

## Controller

- **SecuredController.scala:**

  Contains Actions to perform authentication and demonstrate an authorization example.

## Custom Actions

- **Secured.scala:**

	Defines 2 Custom Actions Builders:
	
	- *AuthenticatedActionBuilder*
	- *AdminActionBuilder*

	Both actions are then injected in a **SecuredControllerComponents** (that extends ControllerComponents) and will be injected in our SecuredController.
	
## Scalafmt

Used lucidchart version:

```
addSbtPlugin("com.lucidchart" % "sbt-scalafmt" % "1.8")
```