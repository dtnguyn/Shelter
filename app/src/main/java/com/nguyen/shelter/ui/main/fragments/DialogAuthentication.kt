package com.nguyen.shelter.ui.main.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.paging.ExperimentalPagingApi
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.nguyen.shelter.R
import com.nguyen.shelter.ui.main.viewmodels.MainStateEvent
import com.nguyen.shelter.ui.main.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.dialog_authentication.view.*

private const val RC_SIGN_IN = 1

class DialogAuthentication(private val viewModel: MainViewModel, private val activity: Activity): DialogFragment() {

    private lateinit var callbackManager: CallbackManager

    @ExperimentalPagingApi
    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_authentication, null)

            val fbButton = view.fb_button
            val ggButton = view.google_button

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(activity, gso)

            ggButton.setOnClickListener {
                val signInIntent = googleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }


            callbackManager = CallbackManager.Factory.create()

            fbButton.setOnClickListener {
                LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile", "email"))
            }
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult?> {
                    override fun onSuccess(loginResult: LoginResult?) {
                        loginResult?.accessToken?.let {
                            viewModel.setStateEvent(MainStateEvent.FacebookAuthenticate(it, activity))
                            dismiss()
                        }

                    }

                    override fun onCancel() {
                        println("debug: Login Cancel")
                    }

                    override fun onError(exception: FacebookException) {
                        println("debug: Login Error")
                    }
                })


            builder.setView(view)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    @ExperimentalPagingApi
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        //Facebook login result
        callbackManager.onActivityResult(requestCode, resultCode, data)

        //Google login result
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                viewModel.setStateEvent(MainStateEvent.GoogleAuthenticate(account.idToken!!, activity))
                dismiss()
            } catch (e: ApiException) {

            }
        }



    }
}