import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrearJugadorComponent } from './crear-jugador.component';

describe('CrearJugadorComponent', () => {
  let component: CrearJugadorComponent;
  let fixture: ComponentFixture<CrearJugadorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CrearJugadorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CrearJugadorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
