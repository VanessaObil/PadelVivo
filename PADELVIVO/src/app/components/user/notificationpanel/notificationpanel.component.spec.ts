import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotificationpanelComponent } from './notificationpanel.component';

describe('NotificationpanelComponent', () => {
  let component: NotificationpanelComponent;
  let fixture: ComponentFixture<NotificationpanelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NotificationpanelComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NotificationpanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
