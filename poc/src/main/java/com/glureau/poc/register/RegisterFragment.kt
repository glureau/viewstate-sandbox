package com.glureau.poc.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.glureau.poc.R
import com.glureau.poc.common.extensions.setErrorIfDifferent
import com.glureau.poc.common.extensions.setTextIfDifferent
import com.glureau.poc.common.extensions.textChanges
import com.glureau.poc.di.appInjector
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject

class RegisterFragment : Fragment() {

    private val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }

    companion object {
        fun newInstance() = RegisterFragment()
    }

    @Inject
    lateinit var viewModel: RegisterViewModel

    //TODO : Improve adapter
    private val adapter = RegisterUserAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appInjector().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    // TODO: Not using savedInstanceState = all values set in the form will be lost on recreation (screen rotation or background activity cleared by Android)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onFirstNameChanged(first_name.textChanges())
        viewModel.onLastNameChanged(last_name.textChanges())
        viewModel.onAgeChanged(RxTextView.textChanges(age))
        viewModel.onSubmit(RxView.clicks(register))

        var debugLastState: RegisterViewState? = null
        viewModel.viewState
            .autoDisposable(scopeProvider)
            .subscribe { state ->
                first_name.setTextIfDifferent(state.firstName)
                first_name.setErrorIfDifferent(state.firstNameError)
                last_name.setTextIfDifferent(state.lastName)
                last_name.setErrorIfDifferent(state.lastNameError)
                age.setTextIfDifferent(state.age)
                age.setErrorIfDifferent(state.ageError)
                error_message.setTextIfDifferent(state.submitError)

                adapter.submitList(state.users)

                debugLastState = state
            }

        recycler_view.adapter = adapter

        // Just a showcase on how easy a view debug can be with this pattern
        debug.setOnClickListener {
            Toast.makeText(context, debugLastState.toString(), Toast.LENGTH_LONG).show()
            Log.e("DEBUG", debugLastState.toString())
        }
    }
}
