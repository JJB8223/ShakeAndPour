import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KitsDisplayComponent } from './kits-display.component';

describe('KitsDisplayComponent', () => {
  let component: KitsDisplayComponent;
  let fixture: ComponentFixture<KitsDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [KitsDisplayComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(KitsDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
