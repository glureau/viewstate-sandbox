package com.glureau.viewstatepattern.view_state

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.glureau.viewstatepattern.R
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import kotlinx.android.synthetic.main.fragment_main.*

class ViewStateFragment : Fragment() {

    private val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }

    companion object {
        fun newInstance() = ViewStateFragment()
    }

    // TODO: No DI = cannot be easily tested (but UI has NO LOGIC, so no test required)
    //  + highly coupled (don't care here, we know why we'll use Dagger)
    private val viewModel = ViewStateViewModel()
    private val adapter = ViewStateUserAdapter()

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

        var debugLastState: ViewStateViewModel.ViewState? = null
        viewModel.viewState
            .autoDisposable(scopeProvider)
            .subscribe { state ->
                first_name.setTextIfDifferent(state.firstName)
                first_name.error = state.firstNameError
                last_name.setTextIfDifferent(state.lastName)
                last_name.error = state.lastNameError
                age.setTextIfDifferent(state.age)
                age.error = state.ageError
                error_message.text = state.submitError
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
