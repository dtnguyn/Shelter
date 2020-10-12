package com.nguyen.shelter.ui.community.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.paging.ExperimentalPagingApi
import com.facebook.CallbackManager
import com.nguyen.shelter.R
import com.nguyen.shelter.ui.community.viewmodels.BlogViewModel
import com.nguyen.shelter.ui.community.viewmodels.MainStateEvent
import kotlinx.android.synthetic.main.dialog_postal_code.view.*

private const val RC_SIGN_IN = 1

class DialogPostalCode(private val viewModel: BlogViewModel, private val activity: Activity): DialogFragment() {

    private lateinit var callbackManager: CallbackManager

    @ExperimentalPagingApi
    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let { activity ->
            val builder = AlertDialog.Builder(activity)

            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.dialog_postal_code, null)

            builder.setView(view)
                .setPositiveButton(R.string.save) { _, _ ->
                    val newPostalCode = view.postal_code_edit_text.text.toString()
                    if(newPostalCode != viewModel.postalCode.value && newPostalCode.isNotBlank()){
                        viewModel.setStateEvent(MainStateEvent.SetPostalCode(newPostalCode))
                        viewModel.setStateEvent(MainStateEvent.SetArea(null))
                    }
                    dismiss()
                }
                .create()
        }
    }

}