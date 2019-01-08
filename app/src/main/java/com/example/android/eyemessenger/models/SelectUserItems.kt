package com.example.android.eyemessenger.models

import com.example.android.eyemessenger.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.single_user_layout.view.*

class SelectUserItems(val user: User): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.single_user_layout
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.select_user_name.text = user.userName
    }

}