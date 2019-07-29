package com.glureau.viewstatepattern.full

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.glureau.viewstatepattern.R
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.concurrent.TimeUnit

class FullFragment : Fragment() {

    companion object {
        fun newInstance() = FullFragment()
        private const val DEBOUNCE_DELAY = 200L // ms
    }

    // TODO: No DI = cannot be easily tested (but UI so no unit test anyway)
    //  + highly coupled (don't care here, we know why we use Dagger)
    private val viewModel = FullViewModel()
    private val adapter = FullUserAdapter()
    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Not using savedInstanceState = all values set in the form will be lost on recreation (screen rotation or background activity cleared by Android)
        prepareRegister()
        prepareRecyclerView()
    }

    private fun prepareRegister() {
        first_name.editText?.let {
            disposables.add(RxTextView.textChanges(it)
                .debounce(DEBOUNCE_DELAY, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .map { str -> viewModel.validateFirstName(str.toString()) }
                .subscribe { validation ->
                    first_name.error = validation.errorMessage
                })
        }

        last_name.editText?.let {
            disposables.add(RxTextView.textChanges(it)
                .debounce(DEBOUNCE_DELAY, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .map { str -> viewModel.validateLastName(str.toString()) }
                .subscribe { validation ->
                    last_name.error = validation.errorMessage
                })
        }

        disposables.add(RxTextView.textChanges(age)
            .debounce(DEBOUNCE_DELAY, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .map { str -> viewModel.validateAge(str.toString()) }
            .subscribe { validation ->
                age.error = validation.errorMessage
            })

        register.setOnClickListener {
            if (viewModel.validateFirstName(first_name.editText?.text.toString()) == FullViewModel.NameValidation.OK &&
                viewModel.validateLastName(last_name.editText?.text.toString()) == FullViewModel.NameValidation.OK &&
                viewModel.validateAge(age.text.toString()) == FullViewModel.AgeValidation.OK
            ) {
                val firstName = first_name.editText?.text.toString()
                val lastName = last_name.editText?.text.toString()
                val ageStr = age.text.toString()

                viewModel.registerNewUser(firstName, lastName, ageStr)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        first_name.editText?.setText("")
                        last_name.editText?.setText("")
                        age.setText("")
                    }, {
                        Toast.makeText(context, "Cannot register: ${it.message}", Toast.LENGTH_LONG).show()
                    })
            } else {
                Toast.makeText(context, "Verify your data before register", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun prepareRecyclerView() {
        recycler_view.adapter = adapter
        disposables.add(
            viewModel.getAllUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    adapter.submitList(it)
                }
        )
    }

    override fun onDestroyView() {
        disposables.dispose()
        super.onDestroyView()
    }
}
