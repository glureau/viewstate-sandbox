package com.glureau.viewstatepattern.naive_commented

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.glureau.viewstatepattern.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_main.*

class NaiveCommentedFragment : Fragment() {

    companion object {
        fun newInstance() = NaiveCommentedFragment()
    }

    // TODO: No DI = cannot be easily tested (but UI so no unit test anyway)
    //  + highly coupled (don't care here, we know why we use Dagger)
    private val viewModel = NaiveCommentedViewModel()
    private val adapter = NaiveCommentedUserAdapter()
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
        register.setOnClickListener {
            // TODO: Manage error cases (i.e. "null" strings or cannot transform to integer) => crash
            // TODO: Live validation of the business rules (display warning when wrong data)
            val firstName = first_name.editText?.text.toString()
            val lastName = last_name.editText?.text.toString()
            val age = age.text.toString().toInt()
            // TODO: Display error when registration fail
            viewModel.registerNewUser(firstName, lastName, age)
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