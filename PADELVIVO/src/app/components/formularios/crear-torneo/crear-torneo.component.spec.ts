import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrearTorneoComponent } from './crear-torneo.component';

describe('CrearTorneoComponent', () => {
  let component: CrearTorneoComponent;
  let fixture: ComponentFixture<CrearTorneoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CrearTorneoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CrearTorneoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
