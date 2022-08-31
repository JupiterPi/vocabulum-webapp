import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TestChatViewComponent } from './test-chat-view.component';

describe('TestChatViewComponent', () => {
  let component: TestChatViewComponent;
  let fixture: ComponentFixture<TestChatViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TestChatViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TestChatViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
