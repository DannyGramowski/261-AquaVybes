import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AquaEditComponent } from './aqua-edit.component';

describe('AquaEditComponent', () => {
  let component: AquaEditComponent;
  let fixture: ComponentFixture<AquaEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AquaEditComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AquaEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
