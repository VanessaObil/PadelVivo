import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CardstorneosComponent } from './cardstorneos.component';

describe('CardstorneosComponent', () => {
  let component: CardstorneosComponent;
  let fixture: ComponentFixture<CardstorneosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CardstorneosComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CardstorneosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
