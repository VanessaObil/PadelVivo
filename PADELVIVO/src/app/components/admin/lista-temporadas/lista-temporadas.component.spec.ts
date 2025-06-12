import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaTemporadasComponent } from './lista-temporadas.component';

describe('ListaTemporadasComponent', () => {
  let component: ListaTemporadasComponent;
  let fixture: ComponentFixture<ListaTemporadasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListaTemporadasComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListaTemporadasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
