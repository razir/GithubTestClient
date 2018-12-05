package com.anton.github2.presentation.profile.adapter

import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anton.github2.R
import com.anton.github2.data.entity.Notification
import com.anton.github2.data.entity.SUBJECT_TYPE_ALERT
import com.anton.github2.data.entity.SUBJECT_TYPE_ISSUE
import com.anton.github2.data.entity.SUBJECT_TYPE_PULL_REQUEST
import com.anton.github2.extensions.name
import com.anton.github2.extensions.parseDate
import kotlinx.android.synthetic.main.item_notification.view.*
import kotlin.properties.Delegates

class NotificationsAdapter(private val onNotificationClick: (notification: Notification) -> Unit) :
    RecyclerView.Adapter<NotificationsAdapter.NotificationHolder>() {

    init {
        setHasStableIds(true)
    }

    var notifications: List<Notification> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): NotificationHolder {
        return NotificationHolder(
            LayoutInflater.from(viewGroup.context).inflate(
                R.layout.item_notification,
                viewGroup,
                false
            )
        )
    }

    override fun getItemCount() = notifications.size

    override fun onBindViewHolder(holder: NotificationHolder, pos: Int) {
        holder.bind(notifications[pos])
    }

    override fun getItemId(position: Int): Long {
        return notifications[position].id.hashCode().toLong()
    }

    inner class NotificationHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onNotificationClick(notifications[pos])
                }
            }
        }

        fun bind(notification: Notification) {
            itemView.apply {
                notificationTitle.text = notification.subject?.title
                notification.subject?.type?.let { type ->
                    var color = 0
                    var text = ""
                    val context = itemView.context
                    when (type) {
                        SUBJECT_TYPE_ALERT -> {
                            text = context.getString(R.string.subject_alert)
                            color = R.color.colorSubjectAlert
                        }
                        SUBJECT_TYPE_ISSUE -> {
                            text = context.getString(R.string.subject_issue)
                            color = R.color.colorSubjectIssue
                        }
                        SUBJECT_TYPE_PULL_REQUEST -> {
                            text = context.getString(R.string.subject_pull_request)
                            color = R.color.colorSubjectPullRequest
                        }
                        else -> {
                            text = type
                            color = R.color.colorSubjectDefault
                        }
                    }
                    val drawable =
                        ContextCompat.getDrawable(itemView.context, R.drawable.bg_notification_type) as GradientDrawable
                    drawable.setColor(ContextCompat.getColor(itemView.context, color))
                    notificationBadge.text = text
                    notificationBadge.background = drawable
                }


                notificationRepositoryName.text = notification.repository?.name
                notificationDate.text = notification.updatedAt?.parseDate()?.name(itemView.context)
            }
        }
    }

}