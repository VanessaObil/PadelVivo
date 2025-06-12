import { Component, OnInit } from '@angular/core';
import { WebsocketService } from '../../../services/websocket-service.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
// Update the import path below if your Notification model is located elsewhere
import { Notification } from '../../../models/Notification';


@Component({
  selector: 'app-notificationpanel',
  templateUrl: './notificationpanel.component.html',
  styleUrls: ['./notificationpanel.component.css'],
  imports: [CommonModule, FormsModule],
})
export class NotificationPanelComponent implements OnInit {

  notifications: Notification[] = [];
  showPanel = false;

  constructor(private websocketService: WebsocketService) {}

  ngOnInit(): void {
    this.websocketService.notifications$.subscribe(n => this.notifications = n);
  }

  togglePanel(): void {
    this.showPanel = !this.showPanel;
  }
}
