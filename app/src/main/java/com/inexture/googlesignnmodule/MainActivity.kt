package com.inexture.googlesignnmodule

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.inexture.googlesignin.googleSignInHandler
import com.inexture.googlesignnmodule.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mBinding.btnGoogleSignIn.setOnClickListener {
            googleSignInHandler(requestEmail = false,target = null) {
                onSuccess {
                    Toast.makeText(this@MainActivity, "${it?.email} - ${it?.displayName} - ${it?.photoUrl}", Toast.LENGTH_LONG).show()
                }
                onError {
                    Toast.makeText(this@MainActivity, "error", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
