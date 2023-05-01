import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AquaCreateComponent } from './aqua-create.component';

describe('AquaCreateComponent', () => {
  let component: AquaCreateComponent;
  let fixture: ComponentFixture<AquaCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AquaCreateComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AquaCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
