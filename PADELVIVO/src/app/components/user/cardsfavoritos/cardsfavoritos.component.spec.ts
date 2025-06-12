import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CardsfavoritosComponent } from './cardsfavoritos.component';

describe('CardsfavoritosComponent', () => {
  let component: CardsfavoritosComponent;
  let fixture: ComponentFixture<CardsfavoritosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CardsfavoritosComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CardsfavoritosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
