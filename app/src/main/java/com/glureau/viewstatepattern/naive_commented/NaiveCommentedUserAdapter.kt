package com.glureau.viewstatepattern.naive_commented

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.glureau.viewstatepattern.R
import com.glureau.viewstatepattern.common.domain.User
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_user.view.*

class NaiveCommentedUserAdapter : ListAdapter<User, NaiveCommentedUserAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val userView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(userView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = getItem(position)
        holder.containerView.apply {
            userFirstName.text = item.firstName
            userLastName.text = item.lastName
            userAge.text = "${item.age} years"
        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User) =
            oldItem.firstName == newItem.firstName && oldItem.lastName == newItem.lastName

        override fun areContentsTheSame(oldItem: User, newItem: User) = oldItem == newItem
    }

    data class UserViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer
}