package com.glureau.poc.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.glureau.poc.R
import com.glureau.poc.common.extensions.exhaustive
import com.glureau.poc.common.extensions.observeViewEffects
import com.glureau.poc.common.extensions.observeViewState
import com.glureau.poc.di.viewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_users.*


class UsersFragment : Fragment() {

    companion object {
        fun newInstance() = UsersFragment()
    }

    private val viewModel by viewModel<UsersViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = UsersAdapter()
        recycler_view.adapter = adapter

        viewModel.observeViewState(this) { state ->
            adapter.submitList(state.users)
        }

        viewModel.observeViewEffects(this) { effect ->
            when (effect) {
                is UserViewEffects.IoErrorMessage -> showSnackbar(effect, true)
                else -> showSnackbar(effect, false)
            }.exhaustive
        }
    }

    private fun showSnackbar(effect: UserViewEffects, temporary: Boolean = true) {
        Snackbar.make(
            recycler_view,
            effect.message,
            if (temporary) Snackbar.LENGTH_SHORT else Snackbar.LENGTH_INDEFINITE
        )
            .setAction(effect.action) { /* action callback ignored */ }
            .setActionTextColor(effect.color)
            .show()
    }
}
