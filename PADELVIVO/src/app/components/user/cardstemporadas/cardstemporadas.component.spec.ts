import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CardstemporadasComponent } from './cardstemporadas.component';

describe('CardstemporadasComponent', () => {
  let component: CardstemporadasComponent;
  let fixture: ComponentFixture<CardstemporadasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CardstemporadasComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CardstemporadasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
