import { Injectable } from '@angular/core';
import SockJS from 'sockjs-client';
import { Client, IMessage, Stomp } from '@stomp/stompjs';
import { BehaviorSubject } from 'rxjs';
import { Notification } from '../models/Notification' // <- esto es importante

 // Define este modelo tú mismo

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  private stompClient!: Client;
  private notificationsSubject = new BehaviorSubject<Notification[]>([]);
  public notifications$ = this.notificationsSubject.asObservable();

  private currentNotifications: Notification[] = [];

  constructor() {
    this.connect();
  }

  connect(): void {
    const token = localStorage.getItem('token');

    this.stompClient = new Client({
      brokerURL: 'ws://localhost:8080/ws', // tu endpoint STOMP
      connectHeaders: {
        Authorization: `Bearer ${token}`
      },
      debug: str => console.log('[WebSocket]', str),
      reconnectDelay: 5000,
      webSocketFactory: () => new SockJS('http://localhost:8080/ws')
    });

    this.stompClient.onConnect = () => {
      this.stompClient.subscribe('/user/queue/notifications', (message: IMessage) => {
        const notification: Notification = JSON.parse(message.body);
        this.currentNotifications.push(notification);
        this.notificationsSubject.next(this.currentNotifications);
      });
    };

    this.stompClient.activate();
  }
}
