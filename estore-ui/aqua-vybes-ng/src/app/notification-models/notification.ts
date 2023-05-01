import { NotificationType } from "./notification-types"

export interface myNotification {
    message: string,
    type: NotificationType,
    duration: number
}
