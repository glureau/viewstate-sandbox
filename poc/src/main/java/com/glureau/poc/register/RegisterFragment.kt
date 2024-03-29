package com.glureau.poc.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.glureau.poc.R
import com.glureau.poc.common.extensions.observeViewState
import com.glureau.poc.common.extensions.setErrorIfDifferent
import com.glureau.poc.common.extensions.setTextIfDifferent
import com.glureau.poc.common.extensions.textChanges
import com.glureau.poc.di.viewModel
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    // Dagger not required anymore in Activity/Fragment ->
    // + Only constructor injection! Yeah
    // + No more `lazy val` BUT this one (it's lazy, instantiated in the onViewCreated))
    // + AppInjector + AppInjectorProvider should not be necessary anymore.
    // - Arch ViewModel providers could fail at runtime (Dagger is safer on this point)
    private val viewModel by viewModel<RegisterViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    // TODO: Not using savedInstanceState = all values set in the form will be lost on recreation (screen rotation or background activity cleared by Android)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onFirstNameChanged(first_name.textChanges())
        viewModel.onLastNameChanged(last_name.textChanges())
        viewModel.onAgeChanged(RxTextView.textChanges(age))
        viewModel.onSubmit(RxView.clicks(register))

        viewModel.observeViewState(this) { state ->
            first_name.setTextIfDifferent(state.firstName)
            first_name.setErrorIfDifferent(state.firstNameError)
            last_name.setTextIfDifferent(state.lastName)
            last_name.setErrorIfDifferent(state.lastNameError)
            age.setTextIfDifferent(state.age)
            age.setErrorIfDifferent(state.ageError)
            error_message.setTextIfDifferent(state.submitError)
        }
    }
}
