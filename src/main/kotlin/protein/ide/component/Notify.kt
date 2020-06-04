package protein.ide.component

import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.project.Project

fun show(
  project: Project,
  title: String,
  content: String,
  displayId: String,
  type: NotificationType,
  listener: NotificationListener
) {
  val group = NotificationGroup(
    displayId,
    NotificationDisplayType.STICKY_BALLOON,
    true
  )
  val notification = group.createNotification(title, content, type, listener)
  Notifications.Bus.notify(notification, project)
}
