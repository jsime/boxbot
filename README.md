# boxbot

A simple Clojure app presenting an API for tracking items in boxes.

## Usage

Start the server:

```
java -jar boxbot.jar --port 8080 --auth-secret abc123cantguessme
```

Register an account:

```
curl -X POST -d 'name=Joebob&email=joebob@domain.tld&password=seekret' http://localhost:8080/register
```

Get a JSON Web Token (JWT) to authenticate yourself to further API calls:

```
curl -X POST -d 'email=joebob@domain.tld&password=seekret' http://localhost:8080/login
```

_... more to follow as I actually tinker with this project ..._

## License

Copyright © 2016 Jon Sime

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
