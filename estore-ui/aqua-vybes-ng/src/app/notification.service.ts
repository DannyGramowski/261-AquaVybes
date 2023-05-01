import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { NotificationType } from './notification-models/notification-types';
import { myNotification } from './notification-models/notification';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor() { }
  public standardTime : number = 20;
  private notification$: Subject<myNotification | null> = new BehaviorSubject<myNotification | null>(null);

  public success(message: string, duration: number = 5000) {
    this.notify(message, NotificationType.Success, duration);
  }

  public warning(message: string, duration: number = 5000) {
    this.notify(message, NotificationType.Warning, duration);
  }

  public error(message: string, duration: number = 5000) {
    this.notify(message, NotificationType.Error, duration);
  }

  private notify(message: string, type:NotificationType, duration:number) {
    duration = !duration? 10000 : duration;
    this.notification$.next({
      message: message,
      type: type,
      duration: duration
    } as myNotification);
  }

  get notification() {
    return this.notification$.asObservable();
  }


}
