package com.glureau.viewstatepattern.naive

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.glureau.viewstatepattern.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_main.*

class NaiveFragment : Fragment() {

    companion object {
        fun newInstance() = NaiveFragment()
    }

    private val viewModel = NaiveViewModel()
    private val adapter = NaiveUserAdapter()
    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRegister()
        prepareRecyclerView()
    }

    private fun prepareRegister() {
        register.setOnClickListener {
            val firstName = first_name.editText?.text.toString()
            val lastName = last_name.editText?.text.toString()
            val age = age?.text.toString().toInt()
            viewModel.registerNewUser(firstName, lastName, age)
        }
    }

    private fun prepareRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(context)
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