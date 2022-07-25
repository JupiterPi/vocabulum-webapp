import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PortionsOverviewComponent } from './portions-overview.component';

describe('PortionsOverviewComponent', () => {
  let component: PortionsOverviewComponent;
  let fixture: ComponentFixture<PortionsOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PortionsOverviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PortionsOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
