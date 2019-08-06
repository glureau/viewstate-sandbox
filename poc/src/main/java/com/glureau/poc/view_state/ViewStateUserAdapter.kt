package com.glureau.poc.view_state

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.glureau.poc.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_user.view.*

class ViewStateUserAdapter : ListAdapter<UserViewState, ViewStateUserAdapter.UserViewStateViewHolder>(
    UserViewStateDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewStateViewHolder {
        val userView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewStateViewHolder(userView)
    }

    override fun onBindViewHolder(holder: UserViewStateViewHolder, position: Int) {
        val item = getItem(position)
        holder.containerView.apply {
            userFirstName.text = item.firstName
            userLastName.text = item.lastName
            userAge.text = "${item.age} years"
        }
    }

    class UserViewStateDiffCallback : DiffUtil.ItemCallback<UserViewState>() {
        override fun areItemsTheSame(oldItem: UserViewState, newItem: UserViewState) =
            oldItem.firstName == newItem.firstName && oldItem.lastName == newItem.lastName

        override fun areContentsTheSame(oldItem: UserViewState, newItem: UserViewState) = oldItem == newItem
    }

    data class UserViewStateViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer
}