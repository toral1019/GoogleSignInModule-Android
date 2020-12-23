package com.inexture.googlesignin

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


fun Fragment?.googleSignOutHandler(
    callback: () -> Unit
): Any? {
    return googleSignOutHandler(this, callback)
}

fun googleSignInHandler(target: Any?, requestEmail: Boolean, callback: (SignInResult.() -> Unit)) {
    transactFragmentAndContinue(target) { fragment ->
        //apply callback for sign in
        val signInResult = SignInResult(requestEmail).apply(callback)
        fragment?.signIn(signInResult)
    }
}


private fun googleSignOutHandler(target: Any?, callback: (() -> Unit)) {
    transactFragmentAndContinue(target) { fragment ->
        fragment?.signOut()
        callback()
    }
}

private fun transactFragmentAndContinue(
    target: Any?,
    callback: (GoogleSignInManagerFragment?) -> Unit
) {
    if (target is AppCompatActivity || target is Fragment) {

        val context = when (target) {
            is Context -> target
            is Fragment -> target.context
            else -> null
        }

        var checkerFragment = when (context) {
            // for app compat activity
            is AppCompatActivity -> context.supportFragmentManager?.findFragmentByTag(
                GoogleSignInManagerFragment::class.java.canonicalName
            ) as GoogleSignInManagerFragment?
            // for support fragment
            is Fragment -> context.childFragmentManager.findFragmentByTag(
                GoogleSignInManagerFragment::class.java.canonicalName
            ) as GoogleSignInManagerFragment?
            // else return null
            else -> null
        }

        if (checkerFragment == null) {
            Log.d(
                "googleSignInHandler",
                "runWithPermissions: adding headless fragment for asking permissions"
            )
            checkerFragment = GoogleSignInManagerFragment.newInstance()
            when (context) {
                is AppCompatActivity -> {
                    context.supportFragmentManager.beginTransaction().apply {
                        add(checkerFragment, GoogleSignInManagerFragment::class.java.canonicalName)
                        commit()
                    }
                    // make sure fragment is added before we do any context based operations
                    context.supportFragmentManager?.executePendingTransactions()
                }
                is Fragment -> {
                    // this does not work at the moment
                    context.childFragmentManager.beginTransaction().apply {
                        add(checkerFragment, GoogleSignInManagerFragment::class.java.canonicalName)
                        commit()
                    }
                    // make sure fragment is added before we do any context based operations
                    context.childFragmentManager.executePendingTransactions()
                }
            }
        }
        callback(checkerFragment)
    }
}
