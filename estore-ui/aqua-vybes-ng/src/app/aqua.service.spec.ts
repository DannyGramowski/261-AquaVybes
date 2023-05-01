import { TestBed } from '@angular/core/testing';

import { AquaService } from './aqua.service';

describe('AquaService', () => {
  let service: AquaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AquaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
