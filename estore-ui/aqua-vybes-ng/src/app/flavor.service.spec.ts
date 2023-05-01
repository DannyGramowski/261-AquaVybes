import { TestBed } from '@angular/core/testing';

import { FlavorService } from './flavor.service';

describe('FlavorService', () => {
  let service: FlavorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FlavorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
