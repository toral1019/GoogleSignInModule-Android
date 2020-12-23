package com.inexture.googlesignin


import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class GoogleSignInManagerFragment : Fragment() {
    private var mGoogleSignInClient: GoogleSignInClient? = null

    companion object {
        private val RC_SIGN_IN = 9001
        fun newInstance(): GoogleSignInManagerFragment {
            val myFragment = GoogleSignInManagerFragment()
            return myFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    private var mSignInResult: SignInResult? = null

    /**
     * store sign in result globally and call startActivityForResult
     */
    fun signIn(signInResult: SignInResult) {
        mSignInResult = signInResult
        //Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            mSignInResult?.requestEmail?.let { isReqEmail->
               if (isReqEmail) gso.requestEmail()
            }
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = context?.let { GoogleSignIn.getClient(it, gso.build()) }
        val signInIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun signOut() {
        if (mGoogleSignInClient == null) {
            mGoogleSignInClient = context?.let {
                GoogleSignIn.getClient(it, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
            }
        }
        mGoogleSignInClient?.signOut()?.addOnSuccessListener {
            Log.d(TAG, "Logged out successfully")
        }
        mGoogleSignInClient?.signOut()?.addOnFailureListener {
            Log.d(TAG, "Failure logging out")
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            if (resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            } else {
                //for error
                mSignInResult?.onError?.invoke()
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            // successfully signin and
            account?.let { mSignInResult?.onSuccess?.invoke(account) }

        } catch (e: ApiException) {
            if (e.statusCode != 12501) mSignInResult?.onError?.invoke()

            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode + " : " + e.message)
            Log.e(TAG, "signInResult:failed code=$e")
        }

    }
}

