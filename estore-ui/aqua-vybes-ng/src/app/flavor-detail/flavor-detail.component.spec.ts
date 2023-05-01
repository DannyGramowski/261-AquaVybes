import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FlavorDetailComponent } from './flavor-detail.component';

describe('FlavorDetailComponent', () => {
  let component: FlavorDetailComponent;
  let fixture: ComponentFixture<FlavorDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FlavorDetailComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FlavorDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
