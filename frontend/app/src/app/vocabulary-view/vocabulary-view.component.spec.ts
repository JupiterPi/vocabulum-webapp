import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VocabularyViewComponent } from './vocabulary-view.component';

describe('VocabularyViewComponent', () => {
  let component: VocabularyViewComponent;
  let fixture: ComponentFixture<VocabularyViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VocabularyViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VocabularyViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
