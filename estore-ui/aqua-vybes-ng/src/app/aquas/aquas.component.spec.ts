import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AquasComponent } from './aquas.component';

describe('AquasComponent', () => {
  let component: AquasComponent;
  let fixture: ComponentFixture<AquasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AquasComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AquasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
