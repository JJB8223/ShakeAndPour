import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomKitComponent } from './custom-kit.component';

describe('CustomKitComponent', () => {
  let component: CustomKitComponent;
  let fixture: ComponentFixture<CustomKitComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CustomKitComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CustomKitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
