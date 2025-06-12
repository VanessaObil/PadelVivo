import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaActualizacionesComponent } from './lista-actualizaciones.component';

describe('ListaActualizacionesComponent', () => {
  let component: ListaActualizacionesComponent;
  let fixture: ComponentFixture<ListaActualizacionesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListaActualizacionesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListaActualizacionesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
