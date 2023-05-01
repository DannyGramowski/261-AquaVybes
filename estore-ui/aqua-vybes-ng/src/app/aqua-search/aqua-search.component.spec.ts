import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AquaSearchComponent } from './aqua-search.component';

describe('AquaSearchComponent', () => {
  let component: AquaSearchComponent;
  let fixture: ComponentFixture<AquaSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AquaSearchComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AquaSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
