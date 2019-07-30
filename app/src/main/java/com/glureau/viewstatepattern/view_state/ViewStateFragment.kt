package com.glureau.viewstatepattern.view_state

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        first_name.editText?.let { editText -> viewModel.onFirstNameChanged(RxTextView.textChanges(editText)) }
        last_name.editText?.let { editText -> viewModel.onLastNameChanged(RxTextView.textChanges(editText)) }
        viewModel.onAgeChanged(RxTextView.textChanges(age))
        viewModel.onSubmit(RxView.clicks(register))

        viewModel.viewState
            .autoDisposable(scopeProvider)
            .subscribe { state ->
                if (first_name.editText?.text.toString() != state.firstName) {
                    first_name.editText?.setText(state.firstName)
                }
                first_name.error = state.firstNameError

                if (last_name.editText?.text.toString() != state.lastName) {
                    last_name.editText?.setText(state.lastName)
                }
                last_name.error = state.lastNameError

                if (age.text.toString() != state.age) {
                    age.setText(state.age)
                }
                age.error = state.ageError

                error_message.text = state.submitError

                adapter.submitList(state.users)
            }
    }
}
