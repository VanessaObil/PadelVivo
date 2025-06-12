import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CardspartidosComponent } from './cardspartidos.component';

describe('CardspartidosComponent', () => {
  let component: CardspartidosComponent;
  let fixture: ComponentFixture<CardspartidosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CardspartidosComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CardspartidosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
