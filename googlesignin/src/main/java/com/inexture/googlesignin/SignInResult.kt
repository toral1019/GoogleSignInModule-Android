package com.inexture.googlesignin

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

open class SignInResult {
    internal var onError: (() -> Unit)? = null
    internal var onSuccess: ((account: GoogleSignInAccount?) -> Unit)? = null
    var requestEmail:Boolean? = null

    constructor(requestEmail:Boolean){
        this.requestEmail = requestEmail
    }

    //DSL
    fun onError(func: (() -> Unit)?) {
        this.onError = func
    }

    fun onSuccess(func: ((account: GoogleSignInAccount?) -> Unit)?) {
        this.onSuccess = func
    }
}
