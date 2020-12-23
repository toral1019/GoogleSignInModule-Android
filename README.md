# GoogleSignInModule

**Easiest solution to google sign-in implementation,don't write whole code for google sign-in**

* Written in [**Kotlin**](http://kotlinlang.org)

* No need to add google sign-in liabrary

* Optional for request email

* Tiny in size

* Support AndroidX


## Usage

In your activity or fragment:

```kotlin
 googleSignInHandler(requestEmail = true) {
                onSuccess {
                    Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_LONG).show()
                }
                onError {
                    Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_LONG).show()
                }
            }
```

Which you could add your own dynamic requirement in paramerer  e mail and also you get callback which incorporate two procedure **onError** and **onSuccess**

- **onError** method is for error in google sign in.
- **onSuccess** method contains user profile data like email,photourl,displayname etc.